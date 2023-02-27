package org.task;

import org.task.logic.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The TelnetServer class defines a server that listens on a specified port for incoming connections from clients.
 * The server creates a thread pool to handle the clients' requests, accepts new client connections in an infinite loop,
 * and submits them to the thread pool for processing. The server also prints out a message to indicate that it has started,
 * and shuts down the thread pool when the server is closed.
 */
public class TelnetServer {
    private final int serverPort;
    private final String rootPath;

    // Constructor method with serverPort and rootPath arguments
    public TelnetServer(int serverPort, String rootPath) {
        this.serverPort = serverPort;
        this.rootPath = rootPath;
    }

    public void start() {
        // Create a new thread pool for clients handling
        ExecutorService executor = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            // Print out a message to indicate that the server has started
            System.out.println("Server started on port " + serverPort + " with root path " + rootPath);

            while (true) { // Start a loop to listen for new clients connections
                Socket clientSocket = serverSocket.accept(); // Accept new client connection
                // Submit a new ClientHandler to the executor to handle the client's requests
                executor.submit(new ClientHandler(clientSocket, rootPath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown(); // Shutdown the thread pool
        }
    }
}
