package ik;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static ik.GraphConstants.*;

/**
 * This class will actually perform the classification efforts. It will
 * use all of the classification tests and apply them one at a time to a 
 * given graph until a non-indeterminate state can be determined for the graph.
 */
public class KnotFinder
{
  private static final Pattern            DIGIT_REGEX         =
                         Pattern.compile("\\d+");
  private static final SimpleDateFormat   DATE_FORMAT         =
                         new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
  public  static final IKClassification[] IK_CLASSIFICATIONS  =
                         {new NullClassification(),
                          new OrderClassification(),
                          new AbsoluteSizeClassification(),
                          new RelativeSizeClassification(),
                          new PlanarityClassification(),
                          new ContainsMinorClassification(K7),
                          new ContainsMinorClassification(H8),
                          new ContainsMinorClassification(H9),
                          new ContainsMinorClassification(F9),
                          new ContainsMinorClassification(K3311),
                          new ContainsMinorClassification(A9),
                          new ContainsMinorClassification(B9),
                          new MinorOfClassification(K7),
                          new MinorOfClassification(H8),
                          new MinorOfClassification(H9),
                          new MinorOfClassification(F9),
                          new MinorOfClassification(K3311),
                          new MinorOfClassification(A9),
                          new MinorOfClassification(B9)};

  private static BufferedReader brGraphs = null;
  private static BufferedWriter bwOut    = null;
  private static String         command  = null;

  /** 
   * The main method which drives the classification attempt on the graphs.
   */
  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.out.println("usage: java KnotFinder <graph file> [output file]");
      System.exit(0);
    }

    String infile  = args[0];
    String outfile = args.length == 2 ? args[1] : null;

    // Recreate what the command looked like
    command = "java -jar knotfinder.jar "+infile;

    if (outfile != null) {
      command += " " + outfile;
    }

    initializeFiles(infile, outfile);
    printHeader();

    Graph currentGraph = nextGraph();

    // Iterate over each graph
    while (currentGraph != null) {
      String           result      = IKClassification.CANNOT_DETERMINE_IK;
      int              testIndex   = 0;
      IKClassification currentTest = null;
      long             startTime   = new Date().getTime();

      // Try each classification test until we find a non indeterminate
      // result
      while ((testIndex < IK_CLASSIFICATIONS.length) &&
             (result == IKClassification.CANNOT_DETERMINE_IK)) {
        currentTest = (IKClassification)IK_CLASSIFICATIONS[testIndex];
        result      = currentTest.classify(currentGraph);
        testIndex++;
      }

      long   endTime = new Date().getTime();
      double seconds = (endTime - startTime) / 1000.0;
      logResult(currentGraph, result, currentTest, seconds);

      currentGraph = nextGraph();
    }

    closeFiles();
  }

  /** 
   * Print the result of the classification attempt to the output stream.
   *
   * @param graph The graph we are working with.
   * @param result The result of the classification.
   * @param lastTest The test which determined that result.
   * @param seconds The number of seconds to perform the classification.
   * @throws IOException if there is an IO error.
   */
  private static void logResult(Graph            graph,
                                String           result,
                                IKClassification lastTest,
                                double           seconds) throws IOException {
    String resultString = null;

    if (result == IKClassification.CANNOT_DETERMINE_IK) {
      resultString = graph.getName() + ", "   +
                     result          + ", , " +
                     seconds;
    } else {
      resultString = graph.getName()    + ", " +
                     result             + ", " +
                     lastTest.getName() + ", " +
                     seconds;
    }

    bwOut.write(resultString);
    bwOut.newLine();
    bwOut.flush();
  }

  /**
   * Reads the next graph from the input file.
   *
   * @return The new Graph that is read, null if the end has been reached.
   * @throws IOException if there is an IO issue.
   * @throws IllegalArgumentException if there is a problem with the
   *         graph data.
   */
  private static Graph nextGraph() throws IOException, 
                                          IllegalArgumentException {
    Graph graph = null;

    // Remove any blank lines
    String nextLine = brGraphs.readLine();

    while (nextLine != null && nextLine.trim().length() == 0) {
      nextLine = brGraphs.readLine();
    }

    // Parse the next graph
    if (nextLine != null) {
      String titleLine = nextLine;
      String descLine  = brGraphs.readLine();
      String edges     = brGraphs.readLine();

      // Are there multiple lines of edges?
      nextLine = brGraphs.readLine();
      while (nextLine != null && nextLine.trim().length() != 0) {
        edges   += " "+nextLine;
        nextLine = brGraphs.readLine();
      }

      // The title line looks like "Graph 3, order 8." so pull
      // out the numbers
      Matcher match = DIGIT_REGEX.matcher(titleLine);
      match.find();
      String name  = match.group();
      match.find();
      int order = Integer.parseInt(match.group());

      graph = new Graph(name, order);
      addAllEdges(graph, edges);
    }

    return graph;
  }

  /**
   * Adds all of the edges to the graph object from the 'edges' String
   * which was read from a file.
   *
   * @param graph The current graph we are working with.
   * @param edges The String line from the file which represents the edges.
   */
  private static void addAllEdges(Graph graph, String edges) {
    StringTokenizer stEdges = new StringTokenizer(edges);

    while (stEdges.hasMoreElements()) {
      int fromVert = Integer.parseInt(stEdges.nextToken());
      int toVert   = Integer.parseInt(stEdges.nextToken());
      graph.addEdge(fromVert, toVert);
    }
  }

  /**
   * Opens the input and output files.  If an output file isn't supplied,
   * then output goes to stdout.
   *
   * @param graphFilePath The filepath where the graphs will be read from.
   * @param outputFilePath The filepath for the output file (can be null).
   * @throws IOException if there is an IO issue.
   */
  private static void initializeFiles(String graphFilePath,
                                      String outputFilePath)
                                     throws IOException {
    brGraphs = new BufferedReader(new FileReader(graphFilePath));

    if (outputFilePath == null) {
      bwOut = new BufferedWriter(new PrintWriter(System.out));
    } else {
      bwOut = new BufferedWriter(new FileWriter(outputFilePath));
    }
  }

  /**
   * Closes the input and output files.
   */
  private static void closeFiles() throws IOException {
    bwOut.write("\n"+DATE_FORMAT.format(new Date())+"\n");

    brGraphs.close();
    bwOut.close();
  }

  /**
   * Prints the first line of the output
   */
  private static void printHeader() throws IOException {
    bwOut.write(DATE_FORMAT.format(new Date())+"\n");
    bwOut.write(command+"\n\n");
  }
}
