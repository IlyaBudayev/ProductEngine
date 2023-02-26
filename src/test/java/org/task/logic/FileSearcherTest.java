package org.task.logic;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class FileSearcherTest {

    @Test
    void assertThatSearchFindFilesOK() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        FileSearcher.search("src\\", 8, "text");
        System.setOut(System.out);
        String output = outContent.toString().trim();
        assertEquals("text3.txt\r\ntext2.txt\r\ntext1.txt", output);
    }

    @Test
    void assertThatSearchFindFiles_NOT_OK() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        FileSearcher.search("src\\", 7, "text");
        System.setOut(System.out);
        String output = outContent.toString().trim();
        assertNotEquals("text3.txt\r\ntext2.txt\r\ntext1.txt", output);
    }

}
