# Campbell, Mattman, Ottman et al. proved that if the number of edges in a 
# graph is greater than or equal to 5 * the number of vertices - 14, then
# the graph is intrinsically knotted because it contains a K7 minor.
class RelativeSizeClassification < IKClassification::Base
  def classify(graph)
    if graph.order >= 7 && graph.size >= ((5 * graph.order) - 14)
      IS_IK
    else
      CANNOT_DETERMINE_IK
    end
  end
  
  def description
    "If there are 5n-14 or more edges (where n is the order) then the " +
    "graph is intrinsically knotted."
  end
end
