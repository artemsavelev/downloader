package ifmo.itcenter.downloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TaskFile {
    private String fileName;
    private List<ListOfTask> listOfTasks;


    public TaskFile(String fileName) {
        this.fileName = fileName;
        this.listOfTasks = new ArrayList<>();

        try {
            String[] strings = Files.readAllLines(Paths.get(fileName)).toArray(new String[]{});

            for (String s : strings) {
                if (!s.isEmpty()) {
                    String[] str = s.split(" ");
                    listOfTasks.add(new ListOfTask(str[0], str[1]));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<ListOfTask> getListOfTasks() {
        return listOfTasks;
    }
}
