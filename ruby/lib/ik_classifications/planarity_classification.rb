# It was proven by Blain, Bowlin, Fleming et al. that if a graph is formed 
# from a planar graph plus two vertices, then the graph is not 
# intrinsically knotted.  This classification tests this logic.
class PlanarityClassification < IKClassification::Base
  def classify(graph)
    each_possible_graph(graph) do |new_graph|
      return IS_NOT_IK if new_graph.planar?
    end

    CANNOT_DETERMINE_IK
  end
  
  def description
    "Any graph that has a planar subgraph after removing any 2 vertices is "+
    "NOT intrinsically knotted."
  end
  
  private
    # Remove each possible pair of vertices
    def each_possible_graph(graph, &block)
      (0...graph.order).each do |first|
        (first+1...graph.order).each do |second|
          yield graph.remove_vertices(first, second)
        end
      end
    end
end  
