package ik;

/** 
 * A test that can rule out some graphs from being IK.  This is based
 * on Robertson, Seymour, and Thomas’ work.
 */
public class AbsoluteSizeClassification implements IKClassification {
  public String classify(Graph graph) {  
    return (graph.getSize() < 15) ? IS_NOT_IK : CANNOT_DETERMINE_IK;
  }

  public String getName() {
    return "AbsoluteSizeClassification";  
  }
  
  public String getDescription() {
    return "If there are less than 15 edges then the graph is " +
           "NOT intrinsically knotted.";
  }
}