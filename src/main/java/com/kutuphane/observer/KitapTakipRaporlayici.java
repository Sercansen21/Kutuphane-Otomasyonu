package com.kutuphane.observer;

import java.util.HashMap;
import java.util.Map;

import com.kutuphane.model.Odunc;

/**
 * Kitap ödünç alma ve iade istatistiklerini takip eden gözlemci.
 */
public class KitapTakipRaporlayici implements KitapTakipObserver {
    private Map<String, Integer> kitapOduncSayisi; 
    private Map<String, Integer> kullaniciOduncSayisi;

    public KitapTakipRaporlayici() {
        this.kitapOduncSayisi = new HashMap<>();
        this.kullaniciOduncSayisi = new HashMap<>();
    }

    @Override
    public void kitapOduncAlindi(Odunc odunc) {
        // Kitap ödünç sayısını güncelle
        String isbn = odunc.getIsbn();
        kitapOduncSayisi.put(isbn, kitapOduncSayisi.getOrDefault(isbn, 0) + 1);

        // Kullanıcı ödünç sayısını güncelle
        String ogrenciNo = odunc.getOgrenciNo();
        kullaniciOduncSayisi.put(ogrenciNo, kullaniciOduncSayisi.getOrDefault(ogrenciNo, 0) + 1);

        // Bildirim
        System.out.println("KitapTakipRaporlayici: Kitap ödünç alındı. " +
                "ISBN: " + isbn + ", Öğrenci No: " + ogrenciNo);
    }

    @Override
    public void kitapIadeEdildi(Odunc odunc) {
        // İade işleminde istatistik güncellemeye gerek yok
        System.out.println("KitapTakipRaporlayici: Kitap iade edildi. " +
                "ISBN: " + odunc.getIsbn() + ", Öğrenci No: " + odunc.getOgrenciNo());
    }

    // En çok ödünç alınan kitapları listele
    public void enCokOduncAlinanKitaplar(int limit) {
        System.out.println("\n=== En Çok Ödünç Alınan " + limit + " Kitap ===");
        kitapOduncSayisi.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(limit)
            .forEach(entry -> System.out.println("ISBN: " + entry.getKey() + " - Ödünç Alma Sayısı: " + entry.getValue()));
    }

    // En çok kitap ödünç alan kullanıcıları listele
    public void enCokKitapOduncAlanKullanicilar(int limit) {
        System.out.println("\n=== En Çok Kitap Ödünç Alan " + limit + " Kullanıcı ===");
        kullaniciOduncSayisi.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(limit)
            .forEach(entry -> System.out.println("Öğrenci No: " + entry.getKey() + " - Ödünç Alma Sayısı: " + entry.getValue()));
    }
}