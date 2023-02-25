package org.task.logic;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class FileSearcher {

    protected static void search(String rootPath, int depth, String mask) throws InterruptedException {
        // Create a stack to keep track of files to search, starting with the root path
        Deque<File> stack = new ArrayDeque<>();
        stack.push(new File(rootPath));
        AtomicBoolean foundAnything= new AtomicBoolean(false);
        // Keep track of the current depth of the search
        AtomicInteger currentDepth = new AtomicInteger();
        // Create an executor with two threads
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Submit the search task to the executor
        Future<?> searchFuture = executor.submit(() -> {
            // Loop until there are no more files to search
            while (!stack.isEmpty()) {
                File current = stack.pop();
                // If it's a directory, add its contents to the stack
                if (current.isDirectory()) {
                    currentDepth.getAndIncrement();
                    // Interrupt when depth is exceeded the initial value
                    if(currentDepth.get() > depth){
                        return;
                    }
                    File[] files = current.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            stack.push(file);
                        }
                    }
                    // If it's a file at the desired depth and matches the mask, print the result
                } else if (currentDepth.get() == depth && current.getName().contains(mask)) {
                    printResult(current.getName());
                }
            }
        });

        // Submit the print task to the executor
        executor.submit(() -> {
            // Loop until the search is done and there are no more results to print
            while (!searchFuture.isDone() || !resultQueue.isEmpty()) {
                // Poll the next result from the queue
                String result = resultQueue.poll();
                // If there is a result, print it to the console
                if (result != null) {
                    System.out.println(result);
                    foundAnything.set(true);
                }
            }
        });

        // Shutdown the executor when both tasks are done
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        printNothingFoundCase(foundAnything);
    }

    private static void printNothingFoundCase(AtomicBoolean foundAnything) {
        if(!foundAnything.get()) {
            System.out.println("---<<<Nothing were found!>>>---");
        }
    }

    // Create a queue to hold results to be printed
    private static final Deque<String> resultQueue = new ArrayDeque<>();

    // Method to add a result to the queue
    private static synchronized void printResult(String result) {
        resultQueue.offer(result);
    }

}
