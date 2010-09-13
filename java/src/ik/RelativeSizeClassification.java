package ik;

/**
 * Campbell, Mattman, Ottman et al. proved that if the number of edges in a 
 * graph is greater than or equal to 5 * the number of vertices - 14, then
 * the graph is intrinsically knotted because it contains a K7 minor.
 */
public class RelativeSizeClassification implements IKClassification {
  public String classify(Graph graph) {  
    if ((graph.getOrder() >= 7) && 
        (graph.getSize() >= (5 * graph.getOrder() - 14))) {
      return IS_IK;
    } else {
      return CANNOT_DETERMINE_IK;
    }
  }
  
  public String getName() {
    return "RelativeSizeClassification";  
  }
  
  public String getDescription() {
    return "If there are 5n-14 or more edges (where n is the order) then " +
           "the graph is intrinsically knotted.";
  }
}