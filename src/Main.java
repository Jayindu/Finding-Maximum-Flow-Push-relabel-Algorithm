/*
 Main class to run the network flow algorithm.
 K W J H D Kandewaththa
 w2053201/20230162
 */

import java.io.IOException;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        try {
            // Check if filename was provided
            String filename = "resources/bridge_1.txt";  // Default filename
            if (args.length > 0) {
                filename = args[0];
            }

            System.out.println("Reading network from file: " + filename);

            // Read network from file
            FlowNetwork network = NetworkParser.readFromFile(filename);
            System.out.println("Network loaded with " + network.getSize() + " nodes");

            // Print network
            network.printNetwork();

            // Find maximum flow (source is 0, sink is n-1)
            System.out.println("\nComputing maximum flow...");
            long startTime = System.nanoTime();

            PushRelabelAlgorithm algorithm = new PushRelabelAlgorithm(network, 0, network.getSize() - 1);
            int maxFlow = algorithm.findMaxFlow();

            long endTime = System.nanoTime();

            // Print algorithm steps first
            System.out.println("\nAlgorithm Steps:");
            List<String> steps = algorithm.getSteps();
            int stepsToShow = Math.min(steps.size(), 50);  // Limit number of steps to display
            for (int i = 0; i < stepsToShow; i++) {
                System.out.println(steps.get(i));
            }
            if (steps.size() > stepsToShow) {
                System.out.println("... (and " + (steps.size() - stepsToShow) + " more steps)");
            }

            // Print flow assignment
            System.out.println("\nFlow Assignment:");
            for (Edge edge : network.getFlowEdges()) {
                System.out.printf("f(%d,%d) = %d\n", edge.getFrom(), edge.getTo(), edge.getFlow());
            }

            // Print time taken
            double timeTaken = (endTime - startTime) / 1_000_000.0;
            System.out.printf("\nTime taken: %.3f ms\n", timeTaken);

            // Print maximum flow at the bottom
            System.out.println("\n------------------------------------------------------");
            System.out.println("MAXIMUM FLOW: " + maxFlow);
            System.out.println("------------------------------------------------------");

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}