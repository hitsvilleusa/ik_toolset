package ik;

import static ik.GraphConstants.K33;
import static ik.GraphConstants.K5;

/**
 * It was proven by Blain, Bowlin, Fleming et al. that if a graph is formed 
 * from a planar graph plus two vertices, then the graph is not 
 * intrinsically knotted.  This classification tests this logic.
 */
public class PlanarityClassification implements IKClassification {
  public String classify(Graph graph) {
    // Remove each possible pair of vertices and see if the remaining graph
    // is planar, by determining if it does not have a K5 or K33 minor  
    for (int from = 0; from < graph.getOrder(); from++) {
      for (int to = from+1; to < graph.getOrder(); to++) {
        int[] vertices = {from,to};
        Graph subGraph = graph.removeVertices(vertices);
        
        if (!((subGraph.getOrder() >= 3) && 
              (subGraph.getSize() > (3*subGraph.getOrder() - 6))) && 
            !(subGraph.containsMinor(K5))                         && 
            !(subGraph.containsMinor(K33))) {
          return IS_NOT_IK;
        }
      }
    }
    
    return CANNOT_DETERMINE_IK;
  }
  
  public String getName() {
    return "PlanarityClassification";  
  }
  
  public String getDescription() {
    return "Any graph that has a planar subgraph after removing any 2 " +
           "vertices is NOT intrinsically knotted.";
  }
}