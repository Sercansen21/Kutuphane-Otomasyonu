package com.kutuphane.dao;

import java.util.List;

import com.kutuphane.model.Kitap;

public interface KitapDAO {
    List<Kitap> tumKitaplariGetir();
    Kitap kitapGetir(String isbn);
    boolean kitapEkle(Kitap kitap);
    boolean kitapGuncelle(Kitap kitap);
    boolean kitapSil(String isbn);
    boolean kitapOduncAlindiOlarakIsaretle(String isbn);
    boolean kitapIadeEdildiOlarakIsaretle(String isbn);
}