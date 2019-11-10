package ifmo.itcenter.downloader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) throws InterruptedException {


        int numberOfFlows = 0;
        String nameOfDir = "";
        String linkOfList = "/mnt/sdb/java/links.txt";


        List<Downloader> downloader = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        TaskFile taskFile = new TaskFile(linkOfList);

        System.out.println("Получены задания:");


        for (ListOfTask listOfTask : taskFile.getListOfTasks()) {

            Downloader downloadTask = new Downloader(listOfTask.getUrl(), listOfTask.getFileName());
            downloader.add(downloadTask);

            Thread thread = new Thread(downloadTask);
            threads.add(thread);
            thread.start();

        }


        for (Thread thread : threads) {
            thread.join();
        }



    }
}
