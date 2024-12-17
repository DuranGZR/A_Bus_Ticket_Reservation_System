package ui;

import model.Rezervasyon;
import service.RezervasyonYonetimi;
import data.FileHandler;
import data.LogManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UserPanel {
    private JFrame frame;
    private RezervasyonYonetimi rezervasyonYonetimi;

    public UserPanel() {
        ArrayList<Rezervasyon> rezervasyonlar = FileHandler.rezervasyonOku("data/rezervasyonlar.rez");
        rezervasyonYonetimi = new RezervasyonYonetimi(rezervasyonlar);
        initUI();
    }

    private void initUI() {
        frame = new JFrame("User Paneli");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton rezervasyonEkleButton = new JButton("Rezervasyon Ekle");
        rezervasyonEkleButton.addActionListener(e -> rezervasyonEkle());

        JPanel panel = new JPanel();
        panel.add(rezervasyonEkleButton);

        frame.add(panel, BorderLayout.CENTER);
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
        LogManager.log("User tarafından yeni rezervasyon eklendi: " + yeniRezervasyon);
    }
}
