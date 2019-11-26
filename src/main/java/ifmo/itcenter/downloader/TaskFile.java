package ifmo.itcenter.downloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskFile {
    private String fileName;
    private Map<String, List<String>> map;


    public TaskFile(String fileName) {
        this.fileName = fileName;
        this.map = new HashMap<>();

        try {
            String[] strings = Files.readAllLines(Paths.get(fileName)).toArray(new String[]{});

            for (String s : strings) {
                if (!s.isEmpty()) {
                    String[] str = s.split(" ");
                    map.computeIfAbsent(str[0], k -> new ArrayList<>()).add(str[1]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Map<String, List<String>> getMap() {
        return map;
    }


}