package com.uncc;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.jgrapht.io.*;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

/**
 * @Author: zerongliu
 * @Author: Shane Polefko
 * @Author: Zhang Zhang
 * @Date: 11/29/18 19:04
 * @Description: Description of Program:
 * The program is designed to take a given input .gml file that has the information
 * about a graph. The program read and parse the file into a graph. And provide a method that implements two
 * algorithms--dijkstra and bellmanford to find the shortest route from one vertex to another.
 * <p>
 * Key Functions:
 * generate gaph according to the input file.
 * print the graph.
 * run two algorithms according to the input command.
 * print the performance of an algorithm.
 * <p>
 * Compiler used: Eclipse, IntelliJ
 * Platform: Windows 10, Mac
 * Compiler: JDK 1.8
 * What works: For our scope, the program defines three main class, the Graph class, Edge class, and the Vertex class
 * We designed the program with good practices in mind, managed common exceptions that could occur, and
 * utilized variable naming in a way that easily tells a reader what the program is doing.
 * <p>
 * What fails:
 * Data Structure Description: Our program mostly utilized hashSet and Map. HashSet is used to store the
 * Vertex of the graph. HashMap is used to store the outgoing edge related to a
 * vertex. And we also use map to store distance from the original vertex to the target vertex, store the relationship
 * between the vertex and its previous vertex
 * note:in the path of the executable jar package, type the command "java -jar graph.jar" to run the program
 */
public class GraphConsole {
    /**
     * path of current file
     */
    public static final String ROOT_PATH = System.getProperty("user.dir");

    /**
     * the name of gml File
     */
    private static final String GML_FILE_NAME = "celegansneural.gml";
    /**
     * dijkstra algorithm
     */
    public static final String DIJKSTRA_ALGORITHM = "dijkstra";
    /**
     * bellman ford algorithm
     */
    public static final String BELLMANFORD_ALGORITHM = "bellmanford";
    /**
     * quit command
     */
    public static final String QUIT = "quit";
    /**
     * graph command
     */
    public static final String GRAPH = "graph";
    /**
     * print command
     */
    public static final String PRINT = "print";
    /**
     * performance command
     */
    public static final String PERFORMANCE = "performance";

    public static Boolean flag = Boolean.FALSE;

    /**
     * define the graph
     */
    private static Graph<String, DefaultWeightedEdge> graph = new DirectedWeightedPseudograph<>(DefaultWeightedEdge.class);

    /**
     * define my custom Graph
     */
    private static com.uncc.Graph customGraph;

    private static long performanceTime;

    public static void main(String[] args) {
        String source, target;
        System.out.println("please input your command");
        Scanner inputCmd = new Scanner(System.in);
        String query = inputCmd.nextLine();
        while (!QUIT.equals(query)) {
            if (query != null) {
                switch (query) {
                    case GRAPH:
                        generateGraph();
                        break;
                    case PRINT:
                        if (graph.vertexSet().isEmpty()) {
                            System.out.println("please generate the graph first");
                        } else {
                            printGraph();
                        }
                        break;
                    case DIJKSTRA_ALGORITHM:
                        executeAlgorithm(inputCmd, DIJKSTRA_ALGORITHM);
                        break;
                    case BELLMANFORD_ALGORITHM:
                        executeAlgorithm(inputCmd, BELLMANFORD_ALGORITHM);
                        break;
                    case PERFORMANCE:
                        if (graph.vertexSet().isEmpty()) {
                            System.out.println("please generate the graph first");
                        } else {
                            printPerformance();
                        }
                        break;
                    default:
                        System.out.println("the input command should be one the commands: graph, print, performance, quit, dijkstra, bellmanford");
                        break;
                }
            }
            query = inputCmd.nextLine();
        }
        inputCmd.close();
    }

    /**
     * execute the specific algorithm
     *
     * @param inputCmd
     */
    private static void executeAlgorithm(Scanner inputCmd, String algorithm) {
        String source;
        String target;
        if (graph.vertexSet().isEmpty()) {
            System.out.println("please generate the graph first");
        } else {
            System.out.println("please input the source vertex:");
            source = inputCmd.nextLine();
            findShortestPath(algorithm, source);
        }
    }

    /**
     * print the performance time
     */
    private static void printPerformance() {
        if (Boolean.FALSE == flag) {
            System.out.println("please execute the graph algorithm first");
        } else {
            System.out.println("the performance of the algorithm is " + performanceTime + " ms");
        }
    }

