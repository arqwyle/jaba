package org.example;
import java.util.concurrent.TimeUnit;

class ProgressBar implements Runnable {
    private final int threadNumber;
    private final int length;
    private final long startTime;

    public ProgressBar(int threadNumber, int length) {
        this.threadNumber = threadNumber;
        this.length = length;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        long threadId = Thread.currentThread().threadId();
        StringBuilder progressBar = new StringBuilder("[");
        for (int i = 0; i < length; i++) {
            synchronized (System.out) {
                clearLine(threadNumber);
                printProgress(threadNumber, threadId, progressBar.toString(), length);
            }
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            progressBar.append("#");
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        synchronized (System.out) {
            clearLine(threadNumber);
            System.out.printf("%-15s %d %-15s %d ms%n", "Thread " + threadNumber, threadId, "Completed in:", elapsedTime);
        }
    }

    // Очистка строки с использованием ANSI-кодов
    private void clearLine(int threadNumber) {
        System.out.print("\033[" + (threadNumber + 1) + ";0H"); // Перемещаем курсор
        System.out.print("\r" + " ".repeat(100) + "\r"); // Очищаем строку
    }

    // Вывод прогресса
    private void printProgress(int threadNumber, long threadId, String progressBar, int length) {
        System.out.printf("%-15s %d %-15s %s]%n",
                "Thread " + threadNumber, threadId, "Progress:", progressBar + " ".repeat(length - progressBar.length()));
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int numberOfThreads = 12;
        int calculationLength = 20;

        // Очистка консоли перед началом работы
        clearConsole();

        Thread[] threads = new Thread[numberOfThreads];
        ProgressBar[] progressBars = new ProgressBar[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            progressBars[i] = new ProgressBar(i + 1, calculationLength);
            threads[i] = new Thread(progressBars[i]);
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        // Вернуть курсор в конец вывода
        synchronized (System.out) {
            System.out.print("\033[" + (numberOfThreads + 2) + ";0H"); // Перемещаем курсор в конец
        }
    }

    // Функция для очистки консоли
    private static void clearConsole() {
        // Попытка очистить консоль с помощью ANSI-кода
        System.out.print("\033[H\033[2J");
        System.out.flush(); // Принудительная отправка данных в консоль
    }
}