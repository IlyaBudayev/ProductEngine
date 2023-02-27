package org.task.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    // Declare instance variables
    private final Socket clientSocket;
    private final String rootPath;

    // Define the constructor for the ClientHandler class
    public ClientHandler(Socket socket, String rootPath) {
        this.clientSocket = socket;
        this.rootPath = rootPath;
    }

    // Override the run method of the Runnable interface
    @Override
    public void run() {

        // Use try-with-resources statement to create BufferedReader and PrintWriter objects
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Send welcome message and instructions to the client
            writer.println("Welcome to the File Searcher!");
            writer.println("Enter the search depth and file mask (separated by a space) or 'quit' to exit:");

            String input;
            // Read user input until the user enters "quit"
            while ((input = reader.readLine()) != null) {
                if ("quit".equalsIgnoreCase(input.trim())) {
                    break;
                }

                // Parse the user input and check if it is valid
                String[] searchParams = input.trim().split(" ");
                if (searchParams.length != 2) {
                    writer.println("Invalid input. Enter the search depth and file mask (separated by a space) or 'quit' to exit:");
                    continue;
                }

                // Convert the search depth to an integer and check if it is valid
                int depth;
                try {
                    depth = Integer.parseInt(searchParams[0]);
                } catch (NumberFormatException e) {
                    writer.println("Invalid depth. The input must be digit and > 0. Enter the search depth and file mask (separated by a space) or 'quit' to exit:");
                    continue;
                }

                // Get the file mask from the user input
                String mask = searchParams[1];

                // Send a message to the client indicating that the search is underway
                writer.println("Searching for files matching '" + mask + "' at depth " + depth + "...");

                // Search for files and send the results back to the client
                List<String> results = FileSearcher.search(rootPath, depth, mask);

                if (results.isEmpty()) {
                    writer.println("No files found.");
                } else {
                    writer.println("Found " + results.size() + " files:");
                    for (String result : results) {
                        writer.println(result);
                    }
                }

                // Send instructions to the client to enter another search or quit
                writer.println("Enter the search depth and file mask (separated by a space) or 'quit' to exit:");
            }

            // Send goodbye message and close the client socket
            writer.println("Goodbye!");
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

