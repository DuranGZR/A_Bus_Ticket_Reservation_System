package ui;

import model.Rezervasyon;
import service.RezervasyonYonetimi;
import data.FileHandler;
import data.LogManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class AdminPanel {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private RezervasyonYonetimi rezervasyonYonetimi;

    public AdminPanel() {
        ArrayList<Rezervasyon> rezervasyonlar = FileHandler.rezervasyonOku("data/rezervasyonlar.rez");
        rezervasyonYonetimi = new RezervasyonYonetimi(rezervasyonlar);
        initUI();
    }

    private void initUI() {
        frame = new JFrame("Admin Paneli");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Tablo oluşturma
        model = new DefaultTableModel(new String[]{"Ad Soyad", "Tarih", "Güzergah", "Saat", "Koltuk No"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Butonlar
        JButton ekleButton = new JButton("Rezervasyon Ekle");
        JButton silButton = new JButton("Rezervasyon Sil");
        JButton guncelleButton = new JButton("Rezervasyon Güncelle");
        JButton loglariGorButton = new JButton("Logları Görüntüle");

        ekleButton.addActionListener(e -> rezervasyonEkle());
        silButton.addActionListener(e -> rezervasyonSil());
        guncelleButton.addActionListener(e -> rezervasyonGuncelle());
        loglariGorButton.addActionListener(e -> new LogGoruntulemePaneli());

        JPanel panel = new JPanel();
        panel.add(ekleButton);
        panel.add(silButton);
        panel.add(guncelleButton);
        panel.add(loglariGorButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);

        veriGoster();
        frame.setVisible(true);
    }

    private void rezervasyonEkle() {
        String adSoyad = JOptionPane.showInputDialog("Ad Soyad:");
        String tarih = JOptionPane.showInputDialog("Tarih (YYYY-MM-DD):");
        String guzergah = JOptionPane.showInputDialog("Güzergah:");
        String saat = JOptionPane.showInputDialog("Saat (HH:MM):");
        int koltukNo = Integer.parseInt(JOptionPane.showInputDialog("Koltuk No:"));

        Rezervasyon yeniRezervasyon = new Rezervasyon(adSoyad, tarih, guzergah, saat, koltukNo);
        rezervasyonYonetimi.ekle(yeniRezervasyon);
        LogManager.log("Admin tarafından yeni rezervasyon eklendi: " + yeniRezervasyon);
        veriGoster();
    }

    private void rezervasyonSil() {
        int row = table.getSelectedRow();
        if (row != -1) {
            Rezervasyon silinecek = rezervasyonYonetimi.getRezervasyonListesi().get(row);
            rezervasyonYonetimi.sil(row);
            LogManager.log("Admin tarafından rezervasyon silindi: " + silinecek);
            veriGoster();
        } else {
            JOptionPane.showMessageDialog(frame, "Silmek için bir satır seçin.");
        }
    }

    private void rezervasyonGuncelle() {
        int row = table.getSelectedRow();
        if (row != -1) {
            String yeniAdSoyad = JOptionPane.showInputDialog("Yeni Ad Soyad:");
            String yeniTarih = JOptionPane.showInputDialog("Yeni Tarih (YYYY-MM-DD):");
            String yeniGuzergah = JOptionPane.showInputDialog("Yeni Güzergah:");
            String yeniSaat = JOptionPane.showInputDialog("Yeni Saat (HH:MM):");
            int yeniKoltukNo = Integer.parseInt(JOptionPane.showInputDialog("Yeni Koltuk No:"));

            Rezervasyon guncelRezervasyon = new Rezervasyon(yeniAdSoyad, yeniTarih, yeniGuzergah, yeniSaat, yeniKoltukNo);
            rezervasyonYonetimi.getRezervasyonListesi().set(row, guncelRezervasyon);
            LogManager.log("Admin tarafından rezervasyon güncellendi: " + guncelRezervasyon);
            veriGoster();
        } else {
            JOptionPane.showMessageDialog(frame, "Güncellemek için bir satır seçin.");
        }
    }

    private void veriGoster() {
        model.setRowCount(0);
        for (Rezervasyon r : rezervasyonYonetimi.getRezervasyonListesi()) {
            model.addRow(new Object[]{r.getAdSoyad(), r.getTarih(), r.getGuzergah(), r.getSaat(), r.getKoltukNo()});
        }
        FileHandler.rezervasyonYaz("data/rezervasyonlar.rez", rezervasyonYonetimi.getRezervasyonListesi());
    }
}
