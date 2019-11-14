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
                System.out.printf("Загружается файл: %s размер %s\n", fileName, getFileSize(fileSize));

                // сохраняем на диск
                FileOutputStream fileOutputStream = new FileOutputStream(path + File.separator + fileName);
                fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                fileOutputStream.close();

                long end = System.currentTimeMillis();
                resultTime = end - start;
                double speed = ((fileSize * 8) / (resultTime / 1000d)) / 1000;

                System.out.printf("Файл: %s загружен размер %s за %s на скорости %.1f kB/s\n", fileName, getFileSize(fileSize), getResultTime(), speed);

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

    public String getFileSize(long fileSize) {

        if (fileSize < 1024) {
            return fileSize + " B";
        }
        int z = (63 - Long.numberOfLeadingZeros(fileSize)) / 10;

        return String.format("%.1f %sB", (double) fileSize / (1L << (z * 10)), " KMGTPE".charAt(z));
    }

    public String getResultTime() {

        double seconds = (double) resultTime / 1000;
        int s = (int) seconds % 60;
        int minutes = (int) seconds / 60;

        if (minutes == 0) {
            return String.format("%.3f %s", seconds, morpher((int) seconds, "s"));
        }

        return String.format("%2d %s %2d %s", minutes, morpher(minutes, "m"), s, morpher(s, "s"));
    }

    private String morpher(int count, String type) {
        String one = "";
        String two = "";
        String three = "";

        if (type.equals("m")) {

            one = "минуту";
            two = "минуты";
            three = "минут";
        }

        if (type.equals("s")) {

            one = "секунду";
            two = "секунды";
            three = "секунд";
        }

        switch (count) {
            case 1:
            case 21:
            case 31:
            case 41:
            case 51:
                return one;
            case 2:
            case 3:
            case 4:
            case 22:
            case 23:
            case 24:
            case 32:
            case 33:
            case 34:
            case 42:
            case 43:
            case 44:
            case 52:
            case 53:
            case 54:
                return two;
            default:
                return three;
        }
    }
}
