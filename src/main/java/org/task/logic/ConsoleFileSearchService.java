package org.task.logic;

import java.util.Scanner;

import static java.lang.System.exit;


public class ConsoleFileSearchService {
    private static final String space = "//----------------------------------------------------------------------------------------//";

    /**
     * Start the console service.
     */
    public static void start() {
        System.out.println(space);
        System.out.println("Hello and Welcome!");
        fillTheData();
    }

    /**
     * Prompt the user for search options, including the root path, search depth, and file mask.
     * Then initiate the search with the provided options using FileSearcher.
     * Upon completion of the search, prompt the user for additional search options.
     */
    private static void fillTheData() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Fill the rootPath value. (type \"exit\" if you want to quit search)");
        String rootPath = checkTheAbsolutePath(scanner);

        System.out.println("Enter the depth number");
        int depth = getDepth(scanner);

        System.out.println("Fill the mask value");
        String mask = scanner.next();

        //Start search
        System.out.println("Searching for: " + mask);

        try {
            FileSearcher.search(rootPath, depth, mask);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        //Back to search
        fillTheData();
    }

    /**
     * Prompt the user to enter a search depth, and verify that it is a positive integer.
     *
     * @param scanner Scanner object used to read user input.
     * @return int value representing the search depth.
     */
    protected static int getDepth(Scanner scanner) {
        String depth = scanner.next();

        while (!depth.matches("[0-9]+")) {
            exitCase(depth);
            System.out.println("Incorrect depth. Must be positive digits. Or type exit");
            depth = scanner.next();
        }

        return Integer.parseInt(depth);
    }

    /**
     * Prompt the user to enter the root path of the directory tree to search, and verify that it is a valid path.
     *
     * @param scanner Scanner object used to read user input.
     * @return String value representing the root path of the directory tree to search.
     */
    protected static String checkTheAbsolutePath(Scanner scanner) {
        String rootPath = scanner.next();
        while (!rootPath.matches("^src\\\\$")) {
            exitCase(rootPath);
            System.out.println("Incorrect path. Must starts from src\\, try another or type exit to quit");
            rootPath = scanner.next();
        }
        return rootPath;
    }

    /**
     * Check if the user has entered the "exit" command, and exit the program if they have.
     *
     * @param rootPath String value entered by the user.
     */
    private static void exitCase(String rootPath) {
        if (rootPath.equals("exit")) {
            System.out.println("Good bye!");
            exit(0);
        }
    }

}