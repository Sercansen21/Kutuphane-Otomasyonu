package com.kutuphane.observer;

import java.util.ArrayList;
import java.util.List;

import com.kutuphane.model.Odunc;

/**
 * Observer Pattern'deki Concrete Subject sınıfı.
 * Bu sınıf, observer'ları yönetir ve kitap ödünç alma/iade olaylarında onları bilgilendirir.
 */
public class KitapTakipYonetici implements KitapTakipSubject {
    private List<KitapTakipObserver> observers;
    private Odunc sonOduncIslem;
    private boolean islemTipi;

    public KitapTakipYonetici() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void observerEkle(KitapTakipObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("Observer eklendi: " + observer.getClass().getSimpleName());
        }
    }

    @Override
    public void observerCikar(KitapTakipObserver observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
            System.out.println("Observer çıkarıldı: " + observer.getClass().getSimpleName());
        }
    }

    @Override
    public void observerlariHaberdar() {
        for (KitapTakipObserver observer : observers) {
            if (islemTipi) {
                observer.kitapOduncAlindi(sonOduncIslem);
            } else {
                observer.kitapIadeEdildi(sonOduncIslem);
            }
        }
    }

    /**
     * Ödünç alma işlemi gerçekleştiğinde çağrılır
     */
    public void kitapOduncAlindiNotify(Odunc odunc) {
        this.sonOduncIslem = odunc;
        this.islemTipi = true;
        observerlariHaberdar();
    }

    /**
     * İade işlemi gerçekleştiğinde çağrılır
     */
    public void kitapIadeEdildiNotify(Odunc odunc) {
        this.sonOduncIslem = odunc;
        this.islemTipi = false;
        observerlariHaberdar();
    }
}