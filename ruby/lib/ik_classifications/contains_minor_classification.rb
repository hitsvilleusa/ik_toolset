# If a graph contains as a minor or is isomorphic to a known IK graph, then by
# the definition of a minor, the graph exhibits the intrinsic knotting
# property.  This classification uses this logic to determine if a graph
# is intrinsically knotted.
class ContainsMinorClassification < IKClassification::Base
  def initialize(ik_graph)
    @ik_graph = ik_graph
  end
  
  def classify(graph)
    graph.contains_minor?(@ik_graph) ? IS_IK : CANNOT_DETERMINE_IK
  end
  
  def name
    "ContainsMinor#{@ik_graph.name}Classification"
  end
  
  def description
    "Any graph that contains the known IK graph #{@ik_graph.name} " +
    "as a minor (including isomorphisms) is intrinsically knotted."
  end
end