package ui;

import model.Rezervasyon;
import service.RezervasyonYonetimi;
import data.FileHandler;
import data.LogManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class UserPanel {
    private JFrame frame;
    private RezervasyonYonetimi rezervasyonYonetimi;
    private List<String> seferListesi;

    public UserPanel() {
        ArrayList<Rezervasyon> rezervasyonlar = FileHandler.rezervasyonOku("data/rezervasyonlar.rez");
        rezervasyonYonetimi = new RezervasyonYonetimi(rezervasyonlar);
        seferListesi = seferleriBelirle();
        initUI();
    }

    private void initUI() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("User Paneli - Sefer ve Boş Koltuk Seçimi");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel label = new JLabel("Mevcut Seferler:");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        // Seferleri Listeleyen JList
        DefaultListModel<String> listModel = new DefaultListModel<>();
        seferListesi.forEach(listModel::addElement);
        JList<String> seferJList = new JList<>(listModel);
        seferJList.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(seferJList);

        JButton secButton = new JButton("Sefer Seç ve Boş Koltukları Göster");
        secButton.setFont(new Font("Arial", Font.BOLD, 14));
        secButton.setBackground(new Color(70, 130, 180));
        secButton.setForeground(Color.WHITE);

        secButton.addActionListener(e -> {
            String secilenSefer = seferJList.getSelectedValue();
            if (secilenSefer != null) {
                bosKoltukSecimEkrani(secilenSefer);
            } else {
                JOptionPane.showMessageDialog(frame, "Lütfen bir sefer seçin!");
            }
        });

        frame.add(label, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(secButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void bosKoltukSecimEkrani(String secilenSefer) {
        JFrame koltukFrame = new JFrame("Sefer: " + secilenSefer + " - Boş Koltuk Seçimi");
        koltukFrame.setSize(400, 400);
        koltukFrame.setLayout(new GridLayout(5, 5));

        HashSet<Integer> doluKoltuklar = doluKoltuklariGetir(secilenSefer);

        for (int i = 1; i <= 25; i++) {
            final int koltukNo = i;
            JButton koltukButton = new JButton(String.valueOf(i));

            if (doluKoltuklar.contains(koltukNo)) {
                koltukButton.setBackground(Color.RED);
                koltukButton.setEnabled(false);
            } else {
                koltukButton.setBackground(Color.GREEN);
                koltukButton.addActionListener(e -> {
                    String adSoyad = JOptionPane.showInputDialog("Ad Soyad:");
                    if (adSoyad != null && !adSoyad.trim().isEmpty()) {
                        String[] seferBilgisi = secilenSefer.split(" - ");
                        Rezervasyon yeniRezervasyon = new Rezervasyon(
                                adSoyad,
                                seferBilgisi[1], // Tarih
                                seferBilgisi[0], // Güzergah
                                seferBilgisi[2], // Saat
                                koltukNo
                        );
                        rezervasyonYonetimi.ekle(yeniRezervasyon);
                        FileHandler.rezervasyonYaz("data/rezervasyonlar.rez", rezervasyonYonetimi.getRezervasyonListesi());
                        LogManager.log("User tarafından koltuk seçildi: " + yeniRezervasyon);
                        JOptionPane.showMessageDialog(koltukFrame, "Rezervasyon başarıyla eklendi!");
                        koltukFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(koltukFrame, "Ad Soyad boş bırakılamaz.");
                    }
                });
            }
            koltukFrame.add(koltukButton);
        }
        koltukFrame.setVisible(true);
    }

    private List<String> seferleriBelirle() {
        return rezervasyonYonetimi.getRezervasyonListesi().stream()
                .map(r -> r.getGuzergah() + " - " + r.getTarih() + " - " + r.getSaat())
                .distinct()
                .collect(Collectors.toList());
    }

    private HashSet<Integer> doluKoltuklariGetir(String secilenSefer) {
        HashSet<Integer> doluKoltuklar = new HashSet<>();
        for (Rezervasyon r : rezervasyonYonetimi.getRezervasyonListesi()) {
            String sefer = r.getGuzergah() + " - " + r.getTarih() + " - " + r.getSaat();
            if (sefer.equals(secilenSefer)) {
                doluKoltuklar.add(r.getKoltukNo());
            }
        }
        return doluKoltuklar;
    }
}
