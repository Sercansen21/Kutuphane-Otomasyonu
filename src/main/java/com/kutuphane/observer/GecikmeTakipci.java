package com.kutuphane.observer;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.kutuphane.model.Odunc;

/**
 * Geciken kitapları takip eden gözlemci.
 */
public class GecikmeTakipci implements KitapTakipObserver {
    private List<Odunc> gecikmisBorclar;

    public GecikmeTakipci() {
        this.gecikmisBorclar = new ArrayList<>();
    }

    @Override
    public void kitapOduncAlindi(Odunc odunc) {
        System.out.println("GecikmeTakipci: Yeni ödünç kaydı alındı. " +
                "Son iade tarihi: " + odunc.getSonIadeTarihi());
    }

    @Override
    public void kitapIadeEdildi(Odunc odunc) {
        // İade edilen kitap gecikmişse borç hesapla
        if (odunc.getSonIadeTarihi() != null && odunc.getIadeTarihi() != null) {
            LocalDate sonIadeTarihi = odunc.getSonIadeTarihi().toLocalDate();
            LocalDate iadeTarihi = odunc.getIadeTarihi().toLocalDate();

            if (iadeTarihi.isAfter(sonIadeTarihi)) {
                long gunFarki = ChronoUnit.DAYS.between(sonIadeTarihi, iadeTarihi);
                double borcMiktari = gunFarki * 1.5; // Her gün için 1.5 TL

                System.out.println("GecikmeTakipci: Kitap " + gunFarki +
                        " gün gecikmeyle iade edildi. Borç: " + borcMiktari + " TL");

                // Geciken ödünç listesine ekle
                gecikmisBorclar.add(odunc);
            } else {
                System.out.println("GecikmeTakipci: Kitap zamanında iade edildi.");
            }
        }
    }

    // Gecikmiş borçları listele
    public void gecikmisBorlariGoster() {
        System.out.println("\n=== Gecikmiş Borçlar ===");
        if (gecikmisBorclar.isEmpty()) {
            System.out.println("Gecikmiş borç bulunmuyor.");
            return;
        }

        for (Odunc odunc : gecikmisBorclar) {
            LocalDate sonIadeTarihi = odunc.getSonIadeTarihi().toLocalDate();
            LocalDate iadeTarihi = odunc.getIadeTarihi().toLocalDate();
            long gunFarki = ChronoUnit.DAYS.between(sonIadeTarihi, iadeTarihi);
            double borcMiktari = gunFarki * 1.5;

            System.out.println("Öğrenci No: " + odunc.getOgrenciNo() +
                    ", ISBN: " + odunc.getIsbn() +
                    ", Gecikme: " + gunFarki + " gün" +
                    ", Borç: " + borcMiktari + " TL");
        }
    }
}