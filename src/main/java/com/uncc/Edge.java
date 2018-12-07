package com.uncc;

/**
 * @Author: zerongliu
 * @Date: 12/4/18 14:55
 * @Description: This class is used to represent Edges within our graphs. It maintains the same basic functions/attributes as 
 * describe in the book (i.e. source, target, and weight).
 */
public class Edge {
    private Vertex sourceVertex;
    private Vertex targetVertex;
    private double weight;

    public Edge(Vertex sourceVertex, Vertex targetVertex, double weight) {
        this.sourceVertex = sourceVertex;
        this.targetVertex = targetVertex;
        this.weight = weight;
    }

    public boolean equals(Edge edge) {
        return this.sourceVertex.equals(edge.getSourceVertex()) &&
                this.targetVertex.equals(edge.getTargetVertex()) &&
                this.weight == edge.getWeight();
    }

    public Vertex getSourceVertex() {
        return sourceVertex;
    }

    public Vertex getTargetVertex() {
        return targetVertex;
    }

    public double getWeight() {
        return weight;
    }

    public String toString() {
        return "[ " + sourceVertex + " , " + targetVertex + " , " + weight + " ]";
    }
}
