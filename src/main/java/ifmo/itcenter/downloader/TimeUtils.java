package ifmo.itcenter.downloader;

public class TimeUtils {

    public static String getResultTime(long time) {

        double seconds = (double) time / 1000;
        int s = (int) seconds % 60;
        int minutes = (int) seconds / 60;

        if (minutes == 0) {
            return String.format("%.3f %s", seconds, TimeUtils.morpher((int) seconds, "s"));
        }

        return String.format("%2d %s %2d %s", minutes, TimeUtils.morpher(minutes, "m"), s, TimeUtils.morpher(s, "s"));
    }

    private static String morpher(int count, String type) {
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
