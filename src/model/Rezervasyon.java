package model;

public class Rezervasyon {
    private String adSoyad;
    private String tarih;
    private String guzergah;
    private String saat;
    private int koltukNo;

    public Rezervasyon(String adSoyad, String tarih, String guzergah, String saat, int koltukNo) {
        this.adSoyad = adSoyad;
        this.tarih = tarih;
        this.guzergah = guzergah;
        this.saat = saat;
        this.koltukNo = koltukNo;
    }

    public String getAdSoyad() { return adSoyad; }
    public String getTarih() { return tarih; }
    public String getGuzergah() { return guzergah; }
    public String getSaat() { return saat; }
    public int getKoltukNo() { return koltukNo; }

    @Override
    public String toString() {
        return adSoyad + "," + tarih + "," + guzergah + "," + saat + "," + koltukNo;
    }

    // Ad Soyad'ı güncelleme metodu
    public void setAdSoyad(String adSoyad) {
        this.adSoyad = adSoyad;
    }

    // Koltuk numarasını güncelleme metodu
    public void setKoltukNo(int koltukNo) {
        this.koltukNo = koltukNo;
    }
}
