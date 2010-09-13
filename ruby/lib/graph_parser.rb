# This class is responsible for parsing all of the graphs from files
# that are in the form:
#   Graph 6, order 4.
#   4 6
#   0 1  0 2  0 3  1 2  1 3  2 3
# It can also parse supplied edges into a graph.  Furthermore, it allows
# for arbitrary parsing of a file so that all graphs are not necessarily
# returned, only graphs indicated.  By default, all graphs in a file will
# be parsed, but it is possible to parse only some of the graphs in the 
# form '2:6 8 10:*'
require 'enumerator'
require 'graph'

class GraphParser
  # Creates a new GraphParser that is responsible for parsing each
  # graph.  When limiting the graphs to parse by 'id' it is assumed
  # that the names of the graphs are integer values.  In a normal use
  # of this class, either edges or a graph_filepath will be supplied for
  # 'what graphs' to use.  The graph_ids simply limits to certain
  # graphs within the file in the form (ex. ['2:6', '8', '10:*']).  If no
  # graph_ids are supplied, '*' will be the default.
  def initialize(edges, graph_filepath, graph_ids)
    if !edges.nil? && !edges.empty?
      order          = edges.flatten.max + 1
      @static_graphs = [Graph.new('0', order, edges)]
    else
      @graph_file = File.open(graph_filepath)
      @graph_ids  = graph_ids
    end
  end
  
  # Return the next graph that should be included in the set of requested
  # graphs.
  def next
    return @static_graphs.shift if @static_graphs
    
    begin
      new_graph = next_graph 
    end until new_graph.nil? || include_graph?(new_graph.name)
    
    new_graph
  end
  
  private
    # Determines if the current graph should be returned or not.  Does
    # it fall in the graph_ids to include.
    def include_graph?(graph_id)
      return true if @graph_ids.nil?
      return true if @graph_ids.empty?
      return true if @graph_ids.include?('*')
      return true if @graph_ids.include?('*:*')
      return true if @graph_ids.include?(graph_id)
      return true if @graph_ids.include?(graph_id.to_i)
      return true if @graph_ids.include?(graph_id.to_s)
      
      @graph_ids.select{|gid| gid =~ /:/}.each do |id_range|
        min_id, max_id = id_range.split(':')
        
        return true if min_id == '*' && (graph_id.to_i <= max_id.to_i)
        return true if max_id == '*' && (graph_id.to_i >= min_id.to_i)
        return true if (graph_id.to_i >= min_id.to_i) && 
                       (graph_id.to_i <= max_id.to_i)
      end
      
      false
    end
    
    # Returns the next graph from the file.
    def next_graph
      return nil unless lines = next_graph_lines

      id          = /^Graph (\d+), order (\d+).$/.match(lines[0])[1]
      order, size = /^(\d+) (\d+)$/.match(lines[1])[1,2].collect{|n| n.to_i}
      edges       = lines[2].split.collect{|v| v.to_i}.enum_slice(2).to_a
                          
      raise "Error parsing graph #{id}." unless edges.size == size
      
      Graph.validate_edges!(edges, order)

      Graph.new(id, 
                order, 
                Graph.canonicalize_edges(edges), 
                false)
    end

    # Gets the 3 lines from the file that represent the graph.
    def next_graph_lines
      return nil if @graph_file.closed?

      graph_lines = []
      4.times{ graph_lines << @graph_file.readline.strip } 
        
      @graph_file.close if @graph_file.eof?
      
      # First line is blank
      graph_lines[1..3]
    end
end