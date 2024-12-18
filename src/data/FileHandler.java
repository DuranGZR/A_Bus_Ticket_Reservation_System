package data;

import model.Kullanici;
import model.Rezervasyon;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileHandler {

    public static ArrayList<Rezervasyon> rezervasyonOku(String dosyaAdi) {
        ArrayList<Rezervasyon> rezervasyonlar = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(dosyaAdi))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String adSoyad = parts[0];
                    String tarih = parts[1];
                    String guzergah = parts[2];
                    String saat = parts[3];
                    int koltukNo = Integer.parseInt(parts[4]);

                    rezervasyonlar.add(new Rezervasyon(adSoyad, tarih, guzergah, saat, koltukNo));
                }
            }
        } catch (IOException e) {
            LogManager.log("Dosya okuma hatası: " + dosyaAdi + " - " + e.getMessage());
        }
        return rezervasyonlar;
    }



    public static void rezervasyonYaz(String dosyaAdi, List<Rezervasyon> rezervasyonlar) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dosyaAdi))) {
            for (Rezervasyon r : rezervasyonlar) {
                writer.write(r.getAdSoyad() + "," + r.getTarih() + "," + r.getGuzergah() + "," + r.getSaat() + "," + r.getKoltukNo());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<Kullanici> kullaniciOku(String dosyaAdi) {
        List<Kullanici> kullaniciListesi = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(dosyaAdi))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(","); // Kullanıcı bilgilerini virgülle ayırıyoruz
                if (data.length == 4) { // 4 öğe bekliyoruz (kullanıcı adı, şifre, rol, ad soyad)
                    Kullanici kullanici = new Kullanici(data[0], data[1], data[2], data[3]);
                    kullaniciListesi.add(kullanici);
                } else {
                    System.err.println("Geçersiz veri formatı: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return kullaniciListesi;  // Artık List<Kullanici> dönüyor
    }



    public static void kullaniciYaz(String dosyaAdi, ArrayList<Kullanici> kullaniciListesi) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dosyaAdi))) {
            for (Kullanici k : kullaniciListesi) {
                writer.write(k.getKullaniciAdi() + "," + k.getSifre() + "," + k.getRol() + "," + k.getAdSoyad());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void seferYaz(String dosyaAdi, HashMap<String, Sefer> seferler) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dosyaAdi))) { // Overwrite (üzerine yaz)
            for (String key : seferler.keySet()) {
                Sefer sefer = seferler.get(key);
                bw.write(sefer.getGuzergah() + "," + sefer.getTarih() + "," + sefer.getSaat() + "," + sefer.getToplamKoltuk());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Sefer> seferOku(String dosyaAdi) {
        HashMap<String, Sefer> seferler = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dosyaAdi))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Boş satırları atla

                String[] parts = line.split(",");
                if (parts.length == 4) { // Güzergah, Tarih, Saat, Toplam Koltuk
                    String guzergah = parts[0].trim();
                    String tarih = parts[1].trim();
                    String saat = parts[2].trim();
                    int toplamKoltuk = Integer.parseInt(parts[3].trim());

                    String seferKey = guzergah + " - " + tarih + " - " + saat;
                    seferler.put(seferKey, new Sefer(guzergah, tarih, saat, toplamKoltuk));
                }
            }
        } catch (IOException e) {
            System.err.println("Sefer dosyası okunurken hata oluştu: " + e.getMessage());
        }
        return seferler;
    }









    // Yeni logYaz metodu
    public static void logYaz(String dosyaAdi, String logMesaji) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dosyaAdi, true))) { // Append modunda yaz
            bw.write(logMesaji);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Yeni logOku metodu
    public static ArrayList<String> logOku(String dosyaAdi) {
        ArrayList<String> loglar = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dosyaAdi))) {
            String line;
            while ((line = br.readLine()) != null) {
                loglar.add(line);
            }
        } catch (IOException e) {
            System.out.println("Log dosyası bulunamadı.");
        }
        return loglar;
    }
}
