package org.example;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ProgressBarTest {
    @Test
    public void testProgressBarThreadsCompletion() throws InterruptedException {
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        int calculationLength = 20;

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        ProgressBar[] progressBars = new ProgressBar[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            progressBars[i] = new ProgressBar(i + 1, calculationLength);
            executor.submit(progressBars[i]);
        }

        executor.shutdown();
        boolean completed = executor.awaitTermination(10, TimeUnit.SECONDS);

        assertTrue(completed, "Not all threads completed within the timeout");
    }
}