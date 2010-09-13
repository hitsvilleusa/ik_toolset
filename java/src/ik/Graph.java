package ik;

import java.util.*;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 * This class is the heart of the project to determine intrinsically
 * knotted graphs.  It is objects of this Graph class that represents the
 * graphs that we are testing.  The classification tests are performed on
 * these objects.
 */
public class Graph {
  private String      name;
  private int         order;
  private boolean[][] data;

  /**
   * Creates a new Graph object.
   *
   * @param name A String name for the graph
   * @param order An integer number of vertices for the graph
   */
  public Graph(String name, int order) {
    this.name  = name;
    this.order = order;
    this.data  = new boolean[order][order];
  }

  /**
   * The name of the graph.
   *
   * @return A String name for the graph.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the number of vertices in the graph.
   *
   * @return An integer representing the number of vertices in the graph.
   */
  public int getOrder() {
    return order;
  }

  /**
   * Returns the number of edges in the graph.
   *
   * @return An integer representing the number of edges in the graph.
   */
  public int getSize() {
    return edges().size();
  }

  /**
   * Adds the given edge to the Graph.
   *
   * @param fromVertex The from vertex for the edge
   * @param toVertex The to vertex for the edge
   */
  public void addEdge(int fromVertex, int toVertex) {
    setEdge(fromVertex, toVertex, true);
  }

  /**
   * Determines whether or not this graph includes the provided edge.
   *
   * @param fromVertex The from vertex for the edge
   * @param toVertex The to vertex for the edge
   * @return A boolean value indicating whether or not this edge is a part
   *         of the graph.
   */
  public boolean hasEdge(int fromVertex, int toVertex) {
    return getEdge(fromVertex, toVertex);
  }

  /**
   *  The list of edges in this graph.
   *
   * @return A List of edges that are a part of this graph.
   */
  public List<int[]> edges() {
    List<int[]> edges = new ArrayList<int[]>(maxSize());

    for (int from = 0; from < getOrder(); from++) {
      for (int to = from + 1; to < getOrder(); to++) {
        if (hasEdge(from, to)) {
          int[] edge = {from, to};
          edges.add(edge);
        }
      }
    }

    return edges;
  }

  /**
   * Returns a list of vertices that are connected via an edge to the
   * provided vertex.
   *
   * @param fromVertex The vertex we are investigating.
   * @return A List of integers which represent edges from the fromVertex
   *         provided to the vertices returned.
   */
  public List<Integer> connectedVertices(int fromVertex) {
    List<Integer> connectedVertices = new ArrayList<Integer>(getOrder());

    for (int toVertex = 0; toVertex < getOrder(); toVertex++) {
      if (fromVertex != toVertex && hasEdge(fromVertex, toVertex)) {
        connectedVertices.add(new Integer(toVertex));
      }
    }

    return connectedVertices;
  }

  /**
   * Create a new graph with the supplied edge contracted.
   *
   * @param fromVertex The from vertex for the edge.
   * @param toVertex The to vertex for the edge.
   * @return A new Graph with the provided edge removed.
   */
  public Graph contractEdge(int fromVertex, int toVertex) {
    Graph minor = null;

    // Cannot contract an edge if we don't have one
    if (hasEdge(fromVertex, toVertex)) {
      minor = new Graph(getName(), getOrder() - 1);
      int      maxVertex    = fromVertex > toVertex ? fromVertex : toVertex;
      int      minVertex    = fromVertex > toVertex ? toVertex   : fromVertex;
      Iterator edgeIterator = edges().iterator();

      while (edgeIterator.hasNext()) {
        int[] edge    = (int[])edgeIterator.next();
        int   newFrom = -1;
        int   newTo   = -1;

        // The vertex labels for the edge may have changed as a result of
        // one less vertex
        if (edge[0] == maxVertex) {
          newFrom = minVertex;
        } else if (edge[0] > maxVertex) {
          newFrom = edge[0] - 1;
        } else {
          newFrom = edge[0];
        }

        if (edge[1] == maxVertex) {
          newTo = minVertex;
        } else if (edge[1] > maxVertex) {
          newTo = edge[1] - 1;
        } else {
          newTo = edge[1];
        }

        if (newFrom != newTo) {
          minor.addEdge(newFrom, newTo);
        }
      }
    }

    return minor;
  }

