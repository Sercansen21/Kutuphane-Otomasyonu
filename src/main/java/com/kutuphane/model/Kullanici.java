package com.kutuphane.model;

public class Kullanici {
    private String ogrenciNo;
    private String uyeAd;
    private String uyeSoyad;
    private String sifre;
    private String iletisim;

    // Default constructor
    public Kullanici() {
    }

    // Parametreli constructor
    public Kullanici(String ogrenciNo, String uyeAd, String uyeSoyad, String sifre, String iletisim) {
        this.ogrenciNo = ogrenciNo;
        this.uyeAd = uyeAd;
        this.uyeSoyad = uyeSoyad;
        this.sifre = sifre;
        this.iletisim = iletisim;
    }

    // Getter ve Setter metodları
    public String getOgrenciNo() {
        return ogrenciNo;
    }

    public void setOgrenciNo(String ogrenciNo) {
        this.ogrenciNo = ogrenciNo;
    }

    public String getUyeAd() {
        return uyeAd;
    }

    public void setUyeAd(String uyeAd) {
        this.uyeAd = uyeAd;
    }

    public String getUyeSoyad() {
        return uyeSoyad;
    }

    public void setUyeSoyad(String uyeSoyad) {
        this.uyeSoyad = uyeSoyad;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getIletisim() {
        return iletisim;
    }

    public void setIletisim(String iletisim) {
        this.iletisim = iletisim;
    }

    @Override
    public String toString() {
        return "Kullanici [ogrenciNo=" + ogrenciNo + ", uyeAd=" + uyeAd + ", uyeSoyad=" + uyeSoyad +
               ", iletisim=" + iletisim + "]";
    }
}