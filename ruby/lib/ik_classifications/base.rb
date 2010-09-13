# This is the base class which represents a classification test
# for determining if a graph is intrinsically knotted or not.
module IKClassification
  class Base
    IS_IK               = 'ik'
    IS_NOT_IK           = 'not_ik'
    CANNOT_DETERMINE_IK = 'indeterminate'

    # This method will classify the given graph into one of three states--
    # ik, not ik, or indeterminate
    def classify(graph)
      CANNOT_DETERMINE_IK
    end

    # The name of this classification test.
    def name
      self.class.name
    end     
  
    # A description for this classification test.
    def description
      "Classifies the graph."
    end     
  end
end
