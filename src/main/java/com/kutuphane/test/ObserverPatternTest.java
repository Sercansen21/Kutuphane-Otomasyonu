package com.kutuphane.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.kutuphane.facade.OduncFacade;
import com.kutuphane.observer.GecikmeTakipci;
import com.kutuphane.observer.KitapTakipRaporlayici;

public class ObserverPatternTest {

    private OduncFacade oduncFacade;
    private KitapTakipRaporlayici raporlayici;
    private GecikmeTakipci gecikmeTakipci;

    @Before
    public void setUp() {
        oduncFacade = new OduncFacade();

        // Observer'ları oluştur
        raporlayici = new KitapTakipRaporlayici();
        gecikmeTakipci = new GecikmeTakipci();

        // Observer'ları facade'a ekle
        oduncFacade.observerEkle(raporlayici);
        oduncFacade.observerEkle(gecikmeTakipci);
    }

    @Test
    public void testObserverPattern() {
        System.out.println("\n===== OBSERVER PATTERN TEST =====\n");

        // Test verileri - Veritabanınızda bulunan gerçek değerleri kullanın
        String ogrenciNo1 = "12345678901"; 
        String isbn1 = "9780000000001"; 

        // 1. Kitap ödünç alma işlemi
        System.out.println("1. Kitap ödünç alınıyor...");
        boolean oduncAlmaSonucu1 = oduncFacade.kitapOduncAl(ogrenciNo1, isbn1, 14);
        if (!oduncAlmaSonucu1) {
            System.out.println("Kitap zaten ödünç alınmış olabilir, önce iade ediyoruz...");
            oduncFacade.kitapIadeEt(isbn1);
            System.out.println("Şimdi tekrar ödünç almayı deniyoruz...");
            oduncAlmaSonucu1 = oduncFacade.kitapOduncAl(ogrenciNo1, isbn1, 14);
        }
        assertTrue("Kitap ödünç alınabilmeli", oduncAlmaSonucu1);

        // 2. Kitap iade işlemi
        System.out.println("\n2. Kitap iade ediliyor...");
        boolean iadeEtmeSonucu1 = oduncFacade.kitapIadeEt(isbn1);
        assertTrue("Kitap iade edilebilmeli", iadeEtmeSonucu1);

        // 3. Raporları göster
        raporlayici.enCokOduncAlinanKitaplar(5);
        raporlayici.enCokKitapOduncAlanKullanicilar(5);

        // 4. Gecikme raporunu göster
        gecikmeTakipci.gecikmisBorlariGoster();

        System.out.println("\n===== OBSERVER PATTERN TEST TAMAMLANDI =====");
    }
}