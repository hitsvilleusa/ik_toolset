package ik;

/**
 * If a graph is a proper minor (not isomorphic) to a graph that is minor
 * minimal with respect to the property of intrinsic knotting, then by 
 * the definition of minor minimal, any graph that is a minor of that graph
 * is known to NOT exhibit the same property and thus is not intrinsically
 * knotted.  This classification test uses this logic to determine if a graph
 * is not IK.
 */
public class MinorOfClassification implements IKClassification { 
  private Graph minorMinimalIKGraph;
  
  public MinorOfClassification(Graph minorMinimalIKGraph) {
    this.minorMinimalIKGraph = minorMinimalIKGraph;
  }
  
  public String classify(Graph subgraph) {
    String result = CANNOT_DETERMINE_IK;
    
    if (!minorMinimalIKGraph.isIsomorphicTo(subgraph) &&
         minorMinimalIKGraph.containsMinor(subgraph)) {
      result = IS_NOT_IK;
    }

    return result;
  }
  
  public String getName() {
    return "MinorOf"+minorMinimalIKGraph.getName()+"Classification";
  }
  
  public String getDescription() {
    return "Any graph that is a minor (excluding isomorphisms) of the "    +
           "known minor minimal IK graph " + minorMinimalIKGraph.getName() +
           " is NOT intrinsically knotted.";
  }
}