package ik;

/**
 * It was proven by Kohara and Suzuki that a graph with six or
 * fewer vertices is not intrinsically knotted.  This classification test
 * encodes that logic.
 */
public class OrderClassification implements IKClassification {
  public String classify(Graph graph) {    
    return (graph.getOrder() < 7) ? IS_NOT_IK : CANNOT_DETERMINE_IK;
  }
     
  public String getName() {
    return "OrderClassification";  
  }
  
  public String getDescription() {
    return "If there are 6 or less vertices then the graph is " +
           "NOT intrinsically knotted.";
  }
}