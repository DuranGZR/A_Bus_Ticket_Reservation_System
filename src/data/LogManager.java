package data;

import java.io.*;
import java.util.ArrayList;

public class LogManager {
    // Log yazma metodu
    public static void log(String mesaj) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/log.txt", true))) {
            bw.write("[" + java.time.LocalDateTime.now() + "] " + mesaj);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Log okuma metodu
    public static ArrayList<String> logOku() {
        ArrayList<String> loglar = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data/log.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                loglar.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Log dosyası bulunamadı. Yeni log dosyası oluşturulacak.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loglar;
    }
}
