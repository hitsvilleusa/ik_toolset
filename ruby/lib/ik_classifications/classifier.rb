# This class will actually perform the classification efforts. It will
# use all of the classification tests and apply them one at a time to a 
# given graph until a non-indeterminate state can be determined for the graph.

require 'graph'                                                                                                            

module IKClassification
  class Classifier
    CLASSIFICATIONS = [NullClassification.new,
                       OrderClassification.new, 
                       AbsoluteSizeClassification.new, 
                       RelativeSizeClassification.new, 
                       PlanarityClassification.new,
                       ContainsMinorClassification.new(Graph::K7),
                       ContainsMinorClassification.new(Graph::H8),
                       ContainsMinorClassification.new(Graph::H9),
                       ContainsMinorClassification.new(Graph::F9),
                       ContainsMinorClassification.new(Graph::K3311),
                       ContainsMinorClassification.new(Graph::A9),
                       ContainsMinorClassification.new(Graph::B9),
                       MinorOfClassification.new(Graph::K7),
                       MinorOfClassification.new(Graph::H8),
                       MinorOfClassification.new(Graph::H9),
                       MinorOfClassification.new(Graph::F9),
                       MinorOfClassification.new(Graph::K3311),
                       MinorOfClassification.new(Graph::A9),
                       MinorOfClassification.new(Graph::B9)]
    
    # Classify the given graph with respect to the property of 
    # intrinsic knotting.  Returns an array of the results in the form:
    # [classification result, classification test name, execution time]
    def self.classify(graph)
      start_time                 = Time.now
      result                     = nil
      determining_classification = nil
      
      CLASSIFICATIONS.each do |classification|
        result = classification.classify(graph)
        
        if result != Base::CANNOT_DETERMINE_IK
          determining_classification = classification
          break
        end
      end
      
      end_time = Time.now

      [result, determining_classification, end_time - start_time]
    end
  end
end