    /**
     * find the shortest path from source vertex to other vertexes with specific algorithm
     *
     * @param algorithm
     * @param source
     */
    private static void findShortestPath(String algorithm, String source) {
        long start = System.currentTimeMillis();
        if (DIJKSTRA_ALGORITHM.equals(algorithm)) {
            //find the shortest Path using dijkstra algorithm
            //DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraAlg = new DijkstraShortestPath<String, DefaultWeightedEdge>(graph);
            //ShortestPathAlgorithm.SingleSourcePaths singlePath = dijkstraAlg.getPaths(source);
            //System.out.println("the path from " + source + " to " + target + singlePath.getPath(target));
            customGraph.dijkstra(customGraph.getVertexMap().get(source));
            customGraph.showResult();
        } else if (BELLMANFORD_ALGORITHM.equals(algorithm)) {
            //find the shortest Path using bellmanFord algorithm
            //BellmanFordShortestPath<String, DefaultWeightedEdge> bellmanFordAlg = new BellmanFordShortestPath<String, DefaultWeightedEdge>(graph);
            //ShortestPathAlgorithm.SingleSourcePaths anotherSinglePath = bellmanFordAlg.getPaths(source);
            //System.out.println("the path from " + source + " to " + target + anotherSinglePath.getPath(target));
            Boolean success = customGraph.bellmanFord(customGraph.getVertexMap().get(source));
            if (success) {
                customGraph.showResult();
            } else {
                System.out.println("there is an negative circle");
            }
        }
        long end = System.currentTimeMillis();
        performanceTime = end - start;
        flag = Boolean.TRUE;
    }

    /**
     * print the graph
     */
    private static void printGraph() {
        Map<String, Set<String>> edgeMap = new HashMap<>(1024);
        Set<DefaultWeightedEdge> edges = graph.edgeSet();
        edges.forEach(new Consumer<DefaultWeightedEdge>() {
            @Override
            public void accept(DefaultWeightedEdge edge) {
                String edgeSource = graph.getEdgeSource(edge);
                String edgeContent = "(" + edgeSource + "->" + graph.getEdgeTarget(edge) + ")"
                        + "[weight:" + graph.getEdgeWeight(edge) + "]";
                if (edgeMap.get(edgeSource) != null) {
                    edgeMap.get(edgeSource).add(edgeContent);
                } else {
                    Set<String> edgeSet = new HashSet<>();
                    edgeSet.add(edgeContent);
                    edgeMap.put(edgeSource, edgeSet);
                }
            }
        });

        //print all vertexes
        System.out.println("the vertex set of the graph is:");
        Set<String> vertexSet = graph.vertexSet();
        vertexSet.forEach(new Consumer<String>() {
            @Override
            public void accept(String sourceVertex) {
                System.out.println("the source vertex is: " + sourceVertex);
                if (edgeMap.get(sourceVertex) != null) {
                    StringBuilder edgeBuffer = new StringBuilder();
                    edgeMap.get(sourceVertex).forEach(new Consumer<String>() {
                        @Override
                        public void accept(String edgeContent) {
                            edgeBuffer.append(edgeContent);
                            edgeBuffer.append(", ");
                        }
                    });
                    System.out.println("output edge set is: ");
                    System.out.println(edgeBuffer.substring(0, edgeBuffer.length() - 1));
                }
                System.out.println();
            }
        });

    }


    /**
     * generate graph from gml file
     */
    private static void generateGraph() {
        File gmlFile = ITCSUtils.getFileFromPath(ROOT_PATH, GML_FILE_NAME);
        //define vertexProvider, equal to the vertex class
        VertexProvider<String> vertexProvider = new VertexProvider<String>() {
            @Override
            public String buildVertex(String id, Map<String, Attribute> map) {
                return id;
            }
        };

        //define edgepProvider, equal to the edge class
        EdgeProvider<String, DefaultWeightedEdge> edgeProvider = new EdgeProvider<String, DefaultWeightedEdge>() {
            @Override
            public DefaultWeightedEdge buildEdge(String source, String target, String label, Map<String, Attribute> attributeMap) {
                return graph.getEdgeSupplier().get();
            }
        };
        GmlImporter<String, DefaultWeightedEdge> importer = new GmlImporter<>(vertexProvider, edgeProvider);
        try {
            importer.importGraph(graph, gmlFile);
            System.out.println("generate graph successful!");
            Set<Vertex> vertexSet = new HashSet<Vertex>();
            for (String vertex : graph.vertexSet()) {
                vertexSet.add(new Vertex(vertex));
            }
            customGraph = new com.uncc.Graph(vertexSet);
            Set<DefaultWeightedEdge> edges = graph.edgeSet();
            for (DefaultWeightedEdge edge : edges
                    ) {
                customGraph.addEdge(new Edge(customGraph.getVertexMap().get(graph.getEdgeSource(edge)), customGraph.getVertexMap().get(graph.getEdgeTarget(edge)), graph.getEdgeWeight(edge)));
            }
        } catch (ImportException e) {
            e.printStackTrace();
            System.out.println("read gml file error!");
        }

    }
}
