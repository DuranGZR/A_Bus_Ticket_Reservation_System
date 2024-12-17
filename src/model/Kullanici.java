package model;

public class Kullanici {
    private String kullaniciAdi;
    private String sifre;
    private String rol;
    private String adSoyad; // Ad Soyad alanını ekliyoruz

    public Kullanici(String kullaniciAdi, String sifre, String rol, String adSoyad) {
        this.kullaniciAdi = kullaniciAdi;
        this.sifre = sifre;
        this.rol = rol;
        this.adSoyad = adSoyad; // Ad Soyad'ı da parametre olarak alıyoruz
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public String getSifre() {
        return sifre;
    }

    public String getRol() {
        return rol;
    }

    public String getAdSoyad() {
        return adSoyad; // Ad Soyad bilgisini döndürüyoruz
    }

    public void setRol(String text) {
        this.rol = text; // "rol" sınıf değişkenini gelen değerle güncelle
    }

    public void setSifre(String text) {
        this.sifre = text; // "sifre" sınıf değişkenini gelen değerle güncelle
    }

    public void setAdSoyad(String yeniAdSoyad) {
        this.adSoyad = yeniAdSoyad; // Ad Soyad'ı güncelliyoruz
    }

    public void setKullaniciAdi(String yeniKullaniciAdi) {
        this.kullaniciAdi = yeniKullaniciAdi; // Kullanıcı adını güncelliyoruz
    }
}
