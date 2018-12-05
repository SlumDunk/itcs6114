package com.uncc;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @Author: zerongliu
 * @Date: 11/29/18 19:04
 * @Description: Graph class
 */
public class Graph {
    /**
     * edge map, store the vertex and its edges
     */
    private Map<Vertex, LinkedList<Edge>> edgeMap;

    /**
     * vertex set
     */
    private Set<Vertex> vertexSet;

    /**
     * vertex map
     */
    private Map<String, Vertex> vertexMap;
    /**
     * size of vertex
     */
    private int vertexSize;
    /**
     * store v.d
     */
    private Map<Vertex, Distance> distanceMap;
    /**
     * store previous node
     */
    private Map<Vertex, Vertex> previousNodeMap;
    /**
     * infinitely big
     */
    public static final int INF = Integer.MAX_VALUE;
    /**
     * represents no previous node
     */
    public static final Vertex NIL = new Vertex("-1");

    private Vertex startVertex;

    private PriorityQueue<Distance> queue = new PriorityQueue<>();

    public Graph(Set<Vertex> vertexSet) {
        this.vertexSet = vertexSet;
        this.vertexSize = vertexSet.size();
        edgeMap = new HashMap<>();
        distanceMap = new HashMap<>();
        previousNodeMap = new HashMap<>();
        vertexMap = new HashMap<>();
        vertexSet.forEach(new Consumer<Vertex>() {
            @Override
            public void accept(Vertex vertex) {
                vertexMap.put(vertex.getValue(), vertex);
            }
        });
    }

    /**
     * add a new edge
     *
     * @param edge
     */
    public void addEdge(Edge edge) {
        Vertex sourceVertex = edge.getSourceVertex();
        LinkedList<Edge> edgeList = edgeMap.getOrDefault(sourceVertex, new LinkedList<Edge>());
        edgeList.add(edge);
        edgeMap.put(sourceVertex, edgeList);
    }

    /**
     * iterate the whole graph
     */
    public void printGraph() {
        vertexSet.forEach(new Consumer<Vertex>() {
            @Override
            public void accept(Vertex vertex) {
                LinkedList<Edge> list = (LinkedList<Edge>) edgeMap.get(vertex).clone();
                while (!list.isEmpty()) {
                    Edge edge = list.pop();
                    System.out.println(edge.toString());
                }
            }
        });
    }

    /**
     * @param sourceVertex
     */
    public void initializeSingleSource(Vertex sourceVertex) {
        vertexSet.forEach(new Consumer<Vertex>() {
            @Override
            public void accept(Vertex vertex) {
                distanceMap.put(vertex, new Distance(INF, vertex));
                previousNodeMap.put(vertex, NIL);
            }
        });
        Distance sourceDistance = distanceMap.get(sourceVertex);
        sourceDistance.setValue(0);
        distanceMap.put(sourceVertex, sourceDistance);

        distanceMap.forEach(new BiConsumer<Vertex, Distance>() {
            @Override
            public void accept(Vertex vertex, Distance distance) {
                queue.add(distance);
            }
        });
    }

    /**
     * relax an edge
     *
     * @param edge
     */
    public void relax(Edge edge) {
        Vertex sourceVertex = edge.getSourceVertex();
        Vertex targetVertex = edge.getTargetVertex();
        double weight = edge.getWeight();
        if (distanceMap.get(targetVertex).getValue() > distanceMap.get(sourceVertex).getValue() + weight) {
            Distance targetDistance = distanceMap.get(targetVertex);
            targetDistance.setValue(distanceMap.get(sourceVertex).getValue() + weight);
            distanceMap.put(targetVertex, targetDistance);
            previousNodeMap.put(targetVertex, sourceVertex);
        }
    }

