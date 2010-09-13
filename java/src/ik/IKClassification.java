package ik;

/**
 * This is the interface which represents a classification test
 * for determining if a graph is intrinsically knotted or not.
 */
public interface IKClassification {
    public static final String IS_IK               = "ik";
    public static final String IS_NOT_IK           = "not_ik";
    public static final String CANNOT_DETERMINE_IK = "indeterminate";
    
    /**
     * This method will classify the given graph into one of three states--
     * ik, not ik, or indeterminate.
     *
     * @param  graph The graph to be classified
     * @return       A String result which is one of the constants IS_IK
     *               IS_NOT_IK or CANNOT_DETERMINE_IK
     */
    public String classify(Graph graph);
    
    /**
     * The name of the classification test.
     * 
     * @return A String name.
     */
    public String getName();
    
    /** 
     * A description of the classification test.
     *
     * @return A String description.
     */
    public String getDescription(); 
}
