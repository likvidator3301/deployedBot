package Utils;

import java.io.*;
import java.util.ArrayList;

public class FileWorker {
    public static void writeToFile(String pathToFile, String info) {
        try {
            var fos = new FileOutputStream(pathToFile);
            var buffer = info.getBytes();

            fos.write(buffer, 0, buffer.length);
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл");
        }
    }

    public static ArrayList<String> getLinesFromFile(String pathToFile) {
        var result = new ArrayList<String>();
        try {
            var fis = new FileInputStream(pathToFile);
            var br = new BufferedReader(new InputStreamReader(fis));
            String strLine;
            while ((strLine = br.readLine()) != null)
                result.add(strLine);

        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла");
        }
        return result;
    }
}