    /**
     * use bellman ford to find the shortest path
     *
     * @param startVertex the source code
     * @return
     */
    public boolean bellmanFord(Vertex startVertex) {
        this.startVertex = startVertex;
        initializeSingleSource(startVertex);
        for (int i = 0; i < vertexSize - 1; i++) {
            vertexSet.forEach(new Consumer<Vertex>() {
                @Override
                public void accept(Vertex vertex) {
                    LinkedList<Edge> list = (LinkedList<Edge>) edgeMap.getOrDefault(vertex, new LinkedList<>()).clone();
                    while (!list.isEmpty()) {
                        Edge edge = list.pop();
                        relax(edge);
                    }
                }
            });
        }
        //check if there is a negative circle
        Iterator<Vertex> iterator = vertexSet.iterator();
        while (iterator.hasNext()) {
            Vertex vertex = iterator.next();
            LinkedList<Edge> list = (LinkedList<Edge>) edgeMap.getOrDefault(vertex, new LinkedList<>()).clone();
            while (!list.isEmpty()) {
                Edge edge = list.pop();
                Vertex sourceVertex = edge.getSourceVertex();
                Vertex targetVertex = edge.getTargetVertex();
                double weight = edge.getWeight();
                if (distanceMap.get(targetVertex).getValue() > distanceMap.get(sourceVertex).getValue() + weight) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * use dijkstra algorithm to find the shortest path
     *
     * @param startVertex
     */
    public void dijkstra(Vertex startVertex) {
        this.startVertex = startVertex;
        initializeSingleSource(startVertex);
        Set<Integer> visitedVertex = new HashSet<>();
        while (!queue.isEmpty()) {
            Distance minDistance = queue.poll();
            Vertex sourceVertex = minDistance.getVertex();
            LinkedList<Edge> list = (LinkedList<Edge>) edgeMap.getOrDefault(sourceVertex, new LinkedList<>()).clone();
            while (!list.isEmpty()) {
                Edge edge = list.pop();
                relax(edge);
            }
        }

    }

    /**
     * show result
     */
    public void showResult() {
        Stack<Vertex> routeStack = new Stack();
        vertexSet.forEach(new Consumer<Vertex>() {
            @Override
            public void accept(Vertex vertex) {
                final Vertex targetVertex = vertexMap.get(vertex.getValue());
                while (!vertex.equals(NIL)) {
                    routeStack.push(vertex);
                    vertex = previousNodeMap.get(vertex);
                }
                if (routeStack.size() == 1 && startVertex != routeStack.peek()) {
                    System.out.print(targetVertex.getValue() + "(infitely) : ");
                    System.out.println("there is no path from source vertex:" + startVertex.getValue() + " to target vertex:" + routeStack.pop().getValue());
                } else {
                    System.out.print(targetVertex.getValue() + "(" + distanceMap.get(targetVertex).getValue() + ") : ");
                    int count = routeStack.size();
                    while (!routeStack.isEmpty()) {
                        if (count == routeStack.size()) {
                            System.out.print(routeStack.pop().getValue());
                        } else {
                            System.out.print("-->" + routeStack.pop().getValue());
                        }
                    }
                    System.out.println();
                }
            }
        });
    }

    public Map<String, Vertex> getVertexMap() {
        return this.vertexMap;
    }

    /**
     * class use for the Queue in dijkstra
     */
    private class Distance implements Comparable<Distance> {
        /**
         * distance from original vertex to current vertex
         */
        double value;
        /**
         * current Vertex
         */
        Vertex vertex;

        public Distance(double distance, Vertex vertex) {
            this.value = distance;
            this.vertex = vertex;
        }

        @Override
        public int compareTo(Distance o) {
            if (o.value < this.value) {
                return 1;
            } else if (o.value > this.value) {
                return -1;
            } else {
                return 0;
            }

        }

        public double getValue() {
            return this.value;
        }

        public Vertex getVertex() {
            return this.vertex;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public void setVertex(Vertex vertex) {
            this.vertex = vertex;
        }
    }
}