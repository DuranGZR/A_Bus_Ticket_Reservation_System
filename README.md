# 🚌 Otobüs Bileti Rezervasyon Sistemi

## 📌 Proje Hakkında

**Otobüs Bileti Rezervasyon Sistemi**, Java dili kullanılarak geliştirilmiş bir masaüstü uygulamasıdır. Bu sistem, kullanıcıların otobüs seferlerine bilet rezervasyonu yapmasına, mevcut koltuk durumlarını görmesine ve yolcu bilgilerini yönetmesine olanak tanır.

Nesne Yönelimli Programlama (OOP) prensiplerine uygun şekilde tasarlanmış olan bu proje, veri yapıları ve dosya tabanlı veri kaydetme yöntemleri kullanarak esnek ve kalıcı bir yapı sunar.

---

## 🎯 Temel Özellikler

- Kullanıcı dostu **grafik arayüz** (Swing kullanılarak geliştirilmiştir)
- **Otobüs Yönetimi**: Yeni otobüs ekleme, kaldırma ve listeleme
- **Yolcu Yönetimi**: Yolcu bilgilerini kaydetme, arama ve güncelleme
- **Koltuk Rezervasyonu**: Mevcut koltuk durumlarını görüntüleme ve rezervasyon yapma
- **Dosya Tabanlı Veri Saklama**: Veriler uygulama kapansa dahi korunur
- **Koltuk Görselleştirmesi**: Koltukların dolu/boş durumları görsel olarak gösterilir
- **Hızlı Arama Fonksiyonu**: Sefer veya yolcu bilgilerine hızlıca erişme imkanı

---

## 🛠️ Kullanılan Teknolojiler

- **Java (JDK 11+)**
- **Swing (GUI Bileşenleri)**
- **Dosya İşlemleri (File I/O)**
- **Nesne Yönelimli Programlama (OOP)**

---

## 📂 Proje Klasör Yapısı

```plaintext
src/
├── Main.java                  # Uygulamanın başlangıç noktası
├── models/
│   ├── Bus.java                # Otobüs sınıfı ve koltuk yönetimi
│   ├── Passenger.java          # Yolcu bilgilerini tutan sınıf
│   └── Reservation.java        # Rezervasyon detaylarını içeren sınıf
├── services/
│   ├── ReservationService.java # Rezervasyon işlemlerinin yönetimi
│   └── DataHandler.java        # Dosya okuma/yazma fonksiyonları
└── ui/
    ├── MainFrame.java          # Ana arayüz ekranı
    └── SeatSelectionPanel.java # Koltuk seçme ekranı

