package com.kutuphane.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.kutuphane.dao.OduncDAO;
import com.kutuphane.model.Odunc;
import com.kutuphane.util.DBConnection;

public class OduncDAOImpl implements OduncDAO {
    private Connection connection;

    public OduncDAOImpl() {
        connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public List<Odunc> tumOduncKayitlariniGetir() {
        List<Odunc> oduncListesi = new ArrayList<>();
        String sql = "SELECT o.*, k.kitapad, u.uyead, u.uyesoyad FROM kitaptakipveanaliz o " +
                     "LEFT JOIN kitaplar k ON o.isbn = k.isbn " +
                     "LEFT JOIN kullanici u ON o.ogrencino = u.ogrencino " +
                     "ORDER BY o.alinmatarihi DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Odunc odunc = oduncSonucuOlustur(rs);
                oduncListesi.add(odunc);
            }
        } catch (SQLException e) {
            System.out.println("Ödünç kayıtlarını getirme hatası: " + e.getMessage());
            e.printStackTrace();
        }

        return oduncListesi;
    }

    @Override
    public Odunc oduncGetir(String isbn) {
        Odunc odunc = null;
        // Ödünç kontrolü yaparken tam SQL ifadesini yazdıralım
        String sql = "SELECT o.*, k.kitapad, u.uyead, u.uyesoyad FROM kitaptakipveanaliz o " +
                     "LEFT JOIN kitaplar k ON o.isbn = k.isbn " +
                     "LEFT JOIN kullanici u ON o.ogrencino = u.ogrencino " +
                     "WHERE o.isbn = ? " +
                     "ORDER BY o.alinmatarihi DESC LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);

            System.out.println("Kitap ödünç durumu sorgulanıyor: " + sql + ", param: " + isbn);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    odunc = oduncSonucuOlustur(rs);

                    System.out.println("Kitap ödünç durumu: ISBN=" + odunc.getIsbn() +
                                      "\nalimTarihi=" + odunc.getAlimTarihi() +
                                      "\niadetarihi=" + odunc.getSonIadeTarihi());

                    // Burada iadetarihi ve sonIadeTarihi değerlerinin durumunu görmek için debug bilgileri ekleyelim
                    if (odunc.getIadeTarihi() != null) {
                        System.out.println("Kitap iade edilmiş. İade Tarihi: " + odunc.getIadeTarihi());
                    } else {
                        System.out.println("Kitap henüz iade edilmemiş.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Ödünç kaydı getirme hatası: " + e.getMessage());
            e.printStackTrace();
        }

        return odunc;
    }

    @Override
    public Odunc kitapOduncDurumu(String isbn) {
        Odunc odunc = null;
        // İade edilmemiş ödünç kayıtlarını getir
        String sql = "SELECT o.*, k.kitapad, u.uyead, u.uyesoyad FROM kitaptakipveanaliz o " +
                     "LEFT JOIN kitaplar k ON o.isbn = k.isbn " +
                     "LEFT JOIN kullanici u ON o.ogrencino = u.ogrencino " +
                     "WHERE o.isbn = ? " +
                     "ORDER BY o.kayitno DESC LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    odunc = oduncSonucuOlustur(rs);

                    // İade edilip edilmediğini kontrol et
                    Date iadeTarihi = rs.getDate("iadetarihi");
                    // kitaptakipveanaliz tablosunda sadece iadetarihi var, ama bu sütun
                    // hem beklenen iade tarihini hem de gerçek iade tarihini tutuyor olabilir

                    // Debug bilgisi
                    System.out.println("Kitap ödünç durumu: ISBN=" + isbn);
                    System.out.println("alimTarihi=" + odunc.getAlimTarihi());
                    System.out.println("iadetarihi=" + iadeTarihi);

                    // İade edilip edilmediğini belirleyelim
                    // NOT: Gerçek mantık veritabanınıza göre değişebilir
                    if (iadeTarihi != null) {
                        // iadetarihi özel bir durumu gösteriyorsa
                        // Örneğin: iadetarihi, alimTarihi'nden önce ise bu bir iade işaretidir
                        if (iadeTarihi.before(odunc.getAlimTarihi())) {
                            // Bu özel durum bir iade işaretidir
                            odunc.setIadeTarihi(iadeTarihi);
                        } else {
                            // Bu sadece beklenen son iade tarihidir
                            odunc.setSonIadeTarihi(iadeTarihi);
                            odunc.setIadeTarihi(null); // İade edilmemiş
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Kitap ödünç durumu getirme hatası: " + e.getMessage());
            e.printStackTrace();
        }

        return odunc;
    }

    @Override
    public List<Odunc> kullaniciOduncListesi(String ogrenciNo) {
        List<Odunc> oduncListesi = new ArrayList<>();
        String sql = "SELECT o.*, k.kitapad, u.uyead, u.uyesoyad FROM kitaptakipveanaliz o " +
                     "LEFT JOIN kitaplar k ON o.isbn = k.isbn " +
                     "LEFT JOIN kullanici u ON o.ogrencino = u.ogrencino " +
                     "WHERE o.ogrencino = ? " +
                     "ORDER BY o.alinmatarihi DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ogrenciNo);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Odunc odunc = oduncSonucuOlustur(rs);
                    oduncListesi.add(odunc);
                }
            }
        } catch (SQLException e) {
            System.out.println("Kullanıcı ödünç listesi getirme hatası: " + e.getMessage());
            e.printStackTrace();
        }

        return oduncListesi;
    }


    @Override
    public boolean oduncEkle(Odunc odunc) {
        String sql = "INSERT INTO kitaptakipveanaliz (ogrencino, isbn, alinmatarihi, iadetarihi, alinmasayisi) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, odunc.getOgrenciNo());
            stmt.setString(2, odunc.getIsbn());
            stmt.setDate(3, odunc.getAlimTarihi());
            stmt.setDate(4, odunc.getSonIadeTarihi()); // Son iade tarihini iadetarihi sütununa yazıyoruz
            stmt.setInt(5, 1); // Alınma sayısı

            System.out.println("Executing SQL: " + sql);
            System.out.println("Parameters: [" + odunc.getOgrenciNo() + ", " +
                    odunc.getIsbn() + ", " + odunc.getAlimTarihi() + ", " +
                    odunc.getSonIadeTarihi() + ", 1]");

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        odunc.setOduncId(generatedKeys.getInt(1));
                        System.out.println("Generated ID: " + odunc.getOduncId());
                    }
                }
                return true;
            } else {
                System.out.println("No rows affected, insert failed.");
            }
        } catch (SQLException e) {
            System.out.println("Ödünç ekleme SQL hatası: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean oduncGuncelle(Odunc odunc) {
        // İade işleminde aynı zamanda alınma sayısını da 0 yap
        String sql = "UPDATE kitaptakipveanaliz SET iadetarihi = ?, alinmasayisi = ? WHERE kayitno = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, odunc.getIadeTarihi());
            stmt.setInt(2, odunc.getAlinmaSayisi()); // İade edilirse 0, değilse mevcut değer
            stmt.setInt(3, odunc.getOduncId());

            System.out.println("Executing update SQL: " + sql);
            System.out.println("Parameters: [" + odunc.getIadeTarihi() + ", " + odunc.getAlinmaSayisi() + ", " + odunc.getOduncId() + "]");

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Ödünç güncelleme hatası: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean kitapIadeEt(String isbn) {
        // Kitabın ödünç kaydını bul
        Odunc odunc = kitapOduncDurumu(isbn);
        if (odunc == null) {
            System.out.println("Bu kitap için ödünç kaydı bulunamadı!");
            return false;
        }

        if (odunc.isIadeEdilmis()) {
            System.out.println("Bu kitap zaten iade edilmiş!");
            return false;
        }

        // İade işlemi - burada gerçekleşen işlem, mevcut veritabanı yapınıza göre değişebilir
        // Kitaptakipveanaliz tablosunda gerçek iade işlemi nasıl gösteriliyor?

        // Örnek 1: Yeni bir sütun (gerçek iade tarihi) varsa:
        // String sql = "UPDATE kitaptakipveanaliz SET gercek_iade_tarihi = ? WHERE kayitno = ?";

        // Örnek 2: Özel bir değer kullanıyorsak (bizim durumumuzda, iadetarihi'ni güncelliyoruz):
        String sql = "UPDATE kitaptakipveanaliz SET iadetarihi = ? WHERE kayitno = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // İade tarihi olarak bugünü kullan
            Date bugun = Date.valueOf(LocalDate.now());

            // Bizim özel durumumuzda, iadetarihi alanına özel bir değer koyuyoruz
            // Bu değer, alınma tarihinden 1 gün önceki tarih olsun (iade edilmiş işareti olarak)
            Date ozelIadeTarihi = Date.valueOf(odunc.getAlimTarihi().toLocalDate().minusDays(1));

            stmt.setDate(1, ozelIadeTarihi); // İade edilmiş işareti
            stmt.setInt(2, odunc.getOduncId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Kitap başarıyla iade edildi.");
                return true;
            } else {
                System.out.println("İade işlemi başarısız oldu.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Kitap iade hatası: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean oduncSil(int oduncId) {
        String sql = "DELETE FROM kitaptakipveanaliz WHERE kayitno = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, oduncId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Ödünç silme hatası: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ResultSet'ten Odunc nesnesi oluşturmak için yardımcı metod
 // ResultSet'ten Odunc nesnesi oluşturmak için yardımcı metod
    private Odunc oduncSonucuOlustur(ResultSet rs) throws SQLException {
        Odunc odunc = new Odunc();
        odunc.setOduncId(rs.getInt("kayitno"));
        odunc.setOgrenciNo(rs.getString("ogrencino"));
        odunc.setIsbn(rs.getString("isbn"));
        odunc.setAlimTarihi(rs.getDate("alinmatarihi"));

        // ÖNEMLİ: Veritabanı modelimize göre, iadetarihi sütunu
        // hem son iade tarihi hem de gerçek iade tarihini tutuyor olabilir.
        // Bu durumu açıklığa kavuşturalım:

        // İlk olarak iadetarihi sütununu okuyalım
        Date iadetarihi = rs.getDate("iadetarihi");
        odunc.setSonIadeTarihi(iadetarihi); // Son iade tarihi olarak ata

        // Gerçek iade tarihini belirlemek için başka bir mantık uygulamalıyız
        // Örneğin, alinmasayisi > 0 ise, henüz iade edilmemiş demektir.
        int alinmaSayisi = rs.getInt("alinmasayisi");

        // Eğer kitap henüz iade edilmemişse, iadeTarihi'ni null olarak ayarla
        if (alinmaSayisi > 0) {
            odunc.setIadeTarihi(null); // Kitap henüz iade edilmemiş
        } else {
            // Kitap iade edilmiş, iadetarihi gerçek iade tarihi olarak kabul edilir
            odunc.setIadeTarihi(iadetarihi);
        }

        // İlişkili bilgileri ata
        odunc.setKitapAd(rs.getString("kitapad"));
        odunc.setKullaniciAd(rs.getString("uyead"));
        odunc.setKullaniciSoyad(rs.getString("uyesoyad"));
        odunc.setAlinmaSayisi(alinmaSayisi);

        return odunc;
    }
}