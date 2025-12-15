package com.kutuphane.facade;

import java.sql.Date;
import java.time.LocalDate;

import com.kutuphane.dao.KitapDAO;
import com.kutuphane.dao.KullaniciDAO;
import com.kutuphane.dao.OduncDAO;
import com.kutuphane.factory.DAOFactory;
import com.kutuphane.model.Kitap;
import com.kutuphane.model.Kullanici;
import com.kutuphane.model.Odunc;
import com.kutuphane.observer.KitapTakipYonetici;

/**
 * Facade Pattern uygulaması.
 * Bu sınıf, ödünç alma ve iade işlemlerinin karmaşık detaylarını gizler ve
 * bu işlemleri basitleştirilmiş bir arayüz üzerinden gerçekleştirir.
 *
 * Observer Pattern ile entegre edilmiş, ödünç alma ve iade işlemlerinde observer'ları bilgilendirir.
 */
public class OduncFacade {
    private KitapDAO kitapDAO;
    private KullaniciDAO kullaniciDAO;
    private OduncDAO oduncDAO;
    private KitapTakipYonetici kitapTakipYonetici;

    public OduncFacade() {
        DAOFactory factory = DAOFactory.getInstance();
        this.kitapDAO = factory.createKitapDAO();
        this.kullaniciDAO = factory.createKullaniciDAO();
        this.oduncDAO = factory.createOduncDAO();
        this.kitapTakipYonetici = new KitapTakipYonetici();
    }

    /**
     * Observer eklemek için kullanılır
     * @param observer Eklenecek observer
     */
    public void observerEkle(com.kutuphane.observer.KitapTakipObserver observer) {
        kitapTakipYonetici.observerEkle(observer);
    }

    /**
     * Observer çıkarmak için kullanılır
     * @param observer Çıkarılacak observer
     */
    public void observerCikar(com.kutuphane.observer.KitapTakipObserver observer) {
        kitapTakipYonetici.observerCikar(observer);
    }

