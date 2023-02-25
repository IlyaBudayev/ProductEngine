package org.task.logic;

import java.util.Scanner;

import static java.lang.System.exit;


public class ConsoleService {

    private static final String space = "//----------------------------------------------------------------------------------------//";

    public static void start() {
        System.out.println(space);
        System.out.println("Hello and Welcome!");
        fillTheData();
    }

    // Options
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

    protected static int getDepth(Scanner scanner) {
        String depth = scanner.next();

        while (!depth.matches("[0-9]+")) {
            exitCase(depth);
            System.out.println("Incorrect depth. Must be positive digits. Or type exit");
            depth = scanner.next();
        }


        return Integer.parseInt(depth);
    }

    protected static String checkTheAbsolutePath(Scanner scanner) {
        String rootPath = scanner.next();
        while (!rootPath.matches("^([A-Za-z]:\\\\.*$)")
                && !rootPath.matches("/[A-Za-z0-9]+/")) {
            exitCase(rootPath);
            System.out.println("Incorrect path, try another or type exit to quit");
            rootPath = scanner.next();
        }
        return rootPath;
    }

    private static void exitCase(String rootPath) {
        if (rootPath.equals("exit")) {
            System.out.println("Good bye!");
            exit(0);
        }
    }


}