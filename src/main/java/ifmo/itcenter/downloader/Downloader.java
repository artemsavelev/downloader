package ifmo.itcenter.downloader;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Semaphore;

public class Downloader extends Thread {

    private Semaphore semaphore;
    private String url;
    private String fileName;
    private String path;
    private boolean full = false;
    private static long totalSize;
    private static double avgSpeed;
    private static int count;
    private static long totalTime;


    public Downloader(Semaphore semaphore, String url, String fileName, String path) {
        this.semaphore = semaphore;
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
                semaphore.acquire();

                URL url = new URL(this.url);
                NetUtils netUtils = new NetUtils(url);
                FileUtils fileUtils = new FileUtils(path, fileName);

                long start = System.currentTimeMillis();

                fileUtils.saveFile(netUtils.downloadFile(fileName));

                long end = System.currentTimeMillis();
                long resultTime = end - start;
                double speed = ((netUtils.fileSize() * 8) / (resultTime / 1000d)) / 1000;

                synchronized (this) {
                    totalSize += netUtils.fileSize();
                    totalTime += resultTime;
                    avgSpeed += speed;
                    count++;
                }

                full = true;
                semaphore.release();

                System.out.printf("Файл: %s загружен размер %s за %s на скорости %.1f kB/s\n",
                        fileName, Converter.getFileSize(netUtils.fileSize()), TimeUtils.getResultTime(resultTime), speed);

                if (semaphore.availablePermits() == Main.flows) {
                    System.out.println(output(totalTime));
                }

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String output(long time) {

        double avgSpeed = Downloader.avgSpeed / count;
        System.out.println("___________________________________\n");

        return String.format("Загружено: %d файлов, %s (%d) B\nВремя: %s \nСредняя скорость: %.1f kB/s\n",
                count, Converter.getFileSize(totalSize), totalSize, TimeUtils.getResultTime(time), avgSpeed);
    }


}
