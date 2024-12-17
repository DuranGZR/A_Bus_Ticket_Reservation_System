package ui;

import model.Rezervasyon;
import model.Kullanici;
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
    private Kullanici currentUser; // Giriş yapan kullanıcı

    public UserPanel(Kullanici kullanici) {
        // Giriş yapan kullanıcıyı alıyoruz
        this.currentUser = kullanici;

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
        frame.setSize(600, 450);
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

        // Yeni Butonlar
        JButton yolcuBilgisiButton = new JButton("Yolcu Bilgisi");
        yolcuBilgisiButton.setFont(new Font("Arial", Font.BOLD, 14));
        yolcuBilgisiButton.setBackground(new Color(70, 130, 180));
        yolcuBilgisiButton.setForeground(Color.WHITE);
        yolcuBilgisiButton.addActionListener(e -> yolcuBilgisiGoruntule());

        JButton rezervasyonGecmisiButton = new JButton("Rezervasyon Geçmişi");
        rezervasyonGecmisiButton.setFont(new Font("Arial", Font.BOLD, 14));
        rezervasyonGecmisiButton.setBackground(new Color(70, 130, 180));
        rezervasyonGecmisiButton.setForeground(Color.WHITE);
        rezervasyonGecmisiButton.addActionListener(e -> rezervasyonGecmisiGoruntule());

        JButton cikisButton = new JButton("Çıkış");
        cikisButton.setFont(new Font("Arial", Font.BOLD, 14));
        cikisButton.setBackground(new Color(70, 130, 180));
        cikisButton.setForeground(Color.WHITE);
        cikisButton.addActionListener(e -> frame.dispose());

        // Buton Panelini yerleştirme
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 10, 10)); // 2 satır, 2 sütun
        buttonPanel.add(secButton);
        buttonPanel.add(yolcuBilgisiButton);
        buttonPanel.add(rezervasyonGecmisiButton);
        buttonPanel.add(cikisButton);

        frame.add(label, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
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

    // Yolcu Bilgilerini Göster ve Güncelle
    private void yolcuBilgisiGoruntule() {
        JTextField adSoyadField = new JTextField(currentUser.getAdSoyad());
        JTextField kullaniciAdiField = new JTextField(currentUser.getKullaniciAdi());
        JPasswordField sifreField = new JPasswordField(currentUser.getSifre());

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Ad Soyad:"));
        panel.add(adSoyadField);
        panel.add(new JLabel("Kullanıcı Adı:"));
        panel.add(kullaniciAdiField);
        panel.add(new JLabel("Şifre:"));
        panel.add(sifreField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Yolcu Bilgisi ve Güncelleme", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String yeniAdSoyad = adSoyadField.getText();
            String yeniKullaniciAdi = kullaniciAdiField.getText();
            String yeniSifre = new String(sifreField.getPassword());

            if (!yeniAdSoyad.trim().isEmpty() && !yeniKullaniciAdi.trim().isEmpty() && !yeniSifre.trim().isEmpty()) {
                currentUser.setAdSoyad(yeniAdSoyad);
                currentUser.setKullaniciAdi(yeniKullaniciAdi);
                currentUser.setSifre(yeniSifre);

                // Kullanıcı bilgilerini dosyaya kaydet
                FileHandler.kullaniciYaz("data/kullanicilar.rez", (ArrayList<Kullanici>) List.of(currentUser));
                JOptionPane.showMessageDialog(frame, "Bilgiler başarıyla güncellendi.");
            } else {
                JOptionPane.showMessageDialog(frame, "Tüm alanlar doldurulmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void rezervasyonGecmisiGoruntule() {
        ArrayList<Rezervasyon> rezervasyonlar = rezervasyonYonetimi.getRezervasyonListesi();
        StringBuilder gecmisBilgisi = new StringBuilder("Geçmiş Rezervasyonlar:\n");

        // Geçmiş rezervasyonları sadece giriş yapan kullanıcıya ait olacak şekilde filtrele
        for (Rezervasyon r : rezervasyonlar) {
            if (r.getAdSoyad().equals(currentUser.getAdSoyad())) { // Sadece giriş yapan kullanıcının geçmişi
                gecmisBilgisi.append("Ad Soyad: ").append(r.getAdSoyad())
                        .append(", Güzergah: ").append(r.getGuzergah())
                        .append(", Tarih: ").append(r.getTarih())
                        .append(", Saat: ").append(r.getSaat())
                        .append(", Koltuk No: ").append(r.getKoltukNo()).append("\n");
            }
        }

        // Eğer geçmiş rezervasyon yoksa
        if (gecmisBilgisi.length() == "Geçmiş Rezervasyonlar:\n".length()) {
            gecmisBilgisi.append("Geçmiş rezervasyon bulunmamaktadır.");
        }

        JOptionPane.showMessageDialog(frame, gecmisBilgisi.toString(), "Geçmiş Rezervasyonlar", JOptionPane.INFORMATION_MESSAGE);
    }


}
