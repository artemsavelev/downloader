package ifmo.itcenter.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.Semaphore;

public class Downloader extends Thread {

    private Semaphore sem;
    private String url;
    private String fileName;
    private String path;
    private long fileSize;
    private long resultTime;
    private boolean full = false;

    public Downloader(Semaphore sem, String url, String fileName, String path) {
        this.sem = sem;
        this.url = url;
        this.fileName = fileName;
        this.path = path;
    }

    @Override
    public void run() {
        try {
            // если еще не скачал
            if (!full) {
                //Запрашиваем у семафора разрешение на выполнение
                sem.acquire();

                long start = System.currentTimeMillis();

                URL url = new URL(this.url);

                // узнаем количество байт в файле
                HttpURLConnection urlFileSize = (HttpURLConnection) url.openConnection();
                fileSize = urlFileSize.getContentLengthLong();

                // поток для скачивания
                ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
                System.out.printf("Загружается файл: %s размер %s\n", fileName, Converter.getFileSize(fileSize));

                // сохраняем на диск
                FileOutputStream fileOutputStream = new FileOutputStream(path + File.separator + fileName);
                fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                fileOutputStream.close();

                long end = System.currentTimeMillis();
                resultTime = end - start;
                double speed = ((fileSize * 8) / (resultTime / 1000d)) / 1000;

                System.out.printf("Файл: %s загружен размер %s за %s на скорости %.1f kB/s\n",
                        fileName, Converter.getFileSize(fileSize), TimeUtils.getResultTime(resultTime), speed);

                full = true;
                sem.release();

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Что-то пошло не так!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
