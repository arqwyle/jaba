package org.example;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testWithNoFiles() {
        String[] args = {};
        Main.main(args);
        assertEquals("No input files specified.", outContent.toString().trim());
    }

    @Test
    public void testWithNonExistentFiles() {
        String[] args = {"aboba"};
        Main.main(args);
        assertEquals("Error reading file aboba: aboba (Не удается найти указанный файл)", outContent.toString().trim());
    }

    @Test
    public void testWithValidInput() throws IOException {
        Path tempFile = Files.createTempFile("input", ".txt");
        Files.write(tempFile, Arrays.asList("42", "3.14", "Hello"));

        String[] args = {tempFile.toString()};
        Main.main(args);

        assertTrue(Files.exists(Paths.get("integers.txt")));
        assertTrue(Files.exists(Paths.get("floats.txt")));
        assertTrue(Files.exists(Paths.get("strings.txt")));

        List<String> integerLines = Files.readAllLines(Paths.get("integers.txt"));
        List<String> floatLines = Files.readAllLines(Paths.get("floats.txt"));
        List<String> stringLines = Files.readAllLines(Paths.get("strings.txt"));

        assertEquals(1, integerLines.size());
        assertEquals("42", integerLines.getFirst());

        assertEquals(1, floatLines.size());
        assertEquals("3.14", floatLines.getFirst());

        assertEquals(1, stringLines.size());
        assertEquals("Hello", stringLines.getFirst());

        Files.delete(tempFile);
        Files.delete(Paths.get("integers.txt"));
        Files.delete(Paths.get("floats.txt"));
        Files.delete(Paths.get("strings.txt"));
    }

    @Test
    public void testWithOutput() throws IOException {
        Path tempFile = Files.createTempFile("input", ".txt");
        Files.write(tempFile, Arrays.asList("42", "3.14", "Hello"));

        String[] args = {"-o", "output", tempFile.toString()};
        Main.main(args);

        assertTrue(Files.exists(Paths.get("output/integers.txt")));
        assertTrue(Files.exists(Paths.get("output/floats.txt")));
        assertTrue(Files.exists(Paths.get("output/strings.txt")));

        List<String> integerLines = Files.readAllLines(Paths.get("output/integers.txt"));
        List<String> floatLines = Files.readAllLines(Paths.get("output/floats.txt"));
        List<String> stringLines = Files.readAllLines(Paths.get("output/strings.txt"));

        assertEquals(1, integerLines.size());
        assertEquals("42", integerLines.getFirst());

        assertEquals(1, floatLines.size());
        assertEquals("3.14", floatLines.getFirst());

        assertEquals(1, stringLines.size());
        assertEquals("Hello", stringLines.getFirst());

        Files.delete(tempFile);
        Files.delete(Paths.get("output/integers.txt"));
        Files.delete(Paths.get("output/floats.txt"));
        Files.delete(Paths.get("output/strings.txt"));
        Files.delete(Paths.get("output"));
    }

    @Test
    public void testWithPrefix() throws IOException {
        Path tempFile = Files.createTempFile("input", ".txt");
        Files.write(tempFile, Arrays.asList("42", "3.14", "Hello"));

        String[] args = {"-p", "test_", tempFile.toString()};
        Main.main(args);

        assertTrue(Files.exists(Paths.get("test_integers.txt")));
        assertTrue(Files.exists(Paths.get("test_floats.txt")));
        assertTrue(Files.exists(Paths.get("test_strings.txt")));

        List<String> integerLines = Files.readAllLines(Paths.get("test_integers.txt"));
        List<String> floatLines = Files.readAllLines(Paths.get("test_floats.txt"));
        List<String> stringLines = Files.readAllLines(Paths.get("test_strings.txt"));

        assertEquals(1, integerLines.size());
        assertEquals("42", integerLines.getFirst());

        assertEquals(1, floatLines.size());
        assertEquals("3.14", floatLines.getFirst());

        assertEquals(1, stringLines.size());
        assertEquals("Hello", stringLines.getFirst());

        Files.delete(tempFile);
        Files.delete(Paths.get("test_integers.txt"));
        Files.delete(Paths.get("test_floats.txt"));
        Files.delete(Paths.get("test_strings.txt"));
    }

    @Test
    public void testWithAppend() throws IOException {
        Path tempFile = Files.createTempFile("input", ".txt");
        Files.write(tempFile, Arrays.asList("42", "3.14", "Hello"));

        String[] args = {"-a", tempFile.toString()};
        Main.main(args);
        Main.main(args);

        List<String> integerLines = Files.readAllLines(Paths.get("integers.txt"));
        List<String> floatLines = Files.readAllLines(Paths.get("floats.txt"));
        List<String> stringLines = Files.readAllLines(Paths.get("strings.txt"));

        assertEquals(2, integerLines.size());
        assertEquals(2, floatLines.size());
        assertEquals(2, stringLines.size());

        Files.delete(tempFile);
        Files.delete(Paths.get("integers.txt"));
        Files.delete(Paths.get("floats.txt"));
        Files.delete(Paths.get("strings.txt"));
    }

    @Test
    public void testWithFullStatistics() throws IOException {
        Path tempFile = Files.createTempFile("input", ".txt");
        Files.write(tempFile, Arrays.asList("42", "3.14", "Hello"));

        String[] args = {"-f", tempFile.toString()};
        Main.main(args);
        assertEquals("Integers:Count=1,Min=42,Max=42,Sum=42,Average=42.0"
                + "Floats:Count=1,Min=3.14,Max=3.14,Sum=3.14,Average=3.14"
                + "Strings:Count=1,MinLength=5,MaxLength=5", outContent.toString().replaceAll("\\s", ""));

        Files.delete(tempFile);
        Files.delete(Paths.get("integers.txt"));
        Files.delete(Paths.get("floats.txt"));
        Files.delete(Paths.get("strings.txt"));
    }

    @Test
    public void testWithShortStatistics() throws IOException {
        Path tempFile = Files.createTempFile("input", ".txt");
        Files.write(tempFile, Arrays.asList("42", "3.14", "Hello"));

        String[] args = {"-s", tempFile.toString()};
        Main.main(args);
        assertEquals("Integers:Count=1Floats:Count=1Strings:Count=1", outContent.toString().replaceAll("\\s", ""));

        Files.delete(tempFile);
        Files.delete(Paths.get("integers.txt"));
        Files.delete(Paths.get("floats.txt"));
        Files.delete(Paths.get("strings.txt"));
    }
}