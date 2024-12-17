package service;

import model.Kullanici;

import java.util.ArrayList;

public class KullaniciYonetimi {
    private ArrayList<Kullanici> kullanicilar;

    public KullaniciYonetimi(ArrayList<Kullanici> kullanicilar) {
        this.kullanicilar = kullanicilar;
    }

    public Kullanici kullaniciDogrula(String kullaniciAdi, String sifre) {
        for (Kullanici k : kullanicilar) {
            if (k.getKullaniciAdi().equals(kullaniciAdi) && k.getSifre().equals(sifre)) {
                return k;
            }
        }
        return null;
    }
}
