package data;

public class Sefer {
    private String guzergah;
    private String tarih;
    private String saat;
    private int toplamKoltuk;

    public Sefer(String guzergah, String tarih, String saat, int toplamKoltuk) {
        this.guzergah = guzergah;
        this.tarih = tarih;
        this.saat = saat;
        this.toplamKoltuk = toplamKoltuk;
    }

    // Getter ve Setter metotları
    public String getGuzergah() { return guzergah; }
    public String getTarih() { return tarih; }
    public String getSaat() { return saat; }
    public int getToplamKoltuk() { return toplamKoltuk; }

    @Override
    public String toString() {
        return "Güzergah: " + guzergah +
                ", Tarih: " + tarih +
                ", Saat: " + saat +
                ", Toplam Koltuk: " + toplamKoltuk;
    }

}


