package com.kutuphane.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.kutuphane.dao.KitapDAO;
import com.kutuphane.dao.KullaniciDAO;
import com.kutuphane.factory.DAOFactory;
import com.kutuphane.model.Kitap;
import com.kutuphane.model.Kullanici;

public class RegressionTestSuite {

    private DAOFactory factory;
    private KitapDAO kitapDAO;
    private KullaniciDAO kullaniciDAO;

    @Before
    public void setUp() {
        factory = DAOFactory.getInstance();
        kitapDAO = factory.createKitapDAO();
        kullaniciDAO = factory.createKullaniciDAO();
    }

    @Test
    public void testKitapIslemleriVeriKaybetmez() {
        // 1. Var olan bir kitabı kontrol et (temel veri kaybı olup olmadığını görmek için)
        Kitap kitap = kitapDAO.kitapGetir("9780000000001");
        assertNotNull("Temel kitap verileri kaybolmamalı", kitap);

        // Kitap bilgilerini kaydet
        String kitapAd = kitap.getKitapAd();
        String yazarAdSoyad = kitap.getYazarAdSoyad();

        // 2. Yeni kitap ekle
        String testIsbn = "9780000000060";

        // Önce var mı kontrol et ve varsa sil
        Kitap eskiKitap = kitapDAO.kitapGetir(testIsbn);
        if (eskiKitap != null) {
            kitapDAO.kitapSil(testIsbn);
        }

        Kitap yeniKitap = new Kitap();
        yeniKitap.setIsbn(testIsbn);
        yeniKitap.setKitapAd("Regresyon Test Kitabı");
        yeniKitap.setSayfaSayisi(300);
        yeniKitap.setTurAd("Test");
        yeniKitap.setYazarAdSoyad("Test Yazarı");

        boolean eklemeBasarili = kitapDAO.kitapEkle(yeniKitap);
        assertTrue("Kitap ekleme başarılı olmalı", eklemeBasarili);

        // 3. Tekrar orijinal kitabı kontrol et (operasyonlar diğer kayıtları etkilememiş olmalı)
        Kitap kontrolKitap = kitapDAO.kitapGetir("9780000000001");
        assertNotNull("Kitap hala erişilebilir olmalı", kontrolKitap);
        assertEquals("Kitap adı değişmemiş olmalı", kitapAd, kontrolKitap.getKitapAd());
        assertEquals("Yazar adı değişmemiş olmalı", yazarAdSoyad, kontrolKitap.getYazarAdSoyad());

        // 4. Temizlik - Test kitabını sil
        kitapDAO.kitapSil(testIsbn);
    }

    @Test
    public void testKullaniciVerileriKaybetmez() {
        // 1. Var olan kullanıcının bilgilerini kontrol et
        Kullanici kullanici = kullaniciDAO.kullaniciGiris("12345678901", "1234");
        assertNotNull("Kullanıcı verileri kaybolmamalı", kullanici);
        assertEquals("Kullanıcı adı doğru olmalı", "Ahmet", kullanici.getUyeAd());
        assertEquals("Kullanıcı soyadı doğru olmalı", "Yilmaz", kullanici.getUyeSoyad());

        // 2. Başka işlemlerden sonra tekrar kontrol et
        kitapDAO.tumKitaplariGetir();

        Kullanici kontrolKullanici = kullaniciDAO.kullaniciGiris("12345678901", "1234");
        assertNotNull("Kullanıcı verileri kaybolmamalı", kontrolKullanici);
        assertEquals("Kullanıcı adı hala doğru olmalı", "Ahmet", kontrolKullanici.getUyeAd());
    }

    @Test
    public void testTumKitapVerileriKaybetmez() {
        // Tüm kitapları getir
        int kitapSayisi = kitapDAO.tumKitaplariGetir().size();
        assertTrue("Kitap sayısı pozitif olmalı", kitapSayisi > 0);

        // Başka işlemler yap
        kitapDAO.kitapGetir("9780000000001");

        // Tekrar tüm kitapları getir ve sayısını kontrol et
        int yeniKitapSayisi = kitapDAO.tumKitaplariGetir().size();
        assertEquals("Kitap sayısı değişmemiş olmalı", kitapSayisi, yeniKitapSayisi);
    }
}