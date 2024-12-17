package ui;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LogGoruntulemePaneli {
    public LogGoruntulemePaneli() {
        JFrame frame = new JFrame("Log Görüntüleme");
        frame.setSize(600, 400);

        JTextArea logArea = new JTextArea();
        try {
            String logIcerik = new String(Files.readAllBytes(Paths.get("data/log.txt")));
            logArea.setText(logIcerik);
        } catch (Exception e) {
            logArea.setText("Log dosyası okunamadı!");
        }

        frame.add(new JScrollPane(logArea), BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
