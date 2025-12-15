package com.kutuphane.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.kutuphane.dao.KitapDAO;
import com.kutuphane.dao.KullaniciDAO;
import com.kutuphane.dao.PersonelDAO;
import com.kutuphane.factory.DAOFactory;
import com.kutuphane.model.Kitap;
import com.kutuphane.model.Kullanici;
import com.kutuphane.model.Personel;

public class UnitTestSuite {

    private DAOFactory factory;
    private KitapDAO kitapDAO;
    private KullaniciDAO kullaniciDAO;
    private PersonelDAO personelDAO;

    @Before
    public void setUp() {
        factory = DAOFactory.getInstance();
        kitapDAO = factory.createKitapDAO();
        kullaniciDAO = factory.createKullaniciDAO();
        personelDAO = factory.createPersonelDAO();
    }

    // ===== Factory Pattern Testleri =====

    @Test
    public void testFactorySingleton() {
        // Factory'nin singleton olarak çalışıp çalışmadığını kontrol et
        DAOFactory anotherFactory = DAOFactory.getInstance();
        assertSame("Factory nesneleri aynı olmalı", factory, anotherFactory);
    }

    @Test
    public void testFactoryCreateObjects() {
        // Factory'nin nesneleri düzgün bir şekilde oluşturup oluşturmadığını kontrol et
        assertNotNull("KitapDAO null olmamalı", kitapDAO);
        assertNotNull("KullaniciDAO null olmamalı", kullaniciDAO);
        assertNotNull("PersonelDAO null olmamalı", personelDAO);
    }

    // ===== KitapDAO Testleri =====

    @Test
    public void testKitapListeleme() {
        // Tüm kitapları getirme
        assertNotNull("Kitap listesi null olmamalı", kitapDAO.tumKitaplariGetir());
        assertTrue("Kitap listesi boş olmamalı", kitapDAO.tumKitaplariGetir().size() > 0);
    }

    @Test
    public void testVarOlanKitapGetirme() {
        // Var olan bir kitabı getirme
        String isbn = "9780000000001"; // Var olan bir ISBN
        Kitap kitap = kitapDAO.kitapGetir(isbn);
        assertNotNull("Var olan kitap null olmamalı", kitap);
        assertEquals("ISBN eşleşmeli", isbn, kitap.getIsbn());
    }

    @Test
    public void testOlmayanKitapGetirme() {
        // Olmayan bir kitabı getirme
        String olmayanIsbn = "1111111111111";
        Kitap olmayanKitap = kitapDAO.kitapGetir(olmayanIsbn);
        assertNull("Olmayan kitap null olmalı", olmayanKitap);
    }

    @Test
    public void testKitapEklemeVeSilme() {
        // Yeni kitap ekleme
        String testIsbn = "9780000000050";

        // Önce var mı kontrol et ve varsa sil
        Kitap eskiKitap = kitapDAO.kitapGetir(testIsbn);
        if (eskiKitap != null) {
            kitapDAO.kitapSil(testIsbn);
        }

        Kitap yeniKitap = new Kitap();
        yeniKitap.setIsbn(testIsbn);
        yeniKitap.setKitapAd("Test Kitabı");
        yeniKitap.setSayfaSayisi(200);
        yeniKitap.setTurAd("Test");
        yeniKitap.setYazarAdSoyad("Test Yazar");

        boolean eklemeBasarili = kitapDAO.kitapEkle(yeniKitap);
        assertTrue("Kitap ekleme başarılı olmalı", eklemeBasarili);

        // Eklenen kitabı kontrol et
        Kitap eklenenKitap = kitapDAO.kitapGetir(testIsbn);
        assertNotNull("Eklenen kitap null olmamalı", eklenenKitap);
        assertEquals("Kitap adı eşleşmeli", "Test Kitabı", eklenenKitap.getKitapAd());

        // Kitabı sil
        boolean silmeBasarili = kitapDAO.kitapSil(testIsbn);
        assertTrue("Kitap silme başarılı olmalı", silmeBasarili);

        // Silindiğini kontrol et
        Kitap silinenKitap = kitapDAO.kitapGetir(testIsbn);
        assertNull("Silinen kitap null olmalı", silinenKitap);
    }

    // ===== KullaniciDAO Testleri =====

    @Test
    public void testDogruKullaniciGirisi() {
        // Doğru kullanıcı adı ve şifre
        Kullanici kullanici = kullaniciDAO.kullaniciGiris("12345678901", "1234");
        assertNotNull("Doğru giriş bilgilerinde kullanıcı null olmamalı", kullanici);
        assertEquals("Kullanıcı adı eşleşmeli", "Ahmet", kullanici.getUyeAd());
    }

    @Test
    public void testYanlisKullaniciGirisi() {
        // Yanlış şifre
        Kullanici yanlisKullanici = kullaniciDAO.kullaniciGiris("12345678901", "yanlis");
        assertNull("Yanlış şifrede kullanıcı null olmalı", yanlisKullanici);

        // Olmayan kullanıcı
        Kullanici olmayanKullanici = kullaniciDAO.kullaniciGiris("99999999999", "sifre");
        assertNull("Olmayan kullanıcı null olmalı", olmayanKullanici);
    }

    // ===== PersonelDAO Testleri =====

    @Test
    public void testPersonelGirisi() {
        // Doğru TC ve şifre
        Personel personel = personelDAO.personelGiris("22334455667", "admin1");
        assertNotNull("Doğru giriş bilgilerinde personel null olmamalı", personel);
        assertEquals("Personel adı eşleşmeli", "Murat", personel.getPersonelAd());

        // Yanlış şifre
        Personel yanlisPersonel = personelDAO.personelGiris("22334455667", "yanlis");
        assertNull("Yanlış şifrede personel null olmalı", yanlisPersonel);
    }
}