package com.kutuphane.dao;

import java.util.List;

import com.kutuphane.model.Kullanici;

public interface KullaniciDAO {
    List<Kullanici> tumKullanicilariGetir();
    Kullanici kullaniciGetir(String ogrenciNo);
    Kullanici kullaniciGiris(String ogrenciNo, String sifre);
    boolean kullaniciEkle(Kullanici kullanici);
    boolean kullaniciSil(String ogrenciNo);
}