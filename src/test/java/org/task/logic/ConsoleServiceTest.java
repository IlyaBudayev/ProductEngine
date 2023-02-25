package org.task.logic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConsoleServiceTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    // Set up System.out to write to outContent before each test
    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    // Restore System.out to its original value after each test
    @AfterEach
    void restoreStreams() {
        System.setOut(System.out);
    }
    @Test
    void assertGetDepthUnhappyTest() {
        String data = "-4\r\n";
        InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            Scanner scanner = new Scanner(System.in);
            assertThrows(NoSuchElementException.class, () -> ConsoleService.getDepth(scanner));
            assertEquals("Incorrect depth. Must be positive digits. Or type exit", outContent.toString().trim());
        } finally {
            System.setIn(stdin);
        }
    }

    @Test
    void assertGetDepthHappyTest() {
        String data = "4\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        Scanner scanner = new Scanner(System.in);
        ConsoleService.getDepth(scanner);
        String inputString = scanner. nextLine();
        assertEquals(inputString,"");
    }

    @Test
    void checkTheAbsolutePathHappyTest() {
        String data = "C:\\Users\\kurin\\IdeaProjects\\ProductEngine\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        Scanner scanner = new Scanner(System.in);
        ConsoleService.checkTheAbsolutePath(scanner);
        String inputString = scanner. nextLine();
        assertEquals(inputString,"");
    }

    @Test
    void checkTheAbsolutePathUnHappyTest() {
        String data = "-4\r\n";
        InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            Scanner scanner = new Scanner(System.in);
            assertThrows(NoSuchElementException.class, () -> ConsoleService.checkTheAbsolutePath(scanner));
            assertEquals("Incorrect path, try another or type exit to quit", outContent.toString().trim());
        } finally {
            System.setIn(stdin);
        }
    }
}