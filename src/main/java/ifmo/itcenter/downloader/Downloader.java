package ifmo.itcenter.downloader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Downloader implements Runnable {
    private String url;
    private String fileName;


    public Downloader(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
    }

    @Override
    public void run() {

        try {
            long start = System.currentTimeMillis();

            URL url = new URL(this.url);

            // узнаем количество байт в файле
            HttpURLConnection urlFileSize = (HttpURLConnection) url.openConnection();
            long fileSize = urlFileSize.getContentLengthLong();

            // поток для скачивания
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());

            // сохраняем на диск
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();

            long end = System.currentTimeMillis();
            long result = end - start;

            System.out.printf("Скачан файл: %s в файл: %s размер %s bytes за %s ms\n", this.url, fileName, fileSize, result);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
