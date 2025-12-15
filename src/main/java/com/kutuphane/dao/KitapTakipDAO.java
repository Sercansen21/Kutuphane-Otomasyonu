package com.kutuphane.dao;

import java.util.List;

import com.kutuphane.model.KitapTakip;

public interface KitapTakipDAO {
    // Kitabın ödünç durumunu kontrol eder
    boolean kitapOduncDurumu(String isbn);

    // Belirli bir ödünç kaydını bulur
    KitapTakip oduncKaydiniBul(String ogrenciNo, String isbn);

    // Yeni bir ödünç kaydı ekler
    boolean kitapOduncAl(KitapTakip kayit);

    // Kitap iade işlemi yapar
    boolean kitapIadeEt(String ogrenciNo, String isbn);

    // Bir kullanıcının ödünç aldığı kitapları listeler
    List<KitapTakip> kullaniciOduncListesi(String ogrenciNo);

    // Tüm ödünç kayıtlarını listeler
    List<KitapTakip> tumOduncKayitlari();

    // Gecikmiş kitapları listeler
    List<KitapTakip> gecikmisOduncKayitlari();
}