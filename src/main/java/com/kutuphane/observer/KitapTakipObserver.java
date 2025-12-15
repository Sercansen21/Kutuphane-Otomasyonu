package com.kutuphane.observer;

import com.kutuphane.model.Odunc;

/**
 * Observer Pattern'deki Observer arayüzü.
 * Bu arayüzü uygulayan sınıflar, ödünç alma ve iade olaylarını takip edebilir.
 */
public interface KitapTakipObserver {
    
    void kitapOduncAlindi(Odunc odunc);

    
    void kitapIadeEdildi(Odunc odunc);
}