package com.kutuphane.model;

public class Kitap {
    private String isbn;
    private String kitapAd;
    private int sayfaSayisi;
    private String turNo;
    private String yazarNo;
    private String turAd;
    private String yazarAdSoyad;

    // Default constructor
    public Kitap() {
    }

    // Parametreli constructor
    public Kitap(String isbn, String kitapAd, int sayfaSayisi, String turNo, String yazarNo) {
        this.isbn = isbn;
        this.kitapAd = kitapAd;
        this.sayfaSayisi = sayfaSayisi;
        this.turNo = turNo;
        this.yazarNo = yazarNo;
    }

    public Kitap(String isbn, String kitapAd, int sayfaSayisi, String turNo, String yazarNo, String turAd) {
        this.isbn = isbn;
        this.kitapAd = kitapAd;
        this.sayfaSayisi = sayfaSayisi;
        this.turNo = turNo;
        this.yazarNo = yazarNo;
        this.turAd = turAd;
    }

    // Getter ve Setter metodları
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getKitapAd() {
        return kitapAd;
    }

    public void setKitapAd(String kitapAd) {
        this.kitapAd = kitapAd;
    }

    public int getSayfaSayisi() {
        return sayfaSayisi;
    }

    public void setSayfaSayisi(int sayfaSayisi) {
        this.sayfaSayisi = sayfaSayisi;
    }

    public String getTurNo() {
        return turNo;
    }

    public void setTurNo(String turNo) {
        this.turNo = turNo;
    }

    public String getYazarNo() {
        return yazarNo;
    }

    public void setYazarNo(String yazarNo) {
        this.yazarNo = yazarNo;
    }

    public String getTurAd() {
        return turAd;
    }

    public void setTurAd(String turAd) {
        this.turAd = turAd;
    }

    public String getYazarAdSoyad() {
        return yazarAdSoyad;
    }

    public void setYazarAdSoyad(String yazarAdSoyad) {
        this.yazarAdSoyad = yazarAdSoyad;
    }

    @Override
    public String toString() {
        return "Kitap [isbn=" + isbn + ", kitapAd=" + kitapAd + ", sayfaSayisi=" + sayfaSayisi +
               ", turNo=" + turNo + ", yazarNo=" + yazarNo + ", turAd=" + turAd +
               ", yazarAdSoyad=" + yazarAdSoyad + "]";
    }
}