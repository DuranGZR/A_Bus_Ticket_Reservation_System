package data;

import model.Kullanici;
import model.Rezervasyon;
import java.io.*;
import java.util.ArrayList;

public class FileHandler {

    public static ArrayList<Rezervasyon> rezervasyonOku(String dosyaAdi) {
        ArrayList<Rezervasyon> liste = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dosyaAdi))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                liste.add(new Rezervasyon(parts[0], parts[1], parts[2], parts[3], Integer.parseInt(parts[4])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return liste;
    }

    public static void rezervasyonYaz(String dosyaAdi, ArrayList<Rezervasyon> liste) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dosyaAdi))) {
            for (Rezervasyon r : liste) {
                bw.write(r.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Kullanici> kullaniciOku(String dosyaAdi) {
        ArrayList<Kullanici> liste = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dosyaAdi))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                liste.add(new Kullanici(parts[0], parts[1], parts[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return liste;
    }
}
