package com.kutuphane.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.kutuphane.dao.KitapDAO;
import com.kutuphane.dao.KullaniciDAO;
import com.kutuphane.dao.PersonelDAO;
import com.kutuphane.factory.DAOFactory;
import com.kutuphane.model.Kitap;
import com.kutuphane.model.Personel;

public class SystemTestSuite {

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

    @Test
    public void testTamSistemDongusu() {
        // Bu test, temel kitap işlemlerini kapsayan bir test

        // 1. Personel Girişi Yapma
        Personel personel = personelDAO.personelGiris("22334455667", "admin1");
        assertNotNull("Personel giriş yapabilmeli", personel);

        // 2. Kitapları Listeleme
        List<Kitap> kitaplar = kitapDAO.tumKitaplariGetir();
        assertNotNull("Kitap listesi alınabilmeli", kitaplar);
        assertFalse("Kitap listesi boş olmamalı", kitaplar.isEmpty());

        // 3. Personel Yeni Kitap Ekleyebilmeli
        String testIsbn = "9780000000080";

        // Önce varsa sil
        Kitap eskiKitap = kitapDAO.kitapGetir(testIsbn);
        if (eskiKitap != null) {
            kitapDAO.kitapSil(testIsbn);
        }
        Kitap yeniKitap = new Kitap();
        yeniKitap.setIsbn(testIsbn);
        yeniKitap.setKitapAd("Sistem Test Kitabı");
        yeniKitap.setSayfaSayisi(500);
        yeniKitap.setTurAd("Test");
        yeniKitap.setYazarAdSoyad("Sistem Test Yazarı");

        boolean kitapEklemeBasarili = kitapDAO.kitapEkle(yeniKitap);
        assertTrue("Kitap ekleme başarılı olmalı", kitapEklemeBasarili);

        // 4. Eklenen kitap kontrol edilebilmeli
        Kitap bulunanKitap = kitapDAO.kitapGetir(testIsbn);
        assertNotNull("Yeni eklenen kitap bulunabilmeli", bulunanKitap);
        assertEquals("Kitap adı doğru olmalı", "Sistem Test Kitabı", bulunanKitap.getKitapAd());

        // 5. Kitap güncellenebilmeli
        bulunanKitap.setKitapAd("Güncellenmiş Sistem Test Kitabı");
        boolean guncellemeBasarili = kitapDAO.kitapGuncelle(bulunanKitap);
        assertTrue("Kitap güncelleme başarılı olmalı", guncellemeBasarili);

        // 6. Güncellenen kitap kontrol edilebilmeli
        Kitap guncelKitap = kitapDAO.kitapGetir(testIsbn);
        assertNotNull("Güncellenen kitap bulunabilmeli", guncelKitap);
        assertEquals("Güncellenen kitap adı doğru olmalı", "Güncellenmiş Sistem Test Kitabı", guncelKitap.getKitapAd());

        // 7. Kitap silinebilmeli
        boolean silmeBasarili = kitapDAO.kitapSil(testIsbn);
        assertTrue("Kitap silme başarılı olmalı", silmeBasarili);

        // 8. Silinen kitap kontrol edilebilmeli
        Kitap silinenKitap = kitapDAO.kitapGetir(testIsbn);
        assertNull("Silinen kitap bulunamaz olmalı", silinenKitap);
    }
}