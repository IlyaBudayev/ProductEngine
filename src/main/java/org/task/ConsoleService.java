package org.task;

import java.util.Scanner;

import static java.lang.System.exit;


public class ConsoleService {

    private static final String space = "//----------------------------------------------------------------------------------------//";

    public static void start() {
        System.out.println("Hello and Welcome!");

        fillTheData();

    }

    // Options
    private static void fillTheData() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Fill the rootPath value");
        String rootPath = checkTheAbsolutePath(scanner);

        System.out.println("Enter the depth number");
        int depth = getDepth(scanner);

        System.out.println("Fill the mask value");
        String mask = scanner.next();

        //Start search
        FileSearcher.search(rootPath, depth, mask);
        System.out.println(space);
    }

    private static int getDepth(Scanner scanner) {
        String depth = scanner.next();
        while (!depth.matches("[0-9]+")) {
            System.out.println("Incorrect depth. Must be digits. Or type exit");
            if (depth.equals("exit")) {
                exit(0);
            }
            depth = scanner.next();
        }
        return Integer.parseInt(depth);
    }

    private static String checkTheAbsolutePath(Scanner scanner) {
        String rootPath = scanner.next();
        while (!rootPath.matches("^([A-Za-z]:\\\\.*$)")
                && !rootPath.matches("/[A-Za-z0-9]+/")) {
            System.out.println("Incorrect path, try another or type exit to quit");
            if (rootPath.equals("exit")) {
                exit(0);
            }
            rootPath = scanner.next();
        }
        return rootPath;
    }


}