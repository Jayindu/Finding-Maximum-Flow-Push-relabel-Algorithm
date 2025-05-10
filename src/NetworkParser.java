/*
 Handles parsing network definitions from files.
 K W J H D Kandewaththa
 w2053201/20230162
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class NetworkParser {


    public static FlowNetwork readFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // Read number of nodes
            int n = Integer.parseInt(reader.readLine().trim());

            // Create network
            FlowNetwork network = new FlowNetwork(n);

            // Read edges
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 3) {
                    int from = Integer.parseInt(parts[0]);
                    int to = Integer.parseInt(parts[1]);
                    int capacity = Integer.parseInt(parts[2]);

                    network.addEdge(from, to, capacity);
                }
            }

            return network;
        }
    }
}