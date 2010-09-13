# If a graph is a proper minor (not isomorphic) to a graph that is minor
# minimal with respect to the property of intrinsic knotting, then by 
# the definition of minor minimal, any graph that is a minor of that graph
# is known to NOT exhibit the same property and thus is not intrinsically
# knotted.  This classification test uses this logic to determine if a graph
# is not IK.
class MinorOfClassification < IKClassification::Base
  def initialize(minor_minimal_ik_graph)
    @minor_minimal_ik_graph = minor_minimal_ik_graph
  end
  
  def classify(graph)
    if graph.minor_of?(@minor_minimal_ik_graph, false)
      IS_NOT_IK
    else 
      CANNOT_DETERMINE_IK
    end
  end

  def name
    "MinorOf#{@minor_minimal_ik_graph.name}Classification"
  end
  
  def description
    "Any graph that is a minor (excluding isomorphisms) of the known " +
    "minor minimal IK graph #{@minor_minimal_ik_graph.name} is NOT "   +
    "intrinsically knotted."
  end
end