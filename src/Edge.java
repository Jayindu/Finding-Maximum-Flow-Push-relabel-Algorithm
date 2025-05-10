/*
 Represents an edge in the flow network.
 K W J H D Kandewaththa
 w2053201/20230162
 */
public class Edge {
    private int from;       // Source node
    private int to;         // Destination node
    private int capacity;   // Capacity of edge
    private int flow;       // Current flow
    private int index;      // Index of reverse edge


    public Edge(int from, int to, int capacity, int index) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
        this.flow = 0;
        this.index = index;
    }


    public int remainingCapacity() {
        return capacity - flow;
    }


    public void addFlow(int amount) {
        flow += amount;
    }

    // Getters and setters
    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return String.format("Edge(%d→%d, cap: %d, flow: %d)", from, to, capacity, flow);
    }
}