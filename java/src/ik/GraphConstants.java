package ik;

/**
 * These are the graph constants that are used in various classification
 * tests.
 *
 * Note: There are more graphs which could be added here
 *         Foisy identified H, G15, H15, J14, J'14
 *         There are also more Triangle-Y exchanges on > 9 vertices
 */
public class GraphConstants
{
  public static final Graph K5;
  public static final Graph K7;
  public static final Graph K33;
  public static final Graph K3311;
  public static final Graph H8;
  public static final Graph H9;
  public static final Graph F9;
  public static final Graph A9;
  public static final Graph B9;
  
  static {
    // Create K5
    K5 = new Graph("K5", 5);
    for(int i = 0; i < 5; i++) {
      for(int j = i+1; j < 5; j++) {
        K5.addEdge(i,j);
      }
    }
    
    // Create K7
    K7 = new Graph("K7", 7);
    for(int i = 0; i < 7; i++) {
      for(int j = i+1; j < 7; j++) {
        K7.addEdge(i,j);
      }
    }
    
    // Create K33
    K33 = new Graph("K33", 6);
    K33.addEdge(0,3);
    K33.addEdge(0,4);
    K33.addEdge(0,5);
    K33.addEdge(1,3);
    K33.addEdge(1,4);
    K33.addEdge(1,5);
    K33.addEdge(2,3);
    K33.addEdge(2,4);
    K33.addEdge(2,5);
    
    // Create K3311
    K3311 = new Graph("K3311", 8);
    K3311.addEdge(0,1);
    K3311.addEdge(0,3);
    K3311.addEdge(0,4);
    K3311.addEdge(0,5);
    K3311.addEdge(0,7);
    K3311.addEdge(1,2);
    K3311.addEdge(1,3);
    K3311.addEdge(1,4);
    K3311.addEdge(1,6);
    K3311.addEdge(2,3);
    K3311.addEdge(2,4);
    K3311.addEdge(2,5);
    K3311.addEdge(2,7);
    K3311.addEdge(3,4);
    K3311.addEdge(3,5);
    K3311.addEdge(3,6);
    K3311.addEdge(3,7);
    K3311.addEdge(4,5);
    K3311.addEdge(4,6);
    K3311.addEdge(4,7);
    K3311.addEdge(5,6);
    K3311.addEdge(6,7);
    
    // Create H8
    H8 = new Graph("H8", 8);
    H8.addEdge(0,1);
    H8.addEdge(0,2);
    H8.addEdge(0,4);
    H8.addEdge(0,5);
    H8.addEdge(0,6);
    H8.addEdge(0,7);
    H8.addEdge(1,2);
    H8.addEdge(1,3);
    H8.addEdge(1,5);
    H8.addEdge(1,7);
    H8.addEdge(2,4);
    H8.addEdge(2,5);
    H8.addEdge(2,6);
    H8.addEdge(2,7);
    H8.addEdge(3,4);
    H8.addEdge(3,6);
    H8.addEdge(4,5);
    H8.addEdge(4,7);
    H8.addEdge(5,6);
    H8.addEdge(5,7);
    H8.addEdge(6,7);
    
    // Create H9
    H9 = new Graph("H9", 9);
    H9.addEdge(0,1);
    H9.addEdge(0,2);
    H9.addEdge(0,4);
    H9.addEdge(0,5);
    H9.addEdge(0,7);
    H9.addEdge(0,8);
    H9.addEdge(1,2);
    H9.addEdge(1,3);
    H9.addEdge(1,5);
    H9.addEdge(1,8);
    H9.addEdge(2,4);
    H9.addEdge(2,6);
    H9.addEdge(2,7);
    H9.addEdge(3,4);
    H9.addEdge(3,7);
    H9.addEdge(4,5);
    H9.addEdge(4,8);
    H9.addEdge(5,6);
    H9.addEdge(5,7);
    H9.addEdge(6,8);
    H9.addEdge(7,8);
    
    // Create F9
    F9 = new Graph("F9", 9);
    F9.addEdge(0,1);
    F9.addEdge(0,2);
    F9.addEdge(0,4);
    F9.addEdge(0,5);
    F9.addEdge(0,6);
    F9.addEdge(0,8);
    F9.addEdge(1,2);
    F9.addEdge(1,3);
    F9.addEdge(1,5);
    F9.addEdge(1,8);
    F9.addEdge(2,4);
    F9.addEdge(2,5);
    F9.addEdge(2,6);
    F9.addEdge(2,8);
    F9.addEdge(3,4);
    F9.addEdge(3,6);
    F9.addEdge(4,5);
    F9.addEdge(4,8);
    F9.addEdge(5,7);
    F9.addEdge(6,7);
    F9.addEdge(7,8);
    
    // Create A9
    A9 = new Graph("A9", 9);
    A9.addEdge(0,1);
    A9.addEdge(0,3);
    A9.addEdge(0,5);
    A9.addEdge(0,8);
    A9.addEdge(1,2);
    A9.addEdge(1,4);
    A9.addEdge(1,5);
    A9.addEdge(1,7);
    A9.addEdge(2,4);
    A9.addEdge(2,5);
    A9.addEdge(2,6);
    A9.addEdge(2,8);
    A9.addEdge(3,4);
    A9.addEdge(3,6);
    A9.addEdge(4,5);
    A9.addEdge(4,7);
    A9.addEdge(4,8);
    A9.addEdge(5,6);
    A9.addEdge(5,7);
    A9.addEdge(5,8);
    A9.addEdge(6,7);
    A9.addEdge(7,8);
    
    // Create B9
    B9 = new Graph("B9", 9);
    B9.addEdge(0,1);
    B9.addEdge(0,3);
    B9.addEdge(0,4);
    B9.addEdge(0,6);
    B9.addEdge(0,8);
    B9.addEdge(1,2);
    B9.addEdge(1,3);
    B9.addEdge(1,4);
    B9.addEdge(1,5);
    B9.addEdge(2,3);
    B9.addEdge(2,4);
    B9.addEdge(2,6);
    B9.addEdge(2,8);
    B9.addEdge(3,6);
    B9.addEdge(3,7);
    B9.addEdge(3,8);
    B9.addEdge(4,6);
    B9.addEdge(4,7);
    B9.addEdge(4,8);
    B9.addEdge(5,6);
    B9.addEdge(5,7);
    B9.addEdge(5,8);
  }
}