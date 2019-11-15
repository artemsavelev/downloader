package ifmo.itcenter.downloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
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
        File dir;
        File file;


        if (args.length != 3) {
            System.out.println(MESSAGE);
            return;
        } else {

            numberOfFlows = Integer.parseInt(args[0]);
            dir = new File(args[1]);
            file = new File(args[2]);

            if (dir.exists()) {
                nameOfDir = dir.toString();
            } else {

                System.out.println("Папка " +
                        "\"" + dir.toString() + "\" " +
                        "не создана, создать? (y/n)");

                Scanner scanner = new Scanner(System.in);
                String answer = scanner.nextLine();

                if (answer.equals("y")) {
                    dir.mkdirs();
                } else {
                    System.out.println("Bye!");
                    return;
                }

                if (dir.isDirectory()) {
                    nameOfDir = dir.toString();
                } else {
                    System.out.println("Переданный параметр " +
                            "\"" + dir.toString() + "\" " +
                            "не является папкой, проверте правильность ввода");
                    return;
                }

            }

            if (file.isFile()) {
                linkOfList = file.toString();
            } else {
                System.out.println("Переданный параметр " +
                        "\"" + file.toString() + "\" " +
                        "не является файлом, проверте правильность ввода");
                return;
            }

        }

        Semaphore sem = new Semaphore(numberOfFlows);
        Downloader downloader;
        TaskFile taskFile = new TaskFile(linkOfList);
        List<Thread> threads = new ArrayList<>();

        System.out.println("Получены задания на загрузку:");
        long start = System.currentTimeMillis();

        for (int i = 0; i < taskFile.getListOfTasks().size(); i++) {
            ListOfTask listOfTask = taskFile.getListOfTasks().get(i);
            System.out.println("Из " + listOfTask.getUrl() + " в файл " + listOfTask.getFileName());

            downloader = new Downloader(sem, listOfTask.getUrl(), listOfTask.getFileName(), nameOfDir);
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
        long end = System.currentTimeMillis();
        long resultTime = end - start;

        long size = 0;
        int count;
        String totalSize;

        if (file.length() != 0) {

            for (File files : Objects.requireNonNull(dir.listFiles())) {
                size += files.length();
            }

            count = Objects.requireNonNull(dir.listFiles()).length;
            totalSize = Converter.getFileSize(size);

        } else {

            System.out.println("Список заданий в файле " +
                    "\"" + file.toString() + "\" " +
                    "пуст, проверьте правильность заполнения файла");
            return;
        }

        System.out.println("___________________________________\n");
        System.out.println("Загружено: " + count + " файлов, " + totalSize);
        System.out.println("Время: " + TimeUtils.getResultTime(resultTime));
        System.out.println("Средняя скорость: " + 0 + "\n");

    }
}

