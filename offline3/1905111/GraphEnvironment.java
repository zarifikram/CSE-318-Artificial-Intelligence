package com.zikram;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GraphEnvironment {
    public int getnNodes() {
        return nNodes;
    }

    private int nNodes;
    private int nEdges;
    private long minWeight, maxWeight;
    private final int INF = 1000000000;
    private ArrayList<LinkedList<WeightedNode>> adjacentList;
    private int[][] adjacentMatrix;
    private ArrayList<Edge> edgeList;

    public GraphEnvironment(int nNodes) {
        this.nNodes = nNodes;
        this.nEdges = 0;
        this.adjacentList = new ArrayList<>();
        this.adjacentMatrix = new int[nNodes][nNodes];
        this.edgeList = new ArrayList<>();
        reset();
    }

    private void reset() {
        // initiate the adjacent list with empty lists
        // initiate the adjacent matrix with 0 [0 being empty]
        for(int i = 0; i < nNodes; i++) adjacentList.add(new LinkedList<>());
        for(int i = 0; i < nNodes; i++){
            for(int j = 0; j < nNodes; j++){
                this.adjacentMatrix[i][j] = 0;
            }
        }

        minWeight = INF;
        maxWeight = -INF;
    }

    public void addEdge(int u, int v, int w, boolean isUndirected){
//                  w
//        adds u -------> v (or undirected)
        u -= 1; v -= 1;
        adjacentList.get(u).add(new WeightedNode(v, w));
        adjacentMatrix[u][v] = w;
        if(isUndirected){
            adjacentList.get(v).add(new WeightedNode(u, w));
            adjacentMatrix[v][u] = w;
        }
        edgeList.add(new Edge(u, v, w));
        this.nEdges++;
        updateMinWeight(w);
        updateMaxWeight(w);
    }

    private void updateMinWeight(int w) {
        if(w < minWeight) minWeight = w;
    }

    private void updateMaxWeight(int w) {
        if(w > maxWeight) maxWeight = w;
    }



    public void showGraph(){
        for(int i = 0; i < nNodes; i++){
            for(WeightedNode node : adjacentList.get(i)){
                System.out.println(i + "  ---->   " + node.getNode() + " (" + node.getWeight() + ") ");
            }
        }
    }

    public long getCut(Set<Integer> s, Set<Integer> sPrime){
        long cut = 0;
        for(int u : s){
            for(int i = 0; i < nNodes; i++)if(sPrime.contains(i)){
                cut += adjacentMatrix[u][i];
            }
        }

        return cut;
    }

    public long getMinWeight() {
        return minWeight;
    }

    public long getMaxWeight() {
        return maxWeight;
    }

    public ArrayList<Edge> getEdgesGreaterThan(float gamma) {
        ArrayList edges = new ArrayList();
        for(Edge edge : edgeList) if(edge.weight >= gamma) edges.add(edge);
        return edges;
    }
}

class WeightedNode {
    private int node;
    private int weight;

    public WeightedNode(int node, int weight) {
        this.node = node;
        this.weight = weight;
    }

    public int getNode() {
        return node;
    }

    public int getWeight() {
        return weight;
    }
}

class Edge {
    public int u, v, weight;

    public Edge(int u, int v, int w) {
        this.u = u;
        this.v = v;
        this.weight = w;
    }
}