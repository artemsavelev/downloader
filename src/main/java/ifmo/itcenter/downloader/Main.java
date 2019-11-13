package ifmo.itcenter.downloader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Main {

    public static final String MESSAGE = "Программа имеет параметры коммандной строки\n\n" +
            "  Входные параметры:\n" +
            "  1. количество одновременно качающих потоков (1,2,3,4....)\n" +
            "  2. имя папки, куда складывать скачанные файлы\n" +
            "  3. путь к файлу со списком ссылок\n\n" +
            "  Пример вызова:\n" +
            "  java -jar utility.jar 5 output_folder links.txt\n";

    public static void main(String[] args) {

        int numberOfFlows;
        String nameOfDir;
        String linkOfList;
        Path dir = Paths.get(args[1]);
        Path file = Paths.get(args[2]);

        if (args.length != 3) {
            System.out.println(MESSAGE);
            return;
        } else {
            numberOfFlows = Integer.parseInt(args[0]);

            if (!Files.isDirectory(dir)) {
                nameOfDir = dir.toString();
            } else {
                System.out.println("Переданный параметр " +
                        "\"" + dir.toString() + "\" " +
                        "не является папкой, проверте правильность ввода");
                return;
            }

            if (Files.isRegularFile(file)) {
                linkOfList = file.toString();
            } else {
                System.out.println("Переданный параметр " +
                        "\"" + file.toString() + "\" " +
                        "не является файлом, проверте правильность ввода");
                return;
            }
        }

//        numberOfFlows = 2;
//        nameOfDir = "";
//        linkOfList = "/mnt/sdb/java/links.txt";

        Semaphore sem = new Semaphore(numberOfFlows);
        Downloader downloader = null;
        TaskFile taskFile = new TaskFile(linkOfList);
        List<Thread> threads = new ArrayList<>();

        System.out.println("Получены задания на загрузку:");

        for (int i = 0; i < taskFile.getListOfTasks().size(); i++) {
            ListOfTask listOfTask = taskFile.getListOfTasks().get(i);
            System.out.println("Из " + listOfTask.getUrl() + " в файл " + listOfTask.getFileName());

            downloader = new Downloader(sem, listOfTask.getUrl(), listOfTask.getFileName());
            downloader.start();
            threads.add(downloader);
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        printResult(taskFile.getListOfTasks().size(), 0, 0, 0);

    }

    private static void printResult(int count, int totalSize, int totalTime, int avgSpeed) {
        System.out.println("__________________________________________________________________________________\n");
        System.out.println("Загружено: " + count + " файлов, " + totalSize);
        System.out.println("Время: ");
        System.out.println("Средняя скорость: " + avgSpeed + "\n");
    }


}

