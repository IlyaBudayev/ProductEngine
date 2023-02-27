package org.task.logic;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FileSearcher {
    /**
     * This method searches for files within a specified directory and at a specified depth level
     * and matching a specified mask.
     *
     * @param rootPath the directory path to start the search from
     * @param depth    the depth level to search for files within
     * @param mask     the file name mask to match
     * @return searchResult the file contains searching results
     * @throws InterruptedException if an error occurs during the execution of the method
     */
    public static List<String> search(String rootPath, int depth, String mask) throws InterruptedException {

        List<String> searchResult = new ArrayList<>();
        // Create a stack to store the files to be searched
        Deque<File> stack = new ArrayDeque<>();
        stack.push(new File(rootPath));

        // Create an atomic integer to keep track of the current depth level
        AtomicInteger currentDepth = new AtomicInteger();

        // Create an executor service with a fixed thread pool of two threads
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Create a blocking queue to store the search results
        BlockingQueue<String> resultsQueue = new LinkedBlockingQueue<>();

        // Submit a task to the executor to search for files
        executor.submit(() -> {
            while (!stack.isEmpty()) {
                File current = stack.pop();

                // If the current file is a directory, add its contents to the stack
                if (current.isDirectory()) {
                    currentDepth.getAndIncrement();
                    File[] files = current.listFiles();

                    if (files != null) {
                        for (File file : files) {
                            stack.push(file);
                        }
                    }
                }

                // If the current file matches the search criteria, add its name to the results queue
                else if (currentDepth.get() == depth && current.getName().contains(mask)) {
                    resultsQueue.offer(current.getName());
                }
            }

            // Add an "END_OF_RESULTS" marker to the results queue to indicate the end of the search
            resultsQueue.offer("END_OF_RESULTS");
        });

        // Submit a task to the executor to print the search results to the console
        executor.submit(() -> {
            try {
                while (true) {
                    String result = resultsQueue.take();
                    if (result.equals("END_OF_RESULTS")) {
                        break;
                    }
                    System.out.println(result);
                    searchResult.add(result);
                }
            } catch (InterruptedException e) {
                // Handle the interrupt exception by setting the current thread's interrupt status
                Thread.currentThread().interrupt();
            }
        });

        // Shut down the executor service and wait for all tasks to complete
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        return searchResult;
    }

}
