# A simple test to make sure the graph is truly a Graph object and not nil.
class NullClassification < IKClassification::Base
  def classify(graph)
    graph ? CANNOT_DETERMINE_IK : IS_NOT_IK
  end
  
  def description
    "If the graph is nil, then it is NOT intrinsically knotted.  This " +
    "test is just to rule out any invalid graphs."
  end
end
