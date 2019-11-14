package ifmo.itcenter.downloader;

public class Converter {
    public static String getFileSize(long fileSize) {

        if (fileSize < 1024) {
            return fileSize + " B";
        }
        int z = (63 - Long.numberOfLeadingZeros(fileSize)) / 10;

        return String.format("%.1f %sB", (double) fileSize / (1L << (z * 10)), " KMGTPE".charAt(z));
    }
}
