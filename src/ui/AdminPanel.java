package ui;

import data.Sefer;
import model.Rezervasyon;
import model.Kullanici;
import service.RezervasyonYonetimi;
import data.FileHandler;
import data.LogManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminPanel {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private RezervasyonYonetimi rezervasyonYonetimi;
    private HashMap<String, Sefer> toplamKoltukSayisi;
    private ArrayList<Kullanici> kullaniciListesi;
    private final String SEFER_DOSYA = "data/seferler.rez";

    public AdminPanel() {
        // Verileri yükle
        ArrayList<Rezervasyon> rezervasyonlar = FileHandler.rezervasyonOku("data/rezervasyonlar.rez");
        rezervasyonYonetimi = new RezervasyonYonetimi(rezervasyonlar);
        toplamKoltukSayisi = FileHandler.seferOku(SEFER_DOSYA);

        // Kullanıcıları yükle ve null kontrolü yap
        kullaniciListesi = (ArrayList<Kullanici>) FileHandler.kullaniciOku("data/kullanicilar.rez");
        if (kullaniciListesi == null) {
            kullaniciListesi = new ArrayList<>(); // Null ise boş liste başlat
        }

        initUI();
    }

    private void initUI() {
        frame = new JFrame("Admin Paneli");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"Güzergah", "Tarih", "Saat", "Toplam Koltuk", "Dolu Koltuk"}, 0);
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14)); // Başlıkları kalın yapalım
        table.setSelectionBackground(new Color(153, 204, 255)); // Satır seçildiğinde farklı bir renk
        JScrollPane scrollPane = new JScrollPane(table);

        // Butonlar
        JButton seferEkleButton = new JButton("Sefer Ekle");
        JButton seferSilButton = new JButton("Sefer Sil");
        JButton rezervasyonDuzenleButton = new JButton("Rezervasyon Düzenle");
        JButton seferAraButton = new JButton("Sefer Ara/Filtrele");
        JButton yolcuGorButton = new JButton("Yolcuları Görüntüle");
        JButton kullaniciGorButton = new JButton("Kullanıcıları Gör");
        JButton logGorButton = new JButton("Log Görüntüle");
        JButton cikisButton = new JButton("Çıkış");

        // Buton actionları
        seferEkleButton.addActionListener(e -> seferEkle());
        seferSilButton.addActionListener(e -> seferSil());
        rezervasyonDuzenleButton.addActionListener(e -> rezervasyonDuzenle());
        seferAraButton.addActionListener(e -> seferAraFiltrele());
        yolcuGorButton.addActionListener(e -> yolculariGor());
        kullaniciGorButton.addActionListener(e -> kullanicilariGor());
        logGorButton.addActionListener(e -> loglariGoruntule());
        cikisButton.addActionListener(e -> geriDon());

        // Buton Panelini yerleştirme
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 5, 10, 10)); // Grid düzeni, 2 satır, 5 sütun
        buttonPanel.add(seferEkleButton);
        buttonPanel.add(seferSilButton);
        buttonPanel.add(rezervasyonDuzenleButton);
        buttonPanel.add(seferAraButton);
        buttonPanel.add(yolcuGorButton);
        buttonPanel.add(kullaniciGorButton);
        buttonPanel.add(logGorButton);
        buttonPanel.add(cikisButton);

        // Butonlara stil eklemek
        setButtonStyle(seferEkleButton);
        setButtonStyle(seferSilButton);
        setButtonStyle(rezervasyonDuzenleButton);
        setButtonStyle(seferAraButton);
        setButtonStyle(yolcuGorButton);
        setButtonStyle(kullaniciGorButton);
        setButtonStyle(logGorButton);
        setButtonStyle(cikisButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        veriGoster();
        frame.setVisible(true);
    }

    private void setButtonStyle(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Büyük ve kalın yazı tipi
        button.setForeground(Color.WHITE); // Beyaz yazı
        button.setBackground(new Color(65, 105, 225)); // Canlı mavi arka plan
        button.setFocusPainted(false); // Odak çerçevesini kaldır
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 144, 255), 3), // Kalın çerçeve
                BorderFactory.createEmptyBorder(15, 30, 15, 30) // Geniş iç boşluk
        ));

        // Butona hafif gölge efekti ekle
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gölge efekti
                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillRoundRect(5, 5, c.getWidth() - 10, c.getHeight() - 10, 15, 15);

                // Buton rengi
                g2.setColor(button.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 15, 15);

                super.paint(g2, c);
                g2.dispose();
            }
        });

        // Hover efekti ekle
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237)); // Hover rengi
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(65, 105, 225)); // Orijinal rengi
            }
        });
    }





    private void seferEkle() {
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
            int koltukSayisi = Integer.parseInt(koltukSayisiField.getText());

            // Yeni sefer oluştur
            String seferKey = guzergah + " - " + tarih + " - " + saat;
            Sefer yeniSefer = new Sefer(guzergah, tarih, saat, koltukSayisi);

            // Mevcut seferleri oku ve yeni seferi ekle
            toplamKoltukSayisi = FileHandler.seferOku(SEFER_DOSYA); // Tüm seferleri yeniden yükle
            toplamKoltukSayisi.put(seferKey, yeniSefer); // Yeni seferi ekle

            // Dosyaya yaz ve tabloyu güncelle
            FileHandler.seferYaz(SEFER_DOSYA, toplamKoltukSayisi);
            veriGoster();
            JOptionPane.showMessageDialog(frame, "Sefer başarıyla eklendi!");
        }
    }



    private void seferSil() {
        int row = table.getSelectedRow();
        if (row != -1) {
            String seferKey = model.getValueAt(row, 0) + " - " + model.getValueAt(row, 1) + " - " + model.getValueAt(row, 2);
            toplamKoltukSayisi.remove(seferKey);
            FileHandler.seferYaz(SEFER_DOSYA, toplamKoltukSayisi);
            LogManager.log("Sefer silindi: " + seferKey);
            veriGoster();
        }
    }


    private void rezervasyonDuzenle() {
        int row = table.getSelectedRow();
        if (row != -1) {
            String guzergah = (String) model.getValueAt(row, 0);
            String tarih = (String) model.getValueAt(row, 1);
            String saat = (String) model.getValueAt(row, 2);

            String seferKey = guzergah + " - " + tarih + " - " + saat;
            ArrayList<Rezervasyon> rezervasyonlar = rezervasyonYonetimi.getRezervasyonListesi();
            ArrayList<Rezervasyon> secilenRezervasyonlar = new ArrayList<>();

            // Seferle ilişkili rezervasyonları bul
            for (Rezervasyon r : rezervasyonlar) {
                String rSefer = r.getGuzergah() + " - " + r.getTarih() + " - " + r.getSaat();
                if (rSefer.equals(seferKey)) {
                    secilenRezervasyonlar.add(r);
                }
            }

            if (secilenRezervasyonlar.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Bu sefere ait rezervasyon bulunmamaktadır.", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Rezervasyonları listele
            String[] rezervasyonAdlari = secilenRezervasyonlar.stream()
                    .map(r -> r.getAdSoyad() + " - Koltuk No: " + r.getKoltukNo())
                    .toArray(String[]::new);

            String secilenRezervasyon = (String) JOptionPane.showInputDialog(
                    frame,
                    "Rezervasyonu düzenlemek için bir yolcu seçin:",
                    "Rezervasyon Düzenle",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    rezervasyonAdlari,
                    rezervasyonAdlari[0]
            );

            if (secilenRezervasyon != null) {
                // Seçilen rezervasyonu bul
                Rezervasyon seciliRezervasyon = secilenRezervasyonlar.stream()
                        .filter(r -> (r.getAdSoyad() + " - Koltuk No: " + r.getKoltukNo()).equals(secilenRezervasyon))
                        .findFirst()
                        .orElse(null);

                if (seciliRezervasyon != null) {
                    JTextField koltukNoField = new JTextField(String.valueOf(seciliRezervasyon.getKoltukNo()));
                    JTextField adSoyadField = new JTextField(seciliRezervasyon.getAdSoyad());

                    JPanel panel = new JPanel(new GridLayout(2, 2));
                    panel.add(new JLabel("Ad Soyad:"));
                    panel.add(adSoyadField);
                    panel.add(new JLabel("Koltuk No:"));
                    panel.add(koltukNoField);

                    int result = JOptionPane.showConfirmDialog(
                            frame,
                            panel,
                            "Rezervasyon Düzenle",
                            JOptionPane.OK_CANCEL_OPTION
                    );

                    if (result == JOptionPane.OK_OPTION) {
                        // Güncellenen bilgileri kaydet
                        seciliRezervasyon.setAdSoyad(adSoyadField.getText());
                        seciliRezervasyon.setKoltukNo(Integer.parseInt(koltukNoField.getText()));

                        // Dosyayı güncelle
                        FileHandler.rezervasyonYaz("data/rezervasyonlar.rez", rezervasyonlar);
                        JOptionPane.showMessageDialog(frame, "Rezervasyon başarıyla güncellendi.");
                        LogManager.log("Rezervasyon güncellendi: " + seciliRezervasyon.getAdSoyad());
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Lütfen bir sefer seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void seferAraFiltrele() {
        String filtre = JOptionPane.showInputDialog(frame, "Güzergah, Tarih veya Saat girin:");
        if (filtre != null) {
            model.setRowCount(0);
            toplamKoltukSayisi.forEach((sefer, toplamKoltuk) -> {
                if (sefer.contains(filtre)) {
                    model.addRow(new Object[]{sefer.split(" - ")[0], sefer.split(" - ")[1], sefer.split(" - ")[2], toplamKoltuk, 0});
                }
            });
        }
    }

    private void yolculariGor() {
        int row = table.getSelectedRow();
        if (row != -1) {
            String guzergah = (String) model.getValueAt(row, 0);
            String tarih = (String) model.getValueAt(row, 1);
            String saat = (String) model.getValueAt(row, 2);

            String seferKey = guzergah + " - " + tarih + " - " + saat;
            StringBuilder yolcuListesi = new StringBuilder("Yolcu Listesi:\n");

            for (Rezervasyon r : rezervasyonYonetimi.getRezervasyonListesi()) {
                String rSefer = r.getGuzergah() + " - " + r.getTarih() + " - " + r.getSaat();
                if (rSefer.equals(seferKey)) {
                    yolcuListesi.append("Ad Soyad: ").append(r.getAdSoyad())
                            .append(", Koltuk No: ").append(r.getKoltukNo()).append("\n");
                }
            }

            if (yolcuListesi.toString().equals("Yolcu Listesi:\n")) {
                yolcuListesi.append("Bu sefere ait yolcu bulunmamaktadır.");
            }

            JOptionPane.showMessageDialog(frame, yolcuListesi.toString(),
                    "Yolcu Listesi - " + guzergah, JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Lütfen bir sefer seçin.",
                    "Uyarı", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void kullanicilariGor() {
        String[] kullaniciAdlari = kullaniciListesi.stream()
                .map(Kullanici::getKullaniciAdi)
                .toArray(String[]::new);

        // Kullanıcı seçimi
        String secilenKullaniciAdi = (String) JOptionPane.showInputDialog(
                frame,
                "Kullanıcıyı seçin:",
                "Kullanıcı Listesi",
                JOptionPane.QUESTION_MESSAGE,
                null,
                kullaniciAdlari,
                kullaniciAdlari[0]
        );

        if (secilenKullaniciAdi != null) {
            kullaniciDuzenle(secilenKullaniciAdi);
        }
    }

    private void kullaniciDuzenle(String kullaniciAdi) {
        // Kullanıcıyı bul
        Kullanici seciliKullanici = kullaniciListesi.stream()
                .filter(k -> k.getKullaniciAdi().equals(kullaniciAdi))
                .findFirst()
                .orElse(null);

        if (seciliKullanici != null) {
            JTextField rolField = new JTextField(seciliKullanici.getRol());
            JTextField sifreField = new JTextField(seciliKullanici.getSifre());

            // Düzenleme paneli oluştur
            JPanel panel = new JPanel(new GridLayout(2, 2));
            panel.add(new JLabel("Rol:"));
            panel.add(rolField);
            panel.add(new JLabel("Şifre:"));
            panel.add(sifreField);

            int result = JOptionPane.showConfirmDialog(
                    frame,
                    panel,
                    "Kullanıcıyı Düzenle",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (result == JOptionPane.OK_OPTION) {
                seciliKullanici.setRol(rolField.getText());
                seciliKullanici.setSifre(sifreField.getText());

                // Dosyaya kaydet
                FileHandler.kullaniciYaz("data/kullanicilar.rez", kullaniciListesi);
                JOptionPane.showMessageDialog(frame, "Kullanıcı başarıyla güncellendi.");
                LogManager.log("Kullanıcı güncellendi: " + kullaniciAdi);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Kullanıcı bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loglariGoruntule() {
        ArrayList<String> loglar = LogManager.logOku();
        JOptionPane.showMessageDialog(frame, String.join("\n", loglar), "Log Kayıtları", JOptionPane.INFORMATION_MESSAGE);
    }

    private void geriDon() {
        frame.dispose();
        new LoginScreen();
    }

    private void veriGoster() {
        model.setRowCount(0); // Mevcut tabloyu temizle
        toplamKoltukSayisi = FileHandler.seferOku(SEFER_DOSYA); // Seferleri tekrar yükle

        HashMap<String, Integer> doluKoltukSayisi = new HashMap<>();

        // Rezervasyonlardan dolu koltuk sayılarını hesapla
        for (Rezervasyon r : rezervasyonYonetimi.getRezervasyonListesi()) {
            String seferKey = r.getGuzergah() + " - " + r.getTarih() + " - " + r.getSaat();
            doluKoltukSayisi.put(seferKey, doluKoltukSayisi.getOrDefault(seferKey, 0) + 1);
        }

        // Seferleri tabloya ekle
        toplamKoltukSayisi.forEach((seferKey, sefer) -> {
            int doluKoltuk = doluKoltukSayisi.getOrDefault(seferKey, 0); // Dolu koltukları al
            model.addRow(new Object[]{
                    sefer.getGuzergah(),    // Güzergah
                    sefer.getTarih(),       // Tarih
                    sefer.getSaat(),        // Saat
                    sefer.getToplamKoltuk(), // Toplam Koltuk Sayısı
                    doluKoltuk              // Dolu Koltuk Sayısı
            });
        });
    }




}
