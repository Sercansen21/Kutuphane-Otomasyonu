package com.kutuphane.dao;

import java.util.List;

import com.kutuphane.model.Odunc;

public interface OduncDAO {
    List<Odunc> tumOduncKayitlariniGetir();
    Odunc oduncGetir(String isbn);
    Odunc kitapOduncDurumu(String isbn); // Kitabın güncel ödünç durumunu getir
    List<Odunc> kullaniciOduncListesi(String ogrenciNo);
    boolean oduncEkle(Odunc odunc);
    boolean oduncGuncelle(Odunc odunc);
    boolean kitapIadeEt(String isbn); // Kitabı iade et (daha basit yöntem)
    boolean oduncSil(int oduncId);
}