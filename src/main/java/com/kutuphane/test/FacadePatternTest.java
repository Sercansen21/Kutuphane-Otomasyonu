package com.kutuphane.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.kutuphane.facade.OduncFacade;
import com.kutuphane.model.Odunc;

public class FacadePatternTest {

    private OduncFacade oduncFacade;

    @Before
    public void setUp() {
        oduncFacade = new OduncFacade();

        // Test başlamadan önce kitabı iade et (önceki testten kalan durumu temizle)
        try {
            oduncFacade.kitapIadeEt("9780000000001");
        } catch (Exception e) {
        }
    }

    @Test
    public void testFacadePattern() {
        // Bu test, Facade Pattern uygulamasını test eder

        // Test verileri
        String ogrenciNo = "12345678901";
        String isbn = "9780000000001";

        System.out.println("Test başlıyor...");

        // 1. Kitabın ödünç alınması
        boolean oduncAlmaSonucu = oduncFacade.kitapOduncAl(ogrenciNo, isbn, 14);

        // İlk ödünç alma işlemi başarısız olabilir (kitap zaten ödünç alınmış olabilir)
        // Bu durumda, kitabı iade etmeyi deneyelim ve tekrar ödünç alalım
        if (!oduncAlmaSonucu) {
            System.out.println("Kitap zaten ödünç alınmış, iade etmeyi deneyeceğiz.");
            oduncFacade.kitapIadeEt(isbn);
            oduncAlmaSonucu = oduncFacade.kitapOduncAl(ogrenciNo, isbn, 14);
        }

        assertTrue("Kitap ödünç alınabilmeli", oduncAlmaSonucu);
        System.out.println("1. Facade ile kitap ödünç alma işlemi başarılı.");

        // 2. Kullanıcının ödünç aldığı kitapların listelenmesi
        List<Odunc> kullaniciOduncListesi = oduncFacade.kullaniciOduncListesi(ogrenciNo);
        assertNotNull("Kullanıcının ödünç listesi alınabilmeli", kullaniciOduncListesi);
        assertFalse("Kullanıcının ödünç listesi boş olmamalı", kullaniciOduncListesi.isEmpty());
        System.out.println("2. Facade ile kullanıcının ödünç listesi alındı. Liste boyutu: " + kullaniciOduncListesi.size());

        // Listenin içinde az önce ödünç aldığımız kitap olmalı
        boolean kitapBulundu = false;
        Odunc oduncKitap = null;
        for (Odunc odunc : kullaniciOduncListesi) {
            System.out.println("Listedeki kitap: ISBN=" + odunc.getIsbn() + ", İade edilmiş mi: " + odunc.isIadeEdilmis());
            if (odunc.getIsbn().equals(isbn) && !odunc.isIadeEdilmis()) {
                kitapBulundu = true;
                oduncKitap = odunc;
                System.out.println("3. Ödünç alınan kitap kullanıcının listesinde bulundu: " + odunc.getKitapAd());
                break;
            }
        }
        assertTrue("Ödünç alınan kitap, kullanıcının ödünç listesinde bulunmalı", kitapBulundu);
        assertNotNull("Ödünç alınan kitap nesnesi null olmamalı", oduncKitap);
        assertFalse("Kitap henüz iade edilmemiş olmalı", oduncKitap.isIadeEdilmis());

        // 3. Kitabın iade edilmesi
        boolean iadeEtmeSonucu = oduncFacade.kitapIadeEt(isbn);
        assertTrue("Kitap iade edilebilmeli", iadeEtmeSonucu);
        System.out.println("4. Facade ile kitap iade işlemi başarılı.");

        // 4. İade edilen kitabın durumunu kontrol et
        kullaniciOduncListesi = oduncFacade.kullaniciOduncListesi(ogrenciNo);
        kitapBulundu = false;
        for (Odunc odunc : kullaniciOduncListesi) {
            // İade edilmiş kitapları ara
            System.out.println("İade sonrası listedeki kitap: ISBN=" + odunc.getIsbn() + ", İade edilmiş mi: " + odunc.isIadeEdilmis());
            if (odunc.getIsbn().equals(isbn) && odunc.isIadeEdilmis()) {
                kitapBulundu = true;
                System.out.println("5. İade edilen kitap kullanıcının listesinde 'iade edilmiş' olarak görünüyor.");
                break;
            }
        }
        assertTrue("İade edilen kitap, kullanıcının ödünç listesinde görünmeli", kitapBulundu);

        System.out.println("Facade Pattern testi başarıyla tamamlandı!");
    }
}