package ui;

import model.Kullanici;
import data.FileHandler;
import data.EncryptionUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LoginScreen {
    private JFrame frame;
    private ArrayList<Kullanici> kullaniciListesi;

    public LoginScreen() {
        kullaniciListesi = FileHandler.kullaniciOku("data/kullanicilar.rez");
        initUI();
    }

    private void initUI() {
        frame = new JFrame("Giriş Ekranı");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout(4, 2));

        JTextField kullaniciAdiField = new JTextField();
        JPasswordField sifreField = new JPasswordField();
        JButton girisButton = new JButton("Giriş Yap");

        panel.add(new JLabel("Kullanıcı Adı:"));
        panel.add(kullaniciAdiField);
        panel.add(new JLabel("Şifre:"));
        panel.add(sifreField);
        panel.add(new JLabel());
        panel.add(girisButton);

        girisButton.addActionListener(e -> {
            String kullaniciAdi = kullaniciAdiField.getText();
            String sifre = EncryptionUtil.hashPassword(new String(sifreField.getPassword()));
            for (Kullanici k : kullaniciListesi) {
                if (k.getKullaniciAdi().equals(kullaniciAdi) && k.getSifre().equals(sifre)) {
                    frame.dispose();
                    if (k.getRol().equals("ADMIN")) {
                        new AdminPanel();
                    } else {
                        new UserPanel();
                    }
                    return;
                }
            }
            JOptionPane.showMessageDialog(frame, "Geçersiz kullanıcı adı veya şifre!");
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}
