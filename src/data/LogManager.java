package data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LogManager {
    public static void log(String mesaj) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/log.txt", true))) {
            bw.write("[" + java.time.LocalDateTime.now() + "] " + mesaj);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
