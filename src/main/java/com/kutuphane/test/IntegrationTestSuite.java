package com.kutuphane.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.kutuphane.dao.KitapDAO;
import com.kutuphane.dao.KullaniciDAO;
import com.kutuphane.dao.PersonelDAO;
import com.kutuphane.factory.DAOFactory;
import com.kutuphane.model.Kitap;
import com.kutuphane.model.Kullanici;
import com.kutuphane.model.Personel;

public class IntegrationTestSuite {

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
    public void testFactoryVeDAOEntegrasyonu() {
        // 1. Factory'den DAO nesnelerini al
        assertNotNull("KitapDAO nesnesi oluşturulabilmeli", kitapDAO);
        assertNotNull("KullaniciDAO nesnesi oluşturulabilmeli", kullaniciDAO);
        assertNotNull("PersonelDAO nesnesi oluşturulabilmeli", personelDAO);

        // 2. DAO nesneleri doğru çalışıyor mu?
        List<Kitap> kitaplar = kitapDAO.tumKitaplariGetir();
        assertNotNull("Kitap listesi alınabilmeli", kitaplar);
        assertFalse("Kitap listesi boş olmamalı", kitaplar.isEmpty());
    }

    @Test
    public void testKullaniciVeKitapEntegrasyonu() {
        // 1. Kullanıcı girişi
        Kullanici kullanici = kullaniciDAO.kullaniciGiris("12345678901", "1234");
        assertNotNull("Kullanıcı giriş yapabilmeli", kullanici);

        // 2. Kullanıcı giriş yaptıktan sonra kitapları görüntüleyebilmeli
        List<Kitap> kitaplar = kitapDAO.tumKitaplariGetir();
        assertFalse("Kitap listesi boş olmamalı", kitaplar.isEmpty());

        // 3. Bir kitabın detayını görüntüleyebilmeli
        String isbn = kitaplar.get(0).getIsbn();
        Kitap kitap = kitapDAO.kitapGetir(isbn);
        assertNotNull("Kitap detayları alınabilmeli", kitap);
        assertEquals("ISBN'ler eşleşmeli", isbn, kitap.getIsbn());
    }

    @Test
    public void testPersonelVeKitapEntegrasyonu() {
        // 1. Personel girişi
        Personel personel = personelDAO.personelGiris("22334455667", "admin1");
        assertNotNull("Personel giriş yapabilmeli", personel);

        // 2. Personel kitap ekleyebilmeli
        String testIsbn = "9780000000070";

        // Önce varsa sil
        Kitap eskiKitap = kitapDAO.kitapGetir(testIsbn);
        if (eskiKitap != null) {
            kitapDAO.kitapSil(testIsbn);
        }

        Kitap yeniKitap = new Kitap();
        yeniKitap.setIsbn(testIsbn);
        yeniKitap.setKitapAd("Entegrasyon Test Kitabı");
        yeniKitap.setSayfaSayisi(400);
        yeniKitap.setTurAd("Test");
        yeniKitap.setYazarAdSoyad("Entegrasyon Yazar");

        boolean eklemeBasarili = kitapDAO.kitapEkle(yeniKitap);
        assertTrue("Kitap ekleme başarılı olmalı", eklemeBasarili);

        // 3. Eklenen kitap kontrol edilebilmeli
        Kitap eklenenKitap = kitapDAO.kitapGetir(testIsbn);
        assertNotNull("Eklenen kitap getirilebilmeli", eklenenKitap);
        assertEquals("Kitap adı eşleşmeli", "Entegrasyon Test Kitabı", eklenenKitap.getKitapAd());

        // 4. Temizlik - Eklenen kitabı sil
        kitapDAO.kitapSil(testIsbn);
    }
}