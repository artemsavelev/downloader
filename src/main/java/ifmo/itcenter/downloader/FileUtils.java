package ifmo.itcenter.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;

public class FileUtils {
    private String path;
    private String fileName;

    public FileUtils() {
    }

    public FileUtils(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;
    }

    public void saveFile(Channel channel) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path + File.separator + fileName)) {
            fileOutputStream.getChannel().transferFrom((ReadableByteChannel) channel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyFile(File source, File dest) throws IOException {
            Files.copy(source.toPath(), dest.toPath());
    }


}
