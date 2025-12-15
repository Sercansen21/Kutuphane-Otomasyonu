package com.kutuphane.model;

import java.sql.Date;

public class Odunc {
    // Veritabanı alanları
    private int oduncId;        // kayitno
    private String ogrenciNo;   // ogrencino
    private String isbn;        // isbn
    private Date alimTarihi;    // alinmatarihi
    private Date sonIadeTarihi; // iadetarihi - son iade tarihi
    private Date iadeTarihi;    // iadetarihi - gerçek iade tarihi (model sınıfında ayrı tutuyoruz)
    private int alinmaSayisi;   // alinmasayisi - eğer > 0 ise henüz iade edilmemiş

    // İlişkisel alanlar
    private String kitapAd;       // kitaplar tablosundan
    private String kullaniciAd;   // kullanici tablosundan
    private String kullaniciSoyad; // kullanici tablosundan

    // Constructor ve diğer metodlar
    public Odunc() {
    }

    public Odunc(int oduncId, String ogrenciNo, String isbn, Date alimTarihi, Date sonIadeTarihi, Date iadeTarihi, int alinmaSayisi) {
        this.oduncId = oduncId;
        this.ogrenciNo = ogrenciNo;
        this.isbn = isbn;
        this.alimTarihi = alimTarihi;
        this.sonIadeTarihi = sonIadeTarihi;
        this.iadeTarihi = iadeTarihi;
        this.alinmaSayisi = alinmaSayisi;
    }

    // Getters ve Setters
    public int getOduncId() {
        return oduncId;
    }

    public void setOduncId(int oduncId) {
        this.oduncId = oduncId;
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

    public Date getAlimTarihi() {
        return alimTarihi;
    }

    public void setAlimTarihi(Date alimTarihi) {
        this.alimTarihi = alimTarihi;
    }

    public Date getSonIadeTarihi() {
        return sonIadeTarihi;
    }

    public void setSonIadeTarihi(Date sonIadeTarihi) {
        this.sonIadeTarihi = sonIadeTarihi;
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

    public String getKullaniciAd() {
        return kullaniciAd;
    }

    public void setKullaniciAd(String kullaniciAd) {
        this.kullaniciAd = kullaniciAd;
    }

    public String getKullaniciSoyad() {
        return kullaniciSoyad;
    }

    public void setKullaniciSoyad(String kullaniciSoyad) {
        this.kullaniciSoyad = kullaniciSoyad;
    }

    // Kitap iade edilmiş mi kontrolü
    public boolean isIadeEdilmis() {
        // Eğer alinmaSayisi 0 ise veya iadeTarihi null değilse, kitap iade edilmiş demektir
        return alinmaSayisi <= 0 || iadeTarihi != null;
    }

    // Kitap gecikmiş mi kontrolü
    public boolean isGecikme() {
        if (!isIadeEdilmis()) {
            // Henüz iade edilmemiş kitaplar için bugünün tarihiyle karşılaştırılır
            return sonIadeTarihi != null && sonIadeTarihi.toLocalDate().isBefore(java.time.LocalDate.now());
        } else {
            // İade edilmiş kitaplar için iade tarihi ile son iade tarihi karşılaştırılır
            return sonIadeTarihi != null && iadeTarihi != null &&
                   iadeTarihi.toLocalDate().isAfter(sonIadeTarihi.toLocalDate());
        }
    }

    @Override
    public String toString() {
        return "Odunc [oduncId=" + oduncId + ", ogrenciNo=" + ogrenciNo + ", isbn=" + isbn +
               ", alimTarihi=" + alimTarihi + ", sonIadeTarihi=" + sonIadeTarihi +
               ", iadeTarihi=" + iadeTarihi + ", alinmaSayisi=" + alinmaSayisi + "]";
    }
}