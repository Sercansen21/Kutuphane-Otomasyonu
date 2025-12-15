package com.kutuphane.model;

import java.sql.Date;

public class KitapTakip {
    private int kayitNo;
    private String ogrenciNo;
    private String isbn;
    private Date alinmaTarihi;
    private Date iadeTarihi;
    private int alinmaSayisi;

    // İlişkili nesneler için alanlar (join için)
    private String kitapAd;
    private String yazarAdSoyad;
    private String uyeAdSoyad;

    public KitapTakip() {
    }

    public KitapTakip(int kayitNo, String ogrenciNo, String isbn, Date alinmaTarihi, Date iadeTarihi, int alinmaSayisi) {
        this.kayitNo = kayitNo;
        this.ogrenciNo = ogrenciNo;
        this.isbn = isbn;
        this.alinmaTarihi = alinmaTarihi;
        this.iadeTarihi = iadeTarihi;
        this.alinmaSayisi = alinmaSayisi;
    }

    public int getKayitNo() {
        return kayitNo;
    }

    public void setKayitNo(int kayitNo) {
        this.kayitNo = kayitNo;
    }

    public String getOgrenciNo() {
        return ogrenciNo;
    }

    public void setOgrenciNo(String ogrenciNo) {
        this.ogrenciNo = ogrenciNo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getAlinmaTarihi() {
        return alinmaTarihi;
    }

    public void setAlinmaTarihi(Date alinmaTarihi) {
        this.alinmaTarihi = alinmaTarihi;
    }

    public Date getIadeTarihi() {
        return iadeTarihi;
    }

    public void setIadeTarihi(Date iadeTarihi) {
        this.iadeTarihi = iadeTarihi;
    }

    public int getAlinmaSayisi() {
        return alinmaSayisi;
    }

    public void setAlinmaSayisi(int alinmaSayisi) {
        this.alinmaSayisi = alinmaSayisi;
    }

    public String getKitapAd() {
        return kitapAd;
    }

    public void setKitapAd(String kitapAd) {
        this.kitapAd = kitapAd;
    }

    public String getYazarAdSoyad() {
        return yazarAdSoyad;
    }

    public void setYazarAdSoyad(String yazarAdSoyad) {
        this.yazarAdSoyad = yazarAdSoyad;
    }

    public String getUyeAdSoyad() {
        return uyeAdSoyad;
    }

    public void setUyeAdSoyad(String uyeAdSoyad) {
        this.uyeAdSoyad = uyeAdSoyad;
    }

    @Override
    public String toString() {
        return "KitapTakip [kayitNo=" + kayitNo + ", ogrenciNo=" + ogrenciNo + ", isbn=" + isbn +
               ", alinmaTarihi=" + alinmaTarihi + ", iadeTarihi=" + iadeTarihi + ", alinmaSayisi=" + alinmaSayisi + "]";
    }
}