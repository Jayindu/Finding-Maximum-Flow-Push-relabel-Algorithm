/*
 Implementation of the Push-Relabel algorithm for maximum flow.
 K W J H D Kandewaththa
 w2053201/20230162
 */

import java.util.ArrayList;
import java.util.List;

public class PushRelabelAlgorithm {
    private FlowNetwork network;    // The flow network
    private int[] excess;           // Excess flow at each vertex
    private int[] height;           // Height of each vertex
    private List<String> steps;     // Steps taken during algorithm execution
    private int source;             // Source node
    private int sink;               // Sink node


    public PushRelabelAlgorithm(FlowNetwork network, int source, int sink) {
        this.network = network;
        this.source = source;
        this.sink = sink;

        int n = network.getSize();

        // Initialize arrays
        excess = new int[n];
        height = new int[n];
        steps = new ArrayList<>();
    }


    private void push(int u, int v, Edge edge) {
        int flowAmount = Math.min(excess[u], edge.remainingCapacity());

        if (flowAmount > 0 && height[u] > height[v]) {
            // Update flow
            edge.addFlow(flowAmount);

            // Update reverse edge flow
            Edge reverseEdge = network.getEdges(v).get(edge.getIndex());
            reverseEdge.setFlow(reverseEdge.getFlow() - flowAmount);

            // Update excess flow
            excess[u] -= flowAmount;
            excess[v] += flowAmount;

            steps.add(String.format("Push: %d units from node %d to node %d", flowAmount, u, v));
        }
    }


    private void relabel(int u) {
        int minHeight = Integer.MAX_VALUE;

        // Find minimum height among neighbors with residual capacity
        for (Edge edge : network.getEdges(u)) {
            if (edge.remainingCapacity() > 0) {
                minHeight = Math.min(minHeight, height[edge.getTo()]);
            }
        }

        if (minHeight != Integer.MAX_VALUE) {
            height[u] = minHeight + 1;
            steps.add(String.format("Relabel: Node %d to height %d", u, height[u]));
        }
    }


    private void discharge(int u) {
        while (excess[u] > 0) {
            // Try pushing to each neighbor
            List<Edge> edges = network.getEdges(u);
            boolean pushed = false;

            for (int i = 0; i < edges.size() && excess[u] > 0; i++) {
                Edge edge = edges.get(i);
                if (edge.remainingCapacity() > 0 && height[u] == height[edge.getTo()] + 1) {
                    push(u, edge.getTo(), edge);
                    pushed = true;
                }
            }

            // If no push was possible, relabel
            if (!pushed) {
                relabel(u);

                // If relabel didn't increase height, we can't push anywhere
                if (minHeight(u) == Integer.MAX_VALUE) {
                    break;
                }
            }
        }
    }


    private int minHeight(int u) {
        int minHeight = Integer.MAX_VALUE;
        for (Edge edge : network.getEdges(u)) {
            if (edge.remainingCapacity() > 0) {
                minHeight = Math.min(minHeight, height[edge.getTo()]);
            }
        }
        return minHeight;
    }


    private void initPreflow() {
        int n = network.getSize();

        // Set height of source to n
        height[source] = n;

        // For all edges from source, push maximum flow
        for (Edge edge : network.getEdges(source)) {
            edge.setFlow(edge.getCapacity());

            // Update excess at destination
            excess[edge.getTo()] += edge.getCapacity();

            // Update reverse edge
            Edge reverseEdge = network.getEdges(edge.getTo()).get(edge.getIndex());
            reverseEdge.setFlow(-edge.getCapacity());
        }

        steps.add("Initialize preflow: Source sends flow to neighbors");
    }


    public int findMaxFlow() {
        // Clear steps history
        steps.clear();

        // Initialize preflow
        initPreflow();

        // Create list of non-source, non-sink vertices
        List<Integer> activeNodes = new ArrayList<>();
        for (int i = 0; i < network.getSize(); i++) {
            if (i != source && i != sink) {
                activeNodes.add(i);
            }
        }

        // FIFO implementation
        int i = 0;
        while (i < activeNodes.size()) {
            int u = activeNodes.get(i);
            int oldExcess = excess[u];

            discharge(u);

            // If vertex still has excess, move it to the end of the list
            if (excess[u] > 0 && oldExcess <= excess[u]) {
                activeNodes.remove(i);
                activeNodes.add(u);
            } else {
                i++;
            }
        }

        // Calculate max flow (sum of flows from source)
        int maxFlow = 0;
        for (Edge edge : network.getEdges(source)) {
            maxFlow += edge.getFlow();
        }

        return maxFlow;
    }


    public List<String> getSteps() {
        return steps;
    }
}