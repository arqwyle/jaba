package org.example;

import org.apache.commons.cli.*;

import java.io.*;
import java.util.ArrayList;

class DataFilter {
    protected static String outputPath = "";
    protected static String prefix = "";
    protected static boolean appendMode = false;
    protected static boolean fullStats = false;
    protected static boolean shortStats = false;

    protected static ArrayList<Integer> integers = new ArrayList<>();
    protected static ArrayList<Double> floats = new ArrayList<>();
    protected static ArrayList<String> strings = new ArrayList<>();

    protected static int minInt = Integer.MAX_VALUE;
    protected static int maxInt = Integer.MIN_VALUE;
    protected static long sumInt = 0;

    protected static double minFloat = Double.MAX_VALUE;
    protected static double maxFloat = Double.MIN_VALUE;
    protected static double sumFloat = 0;

    protected static int minStringLength = Integer.MAX_VALUE;
    protected static int maxStringLength = 0;

    protected static void run(String[] inputFiles) {
        processFiles(inputFiles);
        writeToFile(prefix + "integers.txt", integers);
        writeToFile(prefix + "floats.txt", floats);
        writeToFile(prefix + "strings.txt", strings);
        printStatistics();
        clear();
    }

    protected static void processFiles(String[] inputFiles) {
        for (String file : inputFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    processLine(line);
                }
            } catch (Exception e) {
                System.out.println("Error reading file " + file + ": " + e.getMessage());
            }
        }
    }

    protected static void processLine(String line) {
        try {
            int intValue = Integer.parseInt(line);
            integers.add(intValue);
            sumInt += intValue;
            minInt = Math.min(minInt, intValue);
            maxInt = Math.max(maxInt, intValue);
        } catch (NumberFormatException e1) {
            try {
                double floatValue = Double.parseDouble(line);
                floats.add(floatValue);
                sumFloat += floatValue;
                minFloat = Math.min(minFloat, floatValue);
                maxFloat = Math.max(maxFloat, floatValue);
            } catch (NumberFormatException e2) {
                strings.add(line);
                minStringLength = Math.min(minStringLength, line.length());
                maxStringLength = Math.max(maxStringLength, line.length());
            }
        }
    }

    protected static <T> void writeToFile(String fileName, ArrayList<T> list) {
        if (list.isEmpty()) {
            return;
        }
        try {
            File directory = new File(outputPath);
            directory.mkdirs();
        } catch (SecurityException e) {
            System.out.println("Error creating output directory " + fileName + ": " + e.getMessage());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(!outputPath.isEmpty() ? outputPath + '\\' + fileName : fileName, appendMode))) {
            for (T item : list) {
                writer.write(item.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file " + fileName + ": " + e.getMessage());
        }
    }

    protected static void printStatistics() {
        if (fullStats) {
            if (!integers.isEmpty()) {
                System.out.println("Integers:"
                        + "Count=" + integers.size()
                        + ", Min=" + minInt
                        + ", Max=" + maxInt
                        + ", Sum=" + sumInt
                        + ", Average=" + (double) sumInt / integers.size());
            }
            if (!floats.isEmpty()) {
                System.out.println("Floats:"
                        + "Count=" + floats.size()
                        + ", Min=" + minFloat
                        + ", Max=" + maxFloat
                        + ", Sum=" + sumFloat
                        + ", Average=" + sumFloat / floats.size());
            }
            if (!strings.isEmpty()) {
                System.out.println("Strings:"
                        + "Count=" + strings.size()
                        + ", Min Length=" + minStringLength
                        + ", Max Length=" + maxStringLength);
            }
        }
        if (shortStats) {
            if (!integers.isEmpty()) {
                System.out.println("Integers: Count=" + integers.size());
            }
            if (!floats.isEmpty()) {
                System.out.println("Floats: Count=" + floats.size());
            }
            if (!strings.isEmpty()) {
                System.out.println("Strings: Count=" + strings.size());
            }
        }
    }

    protected static void clear() {
        outputPath = "";
        prefix = "";
        appendMode = false;
        fullStats = false;
        shortStats = false;

        integers.clear();
        floats.clear();
        strings.clear();

        minInt = Integer.MAX_VALUE;
        maxInt = Integer.MIN_VALUE;
        sumInt = 0;

        minFloat = Double.MAX_VALUE;
        maxFloat = Double.MIN_VALUE;
        sumFloat = 0;

        minStringLength = Integer.MAX_VALUE;
        maxStringLength = 0;
    }
}

public class Main {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("o", true, "Output directory");
        options.addOption("p", true, "Output file prefix");
        options.addOption("a", false, "Append mode");
        options.addOption("s", false, "Short statistics");
        options.addOption("f", false, "Full statistics");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            DataFilter.outputPath = cmd.getOptionValue("o", "");
            DataFilter.prefix = cmd.getOptionValue("p", "");
            DataFilter.appendMode = cmd.hasOption("a");
            DataFilter.fullStats = cmd.hasOption("f");
            DataFilter.shortStats = cmd.hasOption("s");

            String[] inputFiles = cmd.getArgs();
            if (inputFiles.length == 0) {
                System.out.println("No input files specified.");
                return;
            }

            DataFilter.run(inputFiles);

        } catch (ParseException e) {
            System.out.println("Error parsing command line options: " + e.getMessage());
        }
    }
}