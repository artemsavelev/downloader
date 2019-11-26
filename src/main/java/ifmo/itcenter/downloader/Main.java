package ifmo.itcenter.downloader;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Main {
    public static int flows;


    public static void main(String[] args) {

        InputParam input = new InputParam();
        input.inputParameters(args);
        flows = input.getFlows();

        Semaphore semaphore = new Semaphore(input.getFlows());
        TaskFile taskFile = new TaskFile(input.getFile().toString());

        System.out.println("Получены задания на загрузку:");

        for (Map.Entry<String, List<String>> entry : taskFile.getMap().entrySet()) {
            System.out.println("Из: " + entry.getKey() + " в файл: " + entry.getValue());

            Thread thread = new Thread(new Downloader(semaphore, entry.getKey(),
                    entry.getValue(), input.getDir().toString()));
            thread.start();
        }

    }
}
