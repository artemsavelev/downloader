package ifmo.itcenter.downloader;

public class ListOfTask {
    private String url;
    private String fileName;

    public ListOfTask(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }
}
