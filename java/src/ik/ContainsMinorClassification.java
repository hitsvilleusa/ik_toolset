package ik;

/**
 * If a graph contains as a minor or is isomorphic to a known IK graph, then
 * by the definition of a minor, the graph exhibits the intrinsic knotting
 * property.  This classification uses this logic to determine if a graph
 * is intrinsically knotted.
 */
public class ContainsMinorClassification implements IKClassification { 
  private Graph ikGraph;
  
  public ContainsMinorClassification(Graph ikGraph) {
    this.ikGraph = ikGraph;
  }
  
  public String classify(Graph graph) {
    String result = CANNOT_DETERMINE_IK;
    
    if (graph.containsMinor(ikGraph)) {
      result = IS_IK;      
    }

    return result;
  }
  
  public String getName() {
    return "ContainsMinor"+ikGraph.getName()+"Classification";
  }

  public String getDescription() {
    return "Any graph that contains the known IK graph " + ikGraph.getName() +
           " as a minor (including isomorphisms) is intrinsically knotted.";
  }
}