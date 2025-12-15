package com.kutuphane.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kutuphane.dao.KitapTakipDAO;
import com.kutuphane.model.KitapTakip;
import com.kutuphane.util.DBConnection;

public class KitapTakipDAOImpl implements KitapTakipDAO {
    private Connection connection;

    public KitapTakipDAOImpl() {
        connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public boolean kitapOduncDurumu(String isbn) {
        String sql = "SELECT COUNT(*) FROM kitaptakipveanaliz WHERE isbn = ? AND iadetarihi > CURRENT_DATE";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Eğer kitap ödünçteyse true döner
                }
            }
        } catch (SQLException e) {
            System.out.println("Kitap ödünç durumu kontrolü hatası: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public KitapTakip oduncKaydiniBul(String ogrenciNo, String isbn) {
        String sql = "SELECT * FROM kitaptakipveanaliz " +
                     "WHERE ogrencino = ? AND isbn = ? AND iadetarihi > CURRENT_DATE";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ogrenciNo);
            stmt.setString(2, isbn);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    KitapTakip kayit = new KitapTakip();
                    kayit.setKayitNo(rs.getInt("kayitno"));
                    kayit.setOgrenciNo(rs.getString("ogrencino"));
                    kayit.setIsbn(rs.getString("isbn"));
                    kayit.setAlinmaTarihi(rs.getDate("alinmatarihi"));
                    kayit.setIadeTarihi(rs.getDate("iadetarihi"));
                    kayit.setAlinmaSayisi(rs.getInt("alinmasayisi"));
                    return kayit;
                }
            }
        } catch (SQLException e) {
            System.out.println("Ödünç kaydı bulma hatası: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean kitapOduncAl(KitapTakip kayit) {
        String sql = "INSERT INTO kitaptakipveanaliz (ogrencino, isbn, alinmatarihi, iadetarihi, alinmasayisi) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, kayit.getOgrenciNo());
            stmt.setString(2, kayit.getIsbn());
            stmt.setDate(3, kayit.getAlinmaTarihi());
            stmt.setDate(4, kayit.getIadeTarihi());
            stmt.setInt(5, kayit.getAlinmaSayisi());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Kitap ödünç alma hatası: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean kitapIadeEt(String ogrenciNo, String isbn) {
        // Bu metot, iade edilen kitabın kayıtlarını güncellemek yerine silme işlemi yapıyor
        // Eğer geçmiş kayıtları tutmak isterseniz, silmek yerine "iade edildi" durumuna güncelleyebilirsiniz
        String sql = "DELETE FROM kitaptakipveanaliz WHERE ogrencino = ? AND isbn = ? AND iadetarihi > CURRENT_DATE";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ogrenciNo);
            stmt.setString(2, isbn);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Kitap iade hatası: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<KitapTakip> kullaniciOduncListesi(String ogrenciNo) {
        List<KitapTakip> oduncListesi = new ArrayList<>();

        String sql = "SELECT k.*, kt.*, " +
                     "CONCAT(u.uyead, ' ', u.uyesoyad) AS uyeadsoyad, " +
                     "ks.kitapad, " +
                     "CONCAT(y.yazarad, ' ', y.yazarsoyad) AS yazaradsoyad " +
                     "FROM kitaptakipveanaliz k " +
                     "JOIN kullanici u ON k.ogrencino = u.ogrencino " +
                     "JOIN kitaplar ks ON k.isbn = ks.isbn " +
                     "LEFT JOIN yazar y ON ks.yazarno = y.yazarno " +
                     "WHERE k.ogrencino = ? AND k.iadetarihi > CURRENT_DATE";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ogrenciNo);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    KitapTakip kayit = new KitapTakip();
                    kayit.setKayitNo(rs.getInt("kayitno"));
                    kayit.setOgrenciNo(rs.getString("ogrencino"));
                    kayit.setIsbn(rs.getString("isbn"));
                    kayit.setAlinmaTarihi(rs.getDate("alinmatarihi"));
                    kayit.setIadeTarihi(rs.getDate("iadetarihi"));
                    kayit.setAlinmaSayisi(rs.getInt("alinmasayisi"));
                    kayit.setKitapAd(rs.getString("kitapad"));
                    kayit.setYazarAdSoyad(rs.getString("yazaradsoyad"));
                    kayit.setUyeAdSoyad(rs.getString("uyeadsoyad"));

                    oduncListesi.add(kayit);
                }
            }
        } catch (SQLException e) {
            System.out.println("Kullanıcı ödünç listesi hatası: " + e.getMessage());
            e.printStackTrace();
        }

        return oduncListesi;
    }

    @Override
    public List<KitapTakip> tumOduncKayitlari() {
        List<KitapTakip> oduncListesi = new ArrayList<>();

        String sql = "SELECT k.*, kt.*, " +
                     "CONCAT(u.uyead, ' ', u.uyesoyad) AS uyeadsoyad, " +
                     "ks.kitapad, " +
                     "CONCAT(y.yazarad, ' ', y.yazarsoyad) AS yazaradsoyad " +
                     "FROM kitaptakipveanaliz k " +
                     "JOIN kullanici u ON k.ogrencino = u.ogrencino " +
                     "JOIN kitaplar ks ON k.isbn = ks.isbn " +
                     "LEFT JOIN yazar y ON ks.yazarno = y.yazarno " +
                     "WHERE k.iadetarihi > CURRENT_DATE";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                KitapTakip kayit = new KitapTakip();
                kayit.setKayitNo(rs.getInt("kayitno"));
                kayit.setOgrenciNo(rs.getString("ogrencino"));
                kayit.setIsbn(rs.getString("isbn"));
                kayit.setAlinmaTarihi(rs.getDate("alinmatarihi"));
                kayit.setIadeTarihi(rs.getDate("iadetarihi"));
                kayit.setAlinmaSayisi(rs.getInt("alinmasayisi"));
                kayit.setKitapAd(rs.getString("kitapad"));
                kayit.setYazarAdSoyad(rs.getString("yazaradsoyad"));
                kayit.setUyeAdSoyad(rs.getString("uyeadsoyad"));

                oduncListesi.add(kayit);
            }
        } catch (SQLException e) {
            System.out.println("Tüm ödünç kayıtları hatası: " + e.getMessage());
            e.printStackTrace();
        }

        return oduncListesi;
    }

    @Override
    public List<KitapTakip> gecikmisOduncKayitlari() {
        List<KitapTakip> gecikmisListesi = new ArrayList<>();

        String sql = "SELECT k.*, kt.*, " +
                     "CONCAT(u.uyead, ' ', u.uyesoyad) AS uyeadsoyad, " +
                     "ks.kitapad, " +
                     "CONCAT(y.yazarad, ' ', y.yazarsoyad) AS yazaradsoyad " +
                     "FROM kitaptakipveanaliz k " +
                     "JOIN kullanici u ON k.ogrencino = u.ogrencino " +
                     "JOIN kitaplar ks ON k.isbn = ks.isbn " +
                     "LEFT JOIN yazar y ON ks.yazarno = y.yazarno " +
                     "WHERE k.iadetarihi < CURRENT_DATE";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                KitapTakip kayit = new KitapTakip();
                kayit.setKayitNo(rs.getInt("kayitno"));
                kayit.setOgrenciNo(rs.getString("ogrencino"));
                kayit.setIsbn(rs.getString("isbn"));
                kayit.setAlinmaTarihi(rs.getDate("alinmatarihi"));
                kayit.setIadeTarihi(rs.getDate("iadetarihi"));
                kayit.setAlinmaSayisi(rs.getInt("alinmasayisi"));
                kayit.setKitapAd(rs.getString("kitapad"));
                kayit.setYazarAdSoyad(rs.getString("yazaradsoyad"));
                kayit.setUyeAdSoyad(rs.getString("uyeadsoyad"));

                gecikmisListesi.add(kayit);
            }
        } catch (SQLException e) {
            System.out.println("Gecikmiş ödünç kayıtları hatası: " + e.getMessage());
            e.printStackTrace();
        }

        return gecikmisListesi;
    }
}