package com.kutuphane.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kutuphane.dao.KullaniciDAO;
import com.kutuphane.model.Kullanici;
import com.kutuphane.util.DBConnection;

public class KullaniciDAOImpl implements KullaniciDAO {
    private Connection connection;

    public KullaniciDAOImpl() {
        connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public List<Kullanici> tumKullanicilariGetir() {
        List<Kullanici> kullanicilar = new ArrayList<>();
        String sql = "SELECT * FROM kullanici";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Kullanici kullanici = new Kullanici();
                kullanici.setOgrenciNo(rs.getString("ogrencino"));
                kullanici.setUyeAd(rs.getString("uyead"));
                kullanici.setUyeSoyad(rs.getString("uyesoyad"));
                kullanici.setSifre(rs.getString("sifre"));
                kullanici.setIletisim(rs.getString("iletisim"));

                kullanicilar.add(kullanici);
            }
        } catch (SQLException e) {
            System.out.println("Kullanıcıları getirme hatası: " + e.getMessage());
            e.printStackTrace();
        }

        return kullanicilar;
    }

    @Override
    public Kullanici kullaniciGetir(String ogrenciNo) {
        Kullanici kullanici = null;
        String sql = "SELECT * FROM kullanici WHERE ogrencino = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ogrenciNo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    kullanici = new Kullanici();
                    kullanici.setOgrenciNo(rs.getString("ogrencino"));
                    kullanici.setUyeAd(rs.getString("uyead"));
                    kullanici.setUyeSoyad(rs.getString("uyesoyad"));
                    kullanici.setSifre(rs.getString("sifre"));
                    kullanici.setIletisim(rs.getString("iletisim"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Kullanıcı getirme hatası: " + e.getMessage());
            e.printStackTrace();
        }

        return kullanici;
    }

    @Override
    public Kullanici kullaniciGiris(String ogrenciNo, String sifre) {
        Kullanici kullanici = null;
        String sql = "SELECT * FROM kullanici WHERE ogrencino = ? AND sifre = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ogrenciNo);
            stmt.setString(2, sifre);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    kullanici = new Kullanici();
                    kullanici.setOgrenciNo(rs.getString("ogrencino"));
                    kullanici.setUyeAd(rs.getString("uyead"));
                    kullanici.setUyeSoyad(rs.getString("uyesoyad"));
                    kullanici.setSifre(rs.getString("sifre"));
                    kullanici.setIletisim(rs.getString("iletisim"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Kullanıcı giriş hatası: " + e.getMessage());
            e.printStackTrace();
        }

        return kullanici;
    }

    @Override
    public boolean kullaniciEkle(Kullanici kullanici) {
        String sql = "INSERT INTO kullanici (ogrencino, uyead, uyesoyad, sifre, iletisim) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, kullanici.getOgrenciNo());
            stmt.setString(2, kullanici.getUyeAd());
            stmt.setString(3, kullanici.getUyeSoyad());
            stmt.setString(4, kullanici.getSifre());
            stmt.setString(5, kullanici.getIletisim());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Kullanıcı ekleme hatası: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean kullaniciSil(String ogrenciNo) {
        try {
            String checkSQL = "SELECT COUNT(*) FROM kitaptakipveanaliz WHERE ogrencino = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSQL)) {
                checkStmt.setString(1, ogrenciNo);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Bu kullanıcının kitap takip kayıtları var, önce onları silmeniz gerekiyor!");
                    return false;
                }
            }

            String deleteSQL = "DELETE FROM kullanici WHERE ogrencino = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL)) {
                deleteStmt.setString(1, ogrenciNo);
                int affectedRows = deleteStmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            System.out.println("Kullanıcı silme hatası: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}