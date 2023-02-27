package org.task.logic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientHandlerTest {
    @Test
    void testRun() throws IOException {
        // Arrange
        String rootPath = "src\\";
        Socket clientSocket = Mockito.mock(Socket.class);
        MockedStatic<FileSearcher> fileSearcherMockedStatic = Mockito.mockStatic(FileSearcher.class);
        BufferedReader reader = new BufferedReader(new StringReader("2 text\nquit\n"));
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer, true);
        Mockito.when(clientSocket.getInputStream()).thenReturn(toInputStream(reader));
        Mockito.when(clientSocket.getOutputStream()).thenReturn(toOutputStream(printWriter));
        ClientHandler clientHandler = new ClientHandler(clientSocket, rootPath);

        // Act
        clientHandler.run();

        // Assert
        assertEquals("Welcome to the File Searcher!\r\n" +
                        "Enter the search depth and file mask (separated by a space) or 'quit' to exit:\r\n" +
                        "Searching for files matching 'text' at depth 2...\r\n" +
                        "No files found.\r\n" +
                        "Enter the search depth and file mask (separated by a space) or 'quit' to exit:\r\n" +
                        "Goodbye!\r\n",
                writer.toString());
        fileSearcherMockedStatic.verify(() -> FileSearcher.search(rootPath, 2, "text"));
        fileSearcherMockedStatic.close();
    }

    private static InputStream toInputStream(BufferedReader reader) {
        return new InputStream() {
            @Override
            public int read() throws IOException {
                return reader.read();
            }
        };
    }

    private static OutputStream toOutputStream(PrintWriter writer) {
        return new OutputStream() {
            @Override
            public void write(int b) {
                writer.write(b);
            }
        };
    }
}
