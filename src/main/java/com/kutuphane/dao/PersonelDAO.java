package com.kutuphane.dao;

import java.util.List;

import com.kutuphane.model.Personel;

public interface PersonelDAO {
    List<Personel> tumPersonelleriGetir();
    Personel personelGetir(String personelTC);
    Personel personelGiris(String personelTC, String sifre);
    boolean personelEkle(Personel personel);
    boolean personelSil(String personelTC);
}