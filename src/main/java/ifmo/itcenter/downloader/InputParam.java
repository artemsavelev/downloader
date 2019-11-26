package ifmo.itcenter.downloader;

import java.io.File;
import java.util.Scanner;


public class InputParam {

    private final String MESSAGE = "Программа имеет параметры коммандной строки\n\n" +
            "  Входные параметры:\n" +
            "  1. количество одновременно качающих потоков (1,2,3,4....)\n" +
            "  2. имя папки, куда складывать скачанные файлы\n" +
            "  3. путь к файлу со списком ссылок\n\n" +
            "  Пример вызова:\n" +
            "  java -jar utility.jar 5 output_folder links.txt\n";

    private int flows;
    private File dir;
    private File file;


    public void inputParameters(String[] args) {

        if (args.length != 3) {
            System.out.println(MESSAGE);
            System.exit(0);
        } else {
            flows = Integer.parseInt(args[0]);
            File dir = new File(args[1]);
            File file = new File(args[2]);

            if (file.isFile()) {
                this.file = file;

                if (dir.exists()) {
                    this.dir = dir;
                } else {
                    System.out.println("Папка \"" + dir + "\" не создана, создать? (y/n)");
                    Scanner scanner = new Scanner(System.in);
                    String answer = scanner.nextLine();

                    if ("y".equals(answer)) {
                        dir.mkdirs();
                    } else {
                        System.out.println("Bye!");
                        System.exit(0);
                    }

                    if (dir.isDirectory()) {
                        this.dir = dir;
                    }
                }

            } else {
                System.out.println("Переданный параметр \"" + file + "\" не является файлом, проверте правильность ввода");
                System.exit(0);
            }
        }
    }


    public int getFlows() {
        return flows;
    }

    public File getDir() {
        return dir;
    }

    public File getFile() {
        return file;
    }
}