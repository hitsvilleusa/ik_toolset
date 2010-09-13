# This class is the heart of the project to determine intrinsically
# knotted graphs.  It is objects of this Graph class that represents the
# graphs that we are testing.  The classification tests are performed on
# these objects.  The 'meaty' parts of this class are the minor_of?, 
# contains_minor?, subgraph_of?, and isomorphic_to? methods.
#
# An object of type Graph is immutable.  This means that upon initialization
# the edges of the graph are set and cannot be modified.  Any calls to 
# remove_vertex, remove_edges, contract_edge will result in a new graph
# being created.  The current graph will not be manipulated.
#
# This class does assume that the nauty tools--dretog, labelg, and planarg
# are in the environment PATH.
class Graph
  attr_reader :name
  attr_reader :order
  attr_reader :edges 
  attr_reader :safe_mode
  
  # Creates a new graph.
  #
  # The name is an arbitrary name for the graph.
  # The order is the number of vertices in the graph.
  # The edges are an array of all of the edges in the graph.
  # The safe mode allows checks to be performed on inputs to the graph.  If
  # set, edges and vertices will be confirmed to be valid before proceeding.
  # The reason that this is optional is because these validations,
  # while valuable, result in roughly a 30% performance hit during the
  # classification efforts.  Since over 99% of the graphs created in this
  # project are created indirectly as a result of a call to 'minor_of' we know
  # that the graphs are valid and do not need any validation, and thus it 
  # is safe to remove these tests.  If this class were used 'outside' of this 
  # single approach, then the conditions would not necessarily always be met.
  # For this reason the 'safe_mode' option is available and can be set to
  # which will perform the validations.
  #
  # Edges are simply two element arrays (ex. [1,2]).
  #
  # We create an adjaceny matrix in order to make 'easy' operations much 
  # more effecient.
  def initialize(name, order, edges, safe_mode=true)
    @name       = name
    @order      = order
    @edges      = edges
    @safe_mode  = safe_mode
    @adj_matrix = (0...@order).to_a.collect{Array.new(@order)}
    
    if @safe_mode
      Graph.validate_edges!(edges, @order)
      @edges = Graph.canonicalize_edges(@edges)
    end
    
    @edges.each do |edge|
      @adj_matrix[edge[0]][edge[1]] = @adj_matrix[edge[1]][edge[0]] = true
    end
  end
  
  # The number of edges in the graph.
  def size
    edges.size
  end
  
  # Determines if the supplied edge exists in the graph or not.
  def has_edge?(edge)
    Graph.validate_edge!(edge, order) if @safe_mode
    
    @adj_matrix[edge[0]][edge[1]]
  end
  
  # The number of edges attached to the supplied vertex.
  def degree(vertex)
    Graph.validate_vertex!(vertex, order) if @safe_mode
    
    histogram[vertex]
  end
  
  # An array of the number of edges attached to every vertex from 0 to order.
  def histogram
    @histogram ||= (0...order).to_a.collect{|vert| @adj_matrix[vert].nitems}
  end
  
  # Removes the vertex from the graph, returning a new graph without that 
  # vertex and the edges connected to that vertex.
  def remove_vertex(vertex)
    Graph.validate_vertex!(vertex, order) if @safe_mode
    
    new_order = order - 1
    new_edges = []
    
    edges.each do |from, to|
      next if from == vertex || to == vertex
      
      new_from = from > vertex ? from - 1 : from
      new_to   = to   > vertex ? to   - 1 : to
      
      new_edges << [new_from, new_to]
    end
    
    Graph.new(name, 
              new_order, 
              new_edges, 
              false)
  end
  
  # Removes all of the vertices from the graph, returning a new graph
  # without those vertices and edges attached to those vertices.
  def remove_vertices(*vertices)
    # Note: This can be optimized to remove multiple vertices in one go,
    #       instead of removing one vertex at a time
    vertices.sort.reverse.inject(self) do |new_graph, vertex| 
      new_graph.remove_vertex(vertex)
    end
  end
  
  # Removes all of the edges from the graph, returning a new graph
  # without those edges.
  def remove_edges(*edges_to_remove)
    if @safe_mode
      unless edges_to_remove.all?{|edge| has_edge?(edge)}
        raise "Cannot remove an edge that does not exist."
      end
      
      edges_to_remove = Graph.canonicalize_edges(edges_to_remove)
    end
    
    Graph.new(name,
              order,
              edges - edges_to_remove,
              false)
  end
  
  # Contracts the given edge, returning a new graph with that edge contracted.
  # Contracting an edge [0,1] means to bring the vertex 1 on to 0, so edges
  # that were previously connected to 1 are now connected to 0, and the
  # vertex 1 no longer exists.
  def contract_edge(edge)
    if @safe_mode
      unless has_edge?(edge)
        raise "Cannot contract an edge which does not exist."
      end
      
      edge = Graph.canonicalized_edge(edge)
    end
    
    new_order = order - 1
    new_edges = []
    
    edges.each do |from, to|
      new_from = if from == edge[1]
                   edge[0]
                 elsif from > edge[1]
                   from - 1
                 else
                   from
                 end

      new_to   = if to == edge[1]
                   edge[0]
                 elsif to > edge[1]
                   to - 1
                 else
                   to
                 end

      # Do not allow loops
      new_edges << [new_from, new_to] if new_from != new_to
    end
    
    Graph.new(name, 
              new_order, 
              Graph.canonicalize_edges(new_edges), 
              false)
  end

  # Determines if two graphs, no matter the vertex labelling, are identical.
  def isomorphic_to?(graph)
    return false unless order          == graph.order
    return false unless size           == graph.size
    return false unless histogram.sort == graph.histogram.sort
    
    to_canonical == graph.to_canonical
  end
  
  # Determines if the our graph contains as a minor, the supplied graph.
  # Optionally can allow two graphs that are isomorphic to be considered 
  # minors.
  def contains_minor?(graph, check_isomorphic=true)
    graph.minor_of?(self, check_isomorphic)
  end
  
  # Determines if our graph is a minor of the supplied graph.  Optionally
  # can allow two graphs that are isomorphic to be considered minors.
  def minor_of?(graph, check_isomorphic=true)
    return false if order > graph.order
    return false if size  > graph.size
    return true  if subgraph_of?(graph, check_isomorphic)
    
    # Contracting an edge will result in one less vertex and at least
    # one less edge
    if order < graph.order && size < graph.size
      graph.edges.each do |edge|
        new_graph = graph.contract_edge(edge)
        
        return true if minor_of?(new_graph)
      end
    end
    
    false
  end 

  # Determines if our graph is a subgraph of the supplied graph.  Optionally
  # can allow two graphs that are isomorphic to be considered subgraphs.
  def subgraph_of?(graph, check_isomorphic=true)
    return false if order > graph.order
    return false if size  > graph.size
    
    if order < graph.order
      # Get the number of vertices equivalent in both graphs, then check
      remove_count = graph.order - order
      
      each_combination(graph.order, remove_count) do |vertices_to_remove|
        # Note: Could be optimized by determining how many edges would be
        #       removed by removing these vertices, and checking that this
        #       number is at least as many as edges in self.size
        new_graph = graph.remove_vertices(*vertices_to_remove)
        
        return true if subgraph_of?(new_graph)
      end
    elsif size < graph.size
      # Get the number of edges equivalent in both graphs, then check
      remove_count = graph.size - size
      
      each_combination(graph.size, remove_count) do |edge_indexes_to_remove|
        edges_to_remove = graph.edges.values_at(*edge_indexes_to_remove)
        new_graph       = graph.remove_edges(*edges_to_remove)
        
        return true if subgraph_of?(new_graph)
      end
    elsif check_isomorphic
      return isomorphic_to?(graph)
    end
    
    false
  end
  
  # Determines if the given graph is planar.  Planar means that the graph 
  # could be arranged in a plane with no edges crossing over each other.
  def planar? 
    # Note: I imagine there is more pruning that could be added in here if
    #       it is necessary.  This hadn't proved to be particularly slow.
    return false if order >= 3 && size > (3 * order - 6)
    
    results = `echo \"#{to_dreadnaut}\" | dretog -q | planarg -qu 2>&1`
        
    return false if results =~ /0 graphs planar$/
    return true  if results =~ /1 graphs planar$/
    
    raise "ERROR: Unable to process planarg results '#{results}'."
  end
  
  # The dreadnaut form looks like this for the complete graph on 7 vertices:
  # n=7g1 2 3 4 5 6;2 3 4 5 6;3 4 5 6;4 5 6;5 6; 6.
  def to_dreadnaut
    return @dreadnaut if @dreadnaut

    buckets = Array.new(@order)
    edges.each do |edge|
      (buckets[edge[0]] ||= []) << edge[1]
    end
    
    edge_list = buckets.collect{|bucket| bucket.join(' ') if bucket}.join(';')
    
    @dreadnaut = "n=#{order}g#{edge_list}."
  end
  
  # This is a universal, simplistic labeling for a graph that no matter
  # the labelling of vertices, two graphs that are isomorphic will have the
  # same canonical label.
  def to_canonical
    @canonical ||= `echo \"#{to_dreadnaut}\" | dretog -q | labelg -q`
  end 
  
  # This is the format that can be interpreted by the graph_parser.
  def to_s
    "Graph #{name}, order #{order}.\n" +
    "#{order} #{size}\n"               +
    "#{edges.collect{|edge| edge.join(' ')}.join('  ')}\n"
  end
  
  # The 'opposite' of a graph.  Where there is an edge in a graph, its 
  # complement does not have an edge.  Likewise, where there is no edge in
  # a graph, the complement does have an edge.
  def complement
    Graph.new("#{name}_complement", 
              order, 
              Graph.complete_graph(order).edges - edges, 
              false)
  end
  
  # Creates a graph with all of the possible edges for a graph of the 
  # given order.
  def self.complete_graph(order)
    edges = []
    
    order.times do |from|
      ((from + 1)...order).each do |to|
        edges << [from, to]
      end
    end
    
    Graph.new("K#{order}", order, edges, false)    
  end

  # Utility methods
  
  # Make sure that the edges are uniq, sorted, and each edge is in the form
  # [0,1] and not [1,0].
  def self.canonicalize_edges(edges)
    edges.collect{|edge| Graph.canonicalized_edge(edge)}.uniq.sort
  end
  
  # Makes an edge in the form [0,1] and not [1,0]
  def self.canonicalized_edge(edge)
    edge[0] <= edge[1] ? edge : edge.reverse
  end
  
  # Confirms that for the given order, that all of the edges are valid.
  def self.validate_edges!(edges, order)
    raise "Edges cannot be nil." unless edges
    
    edges.each{|edge| Graph.validate_edge!(edge, order)}
  end

  # Confirms that for the given order the edge is valid.
  def self.validate_edge!(edge, order)
    Graph.validate_vertex!(edge[0], order)
    Graph.validate_vertex!(edge[1], order)

    if edge[0] == edge[1]      
      raise "Loops are not allowed: #{edge[0]} != #{edge[1]}"
    end
  end
  
  # Confirms that for the given order the vertex is valid.
  def self.validate_vertex!(vertex, order)
    unless vertex >= 0 && vertex < order
      raise "Vertex is not in the valid range: 0 <= #{vertex} < #{order}"
    end
  end
     
  private
    # Utility method to execute a block for each possible combination of 
    # selection_size elements chosen from total_size elements.  The block
    # will be passed the elements (0 based) in the current combination.
    def each_combination(total_size, selection_size, &block)
      set        = Array.new(selection_size)
      next_index = 0
      last_value = -1

      while next_index >= 0 && next_index < selection_size
        if last_value < (total_size - 1)
          set[next_index]  = last_value + 1
          last_value      += 1
          next_index      += 1
        else
          last_value  = set[next_index - 1]
          next_index -= 1
        end

        if next_index == selection_size
          yield set
          last_value  = set[next_index - 1]
          next_index -= 1
        end
      end
    end
end