    /**
     * Kitap ödünç alma işlemini gerçekleştiren facade metodu.
     *
     * @param ogrenciNo Kitabı ödünç alan öğrencinin numarası
     * @param isbn Ödünç alınan kitabın ISBN numarası
     * @param oduncSuresiGun Kitabın kaç gün için ödünç alındığı
     * @return İşlem başarılı ise true, değilse false
     */
    public boolean kitapOduncAl(String ogrenciNo, String isbn, int oduncSuresiGun) {
        try {
            // Debug bilgisi
            System.out.println("Ödünç alma işlemi başlıyor - Öğrenci No: " + ogrenciNo + ", ISBN: " + isbn);

            // 1. Kullanıcıyı kontrol et
            Kullanici kullanici = kullaniciDAO.kullaniciGetir(ogrenciNo);
            if (kullanici == null) {
                System.out.println("HATA: Kullanıcı bulunamadı!");
                return false;
            }
            System.out.println("Kullanıcı bulundu: " + kullanici.getUyeAd() + " " + kullanici.getUyeSoyad());

            // 2. Kitabı kontrol et
            Kitap kitap = kitapDAO.kitapGetir(isbn);
            if (kitap == null) {
                System.out.println("HATA: Kitap bulunamadı!");
                return false;
            }
            System.out.println("Kitap bulundu: " + kitap.getKitapAd());

         // 3. Kitap zaten ödünç alınmış mı kontrol et
            Odunc mevcutOdunc = oduncDAO.oduncGetir(isbn);
            if (mevcutOdunc != null) {
                // Kitap henüz iade edilmemiş mi kontrol et
                if (mevcutOdunc.getIadeTarihi() == null && mevcutOdunc.getAlinmaSayisi() > 0) {
                    System.out.println("HATA: Bu kitap zaten ödünç alınmış! Ödünç alan: " + mevcutOdunc.getOgrenciNo());
                    return false;
                } else {
                    System.out.println("Kitap daha önce ödünç alınmış ancak iade edilmiş, yeniden ödünç verilebilir.");
                }
            }

            // 4. Ödünç kaydı oluştur
            Odunc odunc = new Odunc();
            odunc.setOgrenciNo(ogrenciNo);
            odunc.setIsbn(isbn);
            odunc.setAlimTarihi(Date.valueOf(LocalDate.now()));

            // Kitap ve kullanıcı bilgilerini de set edelim
            odunc.setKitapAd(kitap.getKitapAd());
            odunc.setKullaniciAd(kullanici.getUyeAd());
            odunc.setKullaniciSoyad(kullanici.getUyeSoyad());

            // İade tarihi hesapla (varsayılan veya parametre)
            LocalDate sonIadeTarihi = LocalDate.now().plusDays(oduncSuresiGun > 0 ? oduncSuresiGun : 14);
            odunc.setSonIadeTarihi(Date.valueOf(sonIadeTarihi));

            // 5. Ödünç kaydını veritabanına ekle
            System.out.println("Veritabanına ödünç kaydı ekleniyor...");
            boolean sonuc = oduncDAO.oduncEkle(odunc);

            if (sonuc) {
                System.out.println("Kitap başarıyla ödünç alındı. Son iade tarihi: " + sonIadeTarihi);

                // 6. Observer'ları bilgilendir
                System.out.println("Observer'lar bilgilendiriliyor...");
                kitapTakipYonetici.kitapOduncAlindiNotify(odunc);
            } else {
                System.out.println("HATA: Ödünç alma işlemi sırasında veritabanı hatası oluştu.");
            }

            return sonuc;
        } catch (Exception e) {
            System.out.println("HATA: Ödünç alma işlemi sırasında bir istisna oluştu: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Kitap iade işlemini gerçekleştiren facade metodu.
     *
     * @param isbn İade edilen kitabın ISBN numarası
     * @return İşlem başarılı ise true, değilse false
     */
    public boolean kitapIadeEt(String isbn) {
        try {
            // 1. Kitabı kontrol et
            Kitap kitap = kitapDAO.kitapGetir(isbn);
            if (kitap == null) {
                System.out.println("Kitap bulunamadı!");
                return false;
            }

            // 2. Kitabın ödünç kaydını bul
            Odunc odunc = oduncDAO.oduncGetir(isbn);
            if (odunc == null) {
                System.out.println("Bu kitap için ödünç kaydı bulunamadı!");
                return false;
            }

            // 3. Kitap zaten iade edilmiş mi kontrol et
            if (odunc.getIadeTarihi() != null || odunc.getAlinmaSayisi() <= 0) {
                System.out.println("Bu kitap zaten iade edilmiş!");
                return false;
            }

            // 4. İade tarihini ayarla ve alınma sayısını 0 yap
            odunc.setIadeTarihi(Date.valueOf(LocalDate.now()));
            odunc.setAlinmaSayisi(0); // Kitap iade edildi

            // 5. Ödünç kaydını güncelle
            boolean sonuc = oduncDAO.oduncGuncelle(odunc);

            if (sonuc) {
                System.out.println("Kitap başarıyla iade edildi.");

                // Gecikme kontrolü
                LocalDate bugun = LocalDate.now();
                LocalDate sonIadeTarihi = odunc.getSonIadeTarihi().toLocalDate();

                if (bugun.isAfter(sonIadeTarihi)) {
                    long gecikmeGun = java.time.temporal.ChronoUnit.DAYS.between(sonIadeTarihi, bugun);
                    System.out.println("Dikkat: Kitap " + gecikmeGun + " gün gecikmeyle iade edildi!");
                }

                // 6. Observer'ları bilgilendir
                kitapTakipYonetici.kitapIadeEdildiNotify(odunc);
            } else {
                System.out.println("İade işlemi sırasında bir hata oluştu.");
            }

            return sonuc;
        } catch (Exception e) {
            System.out.println("İade işlemi sırasında bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Bir kullanıcının ödünç aldığı tüm kitapları listeler.
     *
     * @param ogrenciNo Kullanıcının öğrenci numarası
     * @return Kullanıcının ödünç aldığı kitapların listesi
     */
    public java.util.List<Odunc> kullaniciOduncListesi(String ogrenciNo) {
        return oduncDAO.kullaniciOduncListesi(ogrenciNo);
    }

    /**
     * Tüm ödünç kayıtlarını listeler.
     *
     * @return Tüm ödünç kayıtlarının listesi
     */
    public java.util.List<Odunc> tumOduncKayitlari() {
        return oduncDAO.tumOduncKayitlariniGetir();
    }
}