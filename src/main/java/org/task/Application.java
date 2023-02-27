package org.task;

public class Application {
    public static void main(String[] args) {
        System.out.println("Starting");
        TelnetServer telnetServer = new TelnetServer(8081,"src\\");
        telnetServer.start();
    }
}