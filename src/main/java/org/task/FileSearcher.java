package org.task;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;


public class FileSearcher {
    public static void search(String rootPath, int depth, String mask) {

        Deque<File> stack = new ArrayDeque<>();
        stack.push(new File(rootPath));
        int currentDepth = 0;

        while (!stack.isEmpty()) {
            File current = stack.pop();
            if (current.isDirectory()) {
                currentDepth++;
                File[] files = current.listFiles();

                if (files != null) {
                    for (File file : files) {
                        stack.push(file);
                    }
                }
            } else if (currentDepth == depth && current.getName().contains(mask)) {
                System.out.println(current.getName());
            }
        }
    }
}
