package ui;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

public class KoltukSecimEkrani {
    private JFrame frame;
    private HashSet<Integer> doluKoltuklar;

    public KoltukSecimEkrani(HashSet<Integer> doluKoltuklar) {
        this.doluKoltuklar = doluKoltuklar;
        initUI();
    }

    private void initUI() {
        frame = new JFrame("Koltuk Seçimi");
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(5, 5));

        for (int i = 1; i <= 25; i++) {
            final int koltukNo = i; // Lambda içinde kullanılabilir hale getirilir.
            JButton koltukButton = new JButton(String.valueOf(i));

            if (doluKoltuklar.contains(koltukNo)) {
                koltukButton.setBackground(Color.RED);
                koltukButton.setEnabled(false);
            } else {
                koltukButton.setBackground(Color.GREEN);
                koltukButton.addActionListener(e -> {
                    JOptionPane.showMessageDialog(frame, "Seçilen Koltuk: " + koltukNo);
                    frame.dispose();
                });
            }

            frame.add(koltukButton);
        }
        frame.setVisible(true);
    }
}
