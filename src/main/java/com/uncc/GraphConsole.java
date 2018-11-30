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
 * @Date: 11/29/18 19:04
 * @Description: the main class for project 4.
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

    private static Graph<String, DefaultWeightedEdge> graph = new DirectedWeightedPseudograph<>(DefaultWeightedEdge.class);

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
                        printGraph();
                        break;
                    case DIJKSTRA_ALGORITHM:
                        System.out.println("please input the source vertex:");
                        source = inputCmd.nextLine();
                        System.out.println("please input the target vertex:");
                        target = inputCmd.nextLine();
                        findShortestPath(DIJKSTRA_ALGORITHM, source, target);
                        break;
                    case BELLMANFORD_ALGORITHM:
                        System.out.println("please input the source vertex:");
                        source = inputCmd.nextLine();
                        System.out.println("please input the target vertex:");
                        target = inputCmd.nextLine();
                        findShortestPath(BELLMANFORD_ALGORITHM, source, target);
                        break;
                    case PERFORMANCE:
                        printPerformance();
                        break;
                    default:
                        break;
                }
            }
            query = inputCmd.nextLine();
        }
        inputCmd.close();
    }

    /**
     * print the performance time
     */
    private static void printPerformance() {
        System.out.println("the performance of the algorithm is " + performanceTime);
    }

    /**
     * find the shortest path from source vertex to target vertex with specific algorithm
     *
     * @param algorithm
     * @param source
     * @param target
     */
    private static void findShortestPath(String algorithm, String source, String target) {
        long start = System.currentTimeMillis();
        if (DIJKSTRA_ALGORITHM.equals(algorithm)) {
            //find the shortest Path using dijkstra algorithm
            DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraAlg = new DijkstraShortestPath<String, DefaultWeightedEdge>(graph);
            ShortestPathAlgorithm.SingleSourcePaths singlePath = dijkstraAlg.getPaths(source);
            System.out.println("the path from " + source + " to " + target + singlePath.getPath(target));
        } else if (BELLMANFORD_ALGORITHM.equals(algorithm)) {
            //find the shortest Path using bellmanFord algorithm
            BellmanFordShortestPath<String, DefaultWeightedEdge> bellmanFordAlg = new BellmanFordShortestPath<String, DefaultWeightedEdge>(graph);
            ShortestPathAlgorithm.SingleSourcePaths anotherSinglePath = bellmanFordAlg.getPaths(source);
            System.out.println("the path from " + source + " to " + target + anotherSinglePath.getPath(target));
        }
        long end = System.currentTimeMillis();
        performanceTime = end - start;
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
        VertexProvider<String> vertexProvider = new VertexProvider<String>() {
            @Override
            public String buildVertex(String id, Map<String, Attribute> map) {
                return id;
            }
        };

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
        } catch (ImportException e) {
            e.printStackTrace();
            System.out.println("read gml file error!");
        }

    }
}
