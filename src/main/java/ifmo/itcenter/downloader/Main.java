package ifmo.itcenter.downloader;

import java.io.File;


public class Main {


    public static void main(String[] args) {

        String nameOfDir = "";

        TaskFile taskFile = new TaskFile("/mnt/sdb/files/java/links.txt");

        System.out.printf("Получены задания: \n");
        for (ListOfTask listOfTask : taskFile.getListOfTasks()) {
            System.out.printf("Путь: %s в файл: %s\n", listOfTask.getUrl(), listOfTask.getFileName());
        }

        File file = new File(nameOfDir);
        if (!file.exists() || !file.isDirectory()) {
            System.out.printf("Неверное имя папки для сохранения: %s", nameOfDir);
            return;
        }



    }
}
