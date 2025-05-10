/*
 Represents a flow network with nodes and edges.
 K W J H D Kandewaththa
 w2053201/20230162
 */

import java.util.ArrayList;
import java.util.List;

public class FlowNetwork {
    private int n;                  // Number of nodes
    private List<Edge>[] graph;     // Adjacency list


    public FlowNetwork(int n) {
        this.n = n;

        // Initialize adjacency lists
        graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
    }


    public void addEdge(int from, int to, int capacity) {
        if (from < 0 || from >= n || to < 0 || to >= n) {
            throw new IllegalArgumentException("Node indices must be between 0 and " + (n-1));
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }

        // Create forward edge
        Edge forward = new Edge(from, to, capacity, graph[to].size());

        // Create backward edge with 0 capacity
        Edge backward = new Edge(to, from, 0, graph[from].size());

        // Add edges to the graph
        graph[from].add(forward);
        graph[to].add(backward);
    }


    public int getSize() {
        return n;
    }


    public List<Edge> getEdges(int node) {
        if (node < 0 || node >= n) {
            throw new IllegalArgumentException("Node index must be between 0 and " + (n-1));
        }
        return graph[node];
    }


    public List<Edge> getFlowEdges() {
        List<Edge> flowEdges = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (Edge edge : graph[i]) {
                if (edge.getCapacity() > 0 && edge.getFlow() > 0) {
                    flowEdges.add(edge);
                }
            }
        }

        return flowEdges;
    }


    public void printNetwork() {
        System.out.println("Flow Network:");
        for (int i = 0; i < n; i++) {
            for (Edge edge : graph[i]) {
                if (edge.getCapacity() > 0) {  // Only print forward edges
                    System.out.printf("Edge %d → %d: Capacity = %d, Flow = %d\n",
                            edge.getFrom(), edge.getTo(), edge.getCapacity(), edge.getFlow());
                }
            }
        }
    }
}
