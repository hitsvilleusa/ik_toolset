# A test that can rule out some graphs from being IK.  This is based
# on Robertson, Seymour, and Thomas' work.
class AbsoluteSizeClassification < IKClassification::Base
  def classify(graph)
    graph.size < 15 ? IS_NOT_IK : CANNOT_DETERMINE_IK
  end
  
  def description
    "If there are less than 15 edges then the graph is " +
    "NOT intrinsically knotted."
  end
end
