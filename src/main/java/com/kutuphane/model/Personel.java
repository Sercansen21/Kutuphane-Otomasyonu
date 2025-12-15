package com.kutuphane.model;

public class Personel {
    private String personelTC;
    private String personelAd;
    private String personelSoyad;
    private String sifre;

    public Personel() {
    }

    public Personel(String personelTC, String personelAd, String personelSoyad, String sifre) {
        this.personelTC = personelTC;
        this.personelAd = personelAd;
        this.personelSoyad = personelSoyad;
        this.sifre = sifre;
    }

    public String getPersonelTC() {
        return personelTC;
    }

    public void setPersonelTC(String personelTC) {
        this.personelTC = personelTC;
    }

    public String getPersonelAd() {
        return personelAd;
    }

    public void setPersonelAd(String personelAd) {
        this.personelAd = personelAd;
    }

    public String getPersonelSoyad() {
        return personelSoyad;
    }

    public void setPersonelSoyad(String personelSoyad) {
        this.personelSoyad = personelSoyad;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    @Override
    public String toString() {
        return "Personel [personelTC=" + personelTC + ", personelAd=" + personelAd + ", personelSoyad=" + personelSoyad + "]";
    }
}