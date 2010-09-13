# It was proven by Kohara and Suzuki that a graph with six or
# fewer vertices is not intrinsically knotted.  This classification test
# encodes that logic.
class OrderClassification < IKClassification::Base
  def classify(graph)
    graph.order <= 6 ? IS_NOT_IK : CANNOT_DETERMINE_IK
  end
  
  def description
    "If there are 6 or less vertices then the graph is " +
    "NOT intrinsically knotted."
  end
end