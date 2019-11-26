package ifmo.itcenter.downloader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class NetUtils {
    private URL url;

    public NetUtils(URL url) {
        this.url = url;
    }

    public ReadableByteChannel downloadFile(String fileName) throws IOException {
        System.out.printf("Загружается файл: %s размер %s\n", fileName, Converter.getFileSize(fileSize()));
        return Channels.newChannel(url.openStream());
    }

    public long fileSize() throws IOException {
        HttpURLConnection  size = (HttpURLConnection) url.openConnection();
        size.setRequestMethod("HEAD");
        return size.getContentLengthLong();
    }
}