  /**
   * Create a new graph with the vertices provided removed.
   *
   * @param vertices An array of vertices to remove from our graph
   * @return A new Graph without the vertices provided
   */
  public Graph removeVertices(int[] vertices) {
    Graph subGraph = new Graph(getName(), getOrder() - vertices.length);

    Iterator edgeIterator = edges().iterator();

    while (edgeIterator.hasNext()) {
      int[] edge = (int[])edgeIterator.next();
      int   from = edge[0];
      int   to   = edge[1];

      // If either of the points is in the vertices array, then the point
      // is thrown out
      boolean disregardEdge = false;
      for (int i = 0; i < vertices.length; i++) {
        if (from == vertices[i] || to == vertices[i]) {
          disregardEdge = true;
        }
      }

      if (!disregardEdge) {
        int origFrom = from;
        int origTo   = to;

        // The vertex labels for the edge may have changed as a result of
        // less vertices
        for (int i = 0; i < vertices.length; i++) {
          if (origFrom > vertices[i]) {
            from--;
          }
          if (origTo > vertices[i]) {
            to--;
          }
        }

        subGraph.addEdge(from, to);
      }
    }

    return subGraph;
  }

  /**
   * Determine if our graph contains as a minor the provided graph.
   *
   * @param minor The Graph we are comparing to our graph.
   * @return A boolean value indicating whether or not our graph
   *         contains as a minor the provided graph.
   */
  public boolean containsMinor(Graph minor) {
    if (getOrder() < minor.getOrder()) { return false; }
    if (getSize()  < minor.getSize())  { return false; }
    if (containsSubgraph(minor))       { return true;  }

    // Contracting an edge will result in one less edge and one less vertex
    // We check to see if we can afford to lose one edge and one vertex
    if (getSize() <= minor.getSize() || getOrder() <= minor.getOrder()) {
      return false;
    }

    Iterator edgeIterator = edges().iterator();

    // Try contracting each edge, then check again
    while(edgeIterator.hasNext()) {
      int[] edge = (int[])edgeIterator.next();

      Graph newMinor = contractEdge(edge[0], edge[1]);

      if (newMinor.containsMinor(minor)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Determine if our graph contains the provided graph as a subgraph.
   *
   * @param subGraph The graph we are testing against our own.
   * @return A boolean value indicating whether or not our graph
   *         contains the provided graph as a subgraph.
   */
  public boolean containsSubgraph(Graph subGraph) {
    boolean containsSubgraph = false;

    // Has to have at least as many edges and vertices as the subgraph
    // in order to even be considered
    if (getSize()  >= subGraph.getSize() &&
        getOrder() >= subGraph.getOrder()) {
      // Initialize an empty vertexMap from our graph to the subgraph
      int[] vertexMap = new int[subGraph.order];
      for (int i = 0; i < vertexMap.length; i++) {
        vertexMap[i] = -1;
      }

      // Do the work
      containsSubgraph = searchForSubgraph(subGraph, vertexMap, 0);
    }

    return containsSubgraph;
  }

  /**
   * Determines if two graphs are isomorphic to each other.
   *
   * @param graph The graph to check to see if we are isomorphic to.
   * @return A boolean value indicating whether or not we are isomorphic.
   */
  public boolean isIsomorphicTo(Graph graph) {
    return containsSubgraph(graph)       &&
           getSize()  == graph.getSize() &&
           getOrder() == graph.getOrder();
  }

  /**
   * Does the recursive work of trying to determine if a mapping from our
   * graph to the subgraph can be found.
   *
   * @param subGraph The Graph that we are trying to determine if we are a
   *                 subgraph of
   * @param vertexMap The current mapping from our vertices to those of the
   *                  subgraph.
   * @param nextIndex The index we will be working with next.
   * @return A boolean value indicating if the mapping 'so far' is a complete
   *         mapping to the subgraph.
   */
  private boolean searchForSubgraph(Graph subGraph,
                                    int[] vertexMap,
                                    int   nextIndex) {
    // We first need to verify our current map state and make sure that it
    // is a 'possible' solution for what we know so far
    if (!verifyPartialMapping(subGraph, vertexMap, nextIndex - 1)) {
      return false;
    }

    // If we are trying to work on an index that is past our length of the
    // the vertexMap, that means that we have a valid mapping
    if (vertexMap.length == nextIndex) {
      return true;
    }

    // Walk through each vertex
    for (int currentIndex = 0; currentIndex < this.order; currentIndex++) {
      if (!containsVertex(vertexMap, currentIndex)) {
        // Assume this vertex is correct, and then test it
        vertexMap[nextIndex] = currentIndex;
        if (searchForSubgraph(subGraph, vertexMap, nextIndex+1)) {
          return true;
        }
        else {
          // It wasn't correct, try the next
          vertexMap[nextIndex] = -1;
        }
      }
    }

    return false;
  }

  /**
   * Checks whether the current mapping that has been constructed for the
   * graph to a possible subgraph 'is possible'.  It just confirms the
   * in progress solution.
   *
   * @param subGraph The Graph that we are trying to determine if we are a
   *                 subgraph of
   * @param vertexMap The current mapping from our vertices to those of the
   *                  subgraph.
   * @param workingIndex The index we are working with currently.
   * @return A boolean value indicating if the mapping 'so far' is
   *         possible or not.
   */
  private boolean verifyPartialMapping(Graph subGraph,
                                       int[] vertexMap,
                                       int   workingIndex) {
    boolean successfulMapping = true;

    if (workingIndex >= 0 && workingIndex < vertexMap.length) {
      int      mappedFromVertex  = vertexMap[workingIndex];
      List     connectedVertices = subGraph.connectedVertices(workingIndex);
      Iterator verticesIterator  = connectedVertices.iterator();

      while (verticesIterator.hasNext() && successfulMapping) {
        int toVertex       = ((Integer)verticesIterator.next()).intValue();
        int mappedToVertex = vertexMap[toVertex];
        if (mappedToVertex >= 0) {
	      // If the edge exists in the subgraph but not in our graph via
	      // the proposed mapping, then we are done and this mapping doesn't
	      // work
          successfulMapping = this.hasEdge(mappedFromVertex, mappedToVertex);
        }
      }
    }

    return successfulMapping;
  }

  /**
   * Determines if the vertexMap contains the vertex provided.  It is
   * merely a lookup into the array for the value.
   *
   * @param vertexMap The current array of integer vertices.
   * @param vertex    The vertex to check.
   * @return A boolean indicating whether or not the vertex was found.
   */
  private boolean containsVertex(int[] vertexMap, int vertex) {
    boolean containsVertex = false;
    int     currentIndex   = 0;

    while ((currentIndex < vertexMap.length) && (!containsVertex)) {
      containsVertex = (vertexMap[currentIndex] == vertex);
      currentIndex++;
    }

    return containsVertex;
  }

  /**
   * Sets the value for an edge.
   *
   * @param fromVertex The starting vertex of the edge.
   * @param toVertex   The ending vertex of the edge.
   * @param value      The boolean value to set it to, where true indicates
   *                   the edge exists, false indicates that it does not.
   */
  private void setEdge(int fromVertex, int toVertex, boolean value) {
    validateEdge(fromVertex, toVertex);

    data[fromVertex][toVertex] = data[toVertex][fromVertex] = value;
  }

  /**
   * Returns the value set for the edge.
   *
   * @return A boolean result where true means there was an edge, and false
   *         means there was not.
   */
  private boolean getEdge(int fromVertex, int toVertex) {
    validateEdge(fromVertex, toVertex);

    return data[fromVertex][toVertex];
  }

  /**
   * Validates that the edge is legitimate.
   *
   * @throws IllegalArgumentException if it is invalid.
   */
  private void validateEdge(int fromVertex, int toVertex) {
    validateVertex(fromVertex);
    validateVertex(toVertex);

    if (fromVertex == toVertex)
      throw new IllegalArgumentException("Loops are not allowed: " +
                                         fromVertex+" == "+toVertex);
  }

  /**
   * Validates that the vertex is legitimate.
   *
   * @throws IllegalArgumentException if it is invalid.
   */
  private void validateVertex(int vertex) {
    if (vertex < 0 || vertex >= getOrder())
      throw new IllegalArgumentException("Vertex is not in the valid range:" +
                                         " 0 <= "+vertex+" < "+getOrder());
  }

  /**
   * Determines the maximum number of edges that are possible with the given
   * order.  This is used to know how bit to create Arrays.
   *
   * @return An integer representing the max possible size.
   */
  private int maxSize() {
    return (getOrder() * (getOrder() - 1)) / 2;
  }
}

