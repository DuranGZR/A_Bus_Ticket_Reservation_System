package ui;

import model.Kullanici;
import data.FileHandler;
import data.EncryptionUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LoginScreen {
    private JFrame frame;
    private List<Kullanici> kullaniciListesi = new ArrayList<>();

    public LoginScreen() {
        kullaniciListesi = FileHandler.kullaniciOku("data/kullanicilar.rez");
        initUI();
    }

    private void initUI() {
        // Nimbus görünümü kullan
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ana pencere
        frame = new JFrame("Giriş Ekranı");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Ana panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Kenar boşlukları
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Başlık
        JLabel titleLabel = new JLabel("Otobüs Rezervasyon Sistemi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(70, 130, 180));

        // Kullanıcı Adı ve Şifre Alanları
        JLabel kullaniciAdiLabel = new JLabel("Kullanıcı Adı:");
        JTextField kullaniciAdiField = new JTextField();
        kullaniciAdiField.setColumns(15);
        kullaniciAdiField.setFont(new Font("Arial", Font.PLAIN, 14));
        kullaniciAdiField.setForeground(Color.BLACK); // Yazı rengi siyah
        kullaniciAdiField.setBackground(Color.WHITE); // Arkaplan rengi beyaz

        JLabel sifreLabel = new JLabel("Şifre:");
        JPasswordField sifreField = new JPasswordField();
        sifreField.setColumns(15);
        sifreField.setFont(new Font("Arial", Font.PLAIN, 14));
        sifreField.setForeground(Color.BLACK); // Yazı rengi siyah
        sifreField.setBackground(Color.WHITE); // Arkaplan rengi beyaz

        // Butonlar
        JButton girisButton = new JButton("Giriş Yap");
        JButton yeniKullaniciButton = new JButton("Yeni Kullanıcı Ekle");
        JButton yeniAdminButton = new JButton("Yeni Admin Ekle");

        // Buton tasarımları
        girisButton.setBackground(new Color(70, 130, 180));
        girisButton.setForeground(Color.WHITE);
        girisButton.setFont(new Font("Arial", Font.BOLD, 14));

        yeniKullaniciButton.setBackground(new Color(34, 139, 34));
        yeniKullaniciButton.setForeground(Color.WHITE);
        yeniKullaniciButton.setFont(new Font("Arial", Font.BOLD, 14));

        yeniAdminButton.setBackground(new Color(255, 165, 0));
        yeniAdminButton.setForeground(Color.WHITE);
        yeniAdminButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Layout düzeni
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; mainPanel.add(kullaniciAdiLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; mainPanel.add(kullaniciAdiField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; mainPanel.add(sifreLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; mainPanel.add(sifreField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(girisButton, gbc);

        gbc.gridy = 4; mainPanel.add(yeniKullaniciButton, gbc);
        gbc.gridy = 5; mainPanel.add(yeniAdminButton, gbc);

        // Ana Paneli Pencereye Ekle
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        // Giriş Butonu Eventi
        girisButton.addActionListener(e -> {
            String kullaniciAdi = kullaniciAdiField.getText();
            String sifre = EncryptionUtil.hashPassword(new String(sifreField.getPassword()));

            for (Kullanici k : kullaniciListesi) {  // Artık Kullanici türünde döngü
                if (k.getKullaniciAdi().equals(kullaniciAdi) && k.getSifre().equals(sifre)) {
                    frame.dispose();
                    if (k.getRol().equals("ADMIN")) {
                        new AdminPanel();
                    } else {
                        new UserPanel(k);  // Kullanıcı nesnesini parametre olarak gönderiyoruz
                    }
                    return;
                }
            }




            JOptionPane.showMessageDialog(frame, "Geçersiz kullanıcı adı veya şifre!");
        });

        // Yeni Kullanıcı Ekle
        yeniKullaniciButton.addActionListener(e -> kullaniciEkle("USER"));
        yeniAdminButton.addActionListener(e -> kullaniciEkle("ADMIN"));
    }

    private void kullaniciEkle(String rol) {
        JTextField kullaniciAdiField = new JTextField();
        JPasswordField sifreField = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Kullanıcı Adı:"));
        panel.add(kullaniciAdiField);
        panel.add(new JLabel("Şifre:"));
        panel.add(sifreField);

        int result = JOptionPane.showConfirmDialog(frame, panel,
                rol.equals("ADMIN") ? "Yeni Admin Ekle" : "Yeni Kullanıcı Ekle",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String kullaniciAdi = kullaniciAdiField.getText();
            String sifre = EncryptionUtil.hashPassword(new String(sifreField.getPassword()));
            String adSoyad = "";
            Kullanici yeniKullanici = new Kullanici(kullaniciAdi, sifre, rol, adSoyad);

            kullaniciListesi.add(yeniKullanici);
            FileHandler.kullaniciYaz("data/kullanicilar.rez", (ArrayList<Kullanici>) kullaniciListesi);
            JOptionPane.showMessageDialog(frame, rol + " başarıyla eklendi!");
        }
    }
}
