package com.kutuphane.observer;

/**
 * Observer Pattern'deki Subject arayüzü.
 * Bu arayüzü uygulayan sınıflar, observer'ları yönetir ve olaylar gerçekleştiğinde onları bilgilendirir.
 */
public interface KitapTakipSubject {
    /**
     * Observer ekler
     */
    void observerEkle(KitapTakipObserver observer);

    /**
     * Observer çıkarır
     */
    void observerCikar(KitapTakipObserver observer);

    /**
     * Tüm observer'lara bildirim yapar
     */
    void observerlariHaberdar();
}