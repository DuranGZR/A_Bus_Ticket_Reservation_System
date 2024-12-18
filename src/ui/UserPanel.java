package ui;

import model.Rezervasyon;
import model.Kullanici;
import service.RezervasyonYonetimi;
import data.FileHandler;
import data.LogManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(new Color(70, 130, 180));

        // Seferleri Listeleyen JList
        DefaultListModel<String> listModel = new DefaultListModel<>();
        seferListesi.forEach(listModel::addElement);
        JList<String> seferJList = new JList<>(listModel);
        seferJList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        seferJList.setSelectionBackground(new Color(173, 216, 230));
        seferJList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(seferJList);

        // Butonlar
        JButton secButton = createStyledButton("Sefer Seç ve Boş Koltukları Göster");
        secButton.addActionListener(e -> {
            String secilenSefer = seferJList.getSelectedValue();
            if (secilenSefer != null) {
                bosKoltukSecimEkrani(secilenSefer);
            } else {
                JOptionPane.showMessageDialog(frame, "Lütfen bir sefer seçin!");
            }
        });

        JButton yolcuBilgisiButton = createStyledButton("Yolcu Bilgisi");
        yolcuBilgisiButton.addActionListener(e -> yolcuBilgisiGoruntule());

        JButton rezervasyonGecmisiButton = createStyledButton("Rezervasyon Geçmişi");
        rezervasyonGecmisiButton.addActionListener(e -> rezervasyonGecmisiGoruntule());

        JButton cikisButton = createStyledButton("Çıkış");
        cikisButton.addActionListener(e -> geriDon());

        // Buton Panelini yerleştirme
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(secButton);
        buttonPanel.add(yolcuBilgisiButton);
        buttonPanel.add(rezervasyonGecmisiButton);
        buttonPanel.add(cikisButton);

        frame.add(label, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(65, 105, 225));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 144, 255), 3),
                BorderFactory.createEmptyBorder(15, 30, 15, 30)
        ));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237)); // Hover rengi
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(65, 105, 225));
            }
        });

        return button;
    }


    private void geriDon() {
        frame.dispose();
        new LoginScreen();
    }
    private int toplamKoltukSayisiniGetir(String secilenSefer) {
        return FileHandler.seferOku("data/seferler.rez").entrySet().stream()
                .filter(entry -> {
                    String seferKey = entry.getKey();
                    return seferKey.equals(secilenSefer);
                })
                .map(entry -> entry.getValue().getToplamKoltuk()) // Sefer nesnesinden toplam koltuk al
                .findFirst()
                .orElse(25); // Varsayılan değer 25 (bulunmazsa)
    }


    private void bosKoltukSecimEkrani(String secilenSefer) {
        JFrame koltukFrame = new JFrame("Sefer: " + secilenSefer + " - Boş Koltuk Seçimi");
        koltukFrame.setSize(400, 400);

        // Toplam koltuk sayısını getir
        int toplamKoltuk = toplamKoltukSayisiniGetir(secilenSefer);
        koltukFrame.setLayout(new GridLayout((int) Math.ceil(toplamKoltuk / 5.0), 5));
        // Dinamik grid layout

        HashSet<Integer> doluKoltuklar = doluKoltuklariGetir(secilenSefer);
        // Kullanıcı ad soyad bilgilerini yükle
        HashSet<String> mevcutAdSoyadlar = new HashSet<>();
        ArrayList<Kullanici> kullaniciListesi = (ArrayList<Kullanici>) FileHandler.kullaniciOku("data/kullanicilar.rez");
        for (Kullanici kullanici : kullaniciListesi) {
            mevcutAdSoyadlar.add(kullanici.getAdSoyad().trim().toLowerCase()); // Küçük harf normalizasyonu
        }

        for (int i = 1; i <= toplamKoltuk; i++) {
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
                        adSoyad = adSoyad.trim().toLowerCase(); // Kullanıcıdan girilen adı normalize et

                        // Ad Soyad doğrulama
                        if (!mevcutAdSoyadlar.contains(adSoyad)) {
                            JOptionPane.showMessageDialog(koltukFrame,
                                    "Girilen Ad Soyad kayıtlı kullanıcılar arasında bulunmuyor!",
                                    "Hata", JOptionPane.ERROR_MESSAGE);
                            return; // İşlemi iptal et
                        }

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
        return FileHandler.seferOku("data/seferler.rez").values().stream()
                .map(sefer -> sefer.getGuzergah() + " - "
                        + sefer.getTarih() + " - "
                        + sefer.getSaat()
                        + " - Koltuk Sayısı: " + sefer.getToplamKoltuk())
                .collect(Collectors.toList());
    }







    private HashSet<Integer> doluKoltuklariGetir(String secilenSefer) {
        HashSet<Integer> doluKoltuklar = new HashSet<>();

        // Sefer adını "Koltuk Sayısı" olmadan düzgün şekilde parçala
        String[] seferParcalari = secilenSefer.split(" - Koltuk Sayısı:");
        String duzgunSefer = seferParcalari[0].trim(); // Güzergah, Tarih ve Saat

        for (Rezervasyon r : rezervasyonYonetimi.getRezervasyonListesi()) {
            // Rezervasyondaki sefer key'i oluştur
            String seferKey = r.getGuzergah() + " - " + r.getTarih() + " - " + r.getSaat();

            if (seferKey.equals(duzgunSefer)) {
                doluKoltuklar.add(r.getKoltukNo()); // Dolu koltukları ekle
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

    // Rezervasyon geçmişini gösteren method
    private void rezervasyonGecmisiGoruntule() {
        ArrayList<Rezervasyon> rezervasyonlar = rezervasyonYonetimi.getRezervasyonListesi();
        StringBuilder gecmisBilgisi = new StringBuilder("Geçmiş Rezervasyonlar:\n");

        for (Rezervasyon r : rezervasyonlar) {
            if (r.getAdSoyad().equals(currentUser.getAdSoyad())) { // Kullanıcı adı üzerinden kontrol
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
