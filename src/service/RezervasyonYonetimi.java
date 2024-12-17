package service;

import model.Rezervasyon;
import java.util.ArrayList;

public class RezervasyonYonetimi {
    private ArrayList<Rezervasyon> rezervasyonListesi;

    public RezervasyonYonetimi(ArrayList<Rezervasyon> liste) {
        this.rezervasyonListesi = liste;
    }

    public void ekle(Rezervasyon r) {
        rezervasyonListesi.add(r);
    }

    public void sil(int index) {
        if (index >= 0 && index < rezervasyonListesi.size()) {
            rezervasyonListesi.remove(index);
        }
    }

    public ArrayList<Rezervasyon> getRezervasyonListesi() {
        return rezervasyonListesi;
    }
}
