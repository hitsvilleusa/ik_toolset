package ik;

/** 
 * A simple test to make sure the graph is truly a Graph object and not nil.
 */
public class NullClassification implements IKClassification {
  public String classify(Graph graph) {    
    return (graph == null) ? IS_NOT_IK : CANNOT_DETERMINE_IK;
  }       
  
  public String getName() {
    return "NullClassification";  
  } 
  
  public String getDescription() {
    return "If the graph is null, then it is NOT intrinsically knotted.  " +
           "This test is just to rule out any invalid graphs.";
  }
}