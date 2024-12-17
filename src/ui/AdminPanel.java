package ui;

import model.Rezervasyon;
import service.RezervasyonYonetimi;
import data.FileHandler;
import data.LogManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class AdminPanel {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private RezervasyonYonetimi rezervasyonYonetimi;
    private HashMap<String, Integer> toplamKoltukSayisi; // Sefer bazında toplam koltuk sayısını tutar.
    private final String SEFER_DOSYA = "data/seferler.rez";

    public AdminPanel() {
        // Verileri yükle
        ArrayList<Rezervasyon> rezervasyonlar = FileHandler.rezervasyonOku("data/rezervasyonlar.rez");
        rezervasyonYonetimi = new RezervasyonYonetimi(rezervasyonlar);
        toplamKoltukSayisi = FileHandler.seferOku(SEFER_DOSYA);

        initUI();
    }

    private void initUI() {
        // Arayüz başlatma
        frame = new JFrame("Admin Paneli - Boş Koltuk Yönetimi");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Tablo oluşturma
        model = new DefaultTableModel(new String[]{"Güzergah", "Tarih", "Saat", "Toplam Koltuk", "Dolu Koltuklar"}, 0);
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton seferEkleButton = new JButton("Sefer Ekle");
        JButton bosKoltukGoruntuleButton = new JButton("Seferi Düzenle");

        // Buton stilleri
        seferEkleButton.setBackground(new Color(34, 139, 34));
        bosKoltukGoruntuleButton.setBackground(new Color(70, 130, 180));
        seferEkleButton.setForeground(Color.WHITE);
        bosKoltukGoruntuleButton.setForeground(Color.WHITE);

        // Butonların actionları
        seferEkleButton.addActionListener(e -> seferEkle());
        bosKoltukGoruntuleButton.addActionListener(e -> seferSecVeKoltuklariDuzenle());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(seferEkleButton);
        buttonPanel.add(bosKoltukGoruntuleButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        veriGoster();
        frame.setVisible(true);
    }

    private void seferEkle() {
        // Sefer ekleme formu
        JTextField guzergahField = new JTextField();
        JTextField tarihField = new JTextField("YYYY-MM-DD");
        JTextField saatField = new JTextField("HH:MM");
        JTextField koltukSayisiField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.add(new JLabel("Güzergah:"));
        panel.add(guzergahField);
        panel.add(new JLabel("Tarih:"));
        panel.add(tarihField);
        panel.add(new JLabel("Saat:"));
        panel.add(saatField);
        panel.add(new JLabel("Toplam Koltuk Sayısı:"));
        panel.add(koltukSayisiField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Yeni Sefer Ekle", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String guzergah = guzergahField.getText();
            String tarih = tarihField.getText();
            String saat = saatField.getText();
            int toplamKoltuk = Integer.parseInt(koltukSayisiField.getText());

            String seferKey = guzergah + " - " + tarih + " - " + saat;
            toplamKoltukSayisi.put(seferKey, toplamKoltuk);

            // Seferleri dosyaya kaydet
            FileHandler.seferYaz(SEFER_DOSYA, toplamKoltukSayisi);

            LogManager.log("Yeni sefer eklendi: " + seferKey + ", Koltuk Sayısı: " + toplamKoltuk);
            veriGoster();
        }
    }

    private void seferSecVeKoltuklariDuzenle() {
        int row = table.getSelectedRow();
        if (row != -1) {
            String guzergah = (String) model.getValueAt(row, 0);
            String tarih = (String) model.getValueAt(row, 1);
            String saat = (String) model.getValueAt(row, 2);

            String seferKey = guzergah + " - " + tarih + " - " + saat;
            int toplamKoltuk = toplamKoltukSayisi.getOrDefault(seferKey, 0);
            HashSet<Integer> doluKoltuklar = doluKoltuklariGetir(seferKey);

            StringBuilder bosKoltuklar = new StringBuilder("Boş Koltuklar: \n");
            for (int i = 1; i <= toplamKoltuk; i++) {
                if (!doluKoltuklar.contains(i)) {
                    bosKoltuklar.append(i).append(" ");
                }
            }

            JOptionPane.showMessageDialog(frame, bosKoltuklar.toString(), "Boş Koltuklar - " + seferKey, JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Lütfen bir sefer seçin.");
        }
    }

    private HashSet<Integer> doluKoltuklariGetir(String seferKey) {
        HashSet<Integer> doluKoltuklar = new HashSet<>();
        for (Rezervasyon r : rezervasyonYonetimi.getRezervasyonListesi()) {
            String rSefer = r.getGuzergah() + " - " + r.getTarih() + " - " + r.getSaat();
            if (rSefer.equals(seferKey)) {
                doluKoltuklar.add(r.getKoltukNo());
            }
        }
        return doluKoltuklar;
    }

    private void veriGoster() {
        model.setRowCount(0);
        HashMap<String, Integer> doluKoltukSayisi = new HashMap<>();

        for (Rezervasyon r : rezervasyonYonetimi.getRezervasyonListesi()) {
            String key = r.getGuzergah() + " - " + r.getTarih() + " - " + r.getSaat();
            doluKoltukSayisi.put(key, doluKoltukSayisi.getOrDefault(key, 0) + 1);
        }

        toplamKoltukSayisi.forEach((sefer, toplamKoltuk) -> {
            int doluKoltuk = doluKoltukSayisi.getOrDefault(sefer, 0);
            model.addRow(new Object[]{sefer.split(" - ")[0], sefer.split(" - ")[1], sefer.split(" - ")[2], toplamKoltuk, doluKoltuk});
        });
    }
}
