package com.kutuphane.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kutuphane.dao.KitapDAO;
import com.kutuphane.model.Kitap;
import com.kutuphane.util.DBConnection;

public class KitapDAOImpl implements KitapDAO {
    private Connection connection;

    // Varsayılan değerler
    private static final String DEFAULT_YAZARNO = "AY101";
    private static final String DEFAULT_TURNO = "R1001";

    public KitapDAOImpl() {
        connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public List<Kitap> tumKitaplariGetir() {
        List<Kitap> kitaplar = new ArrayList<>();
        String sql = "SELECT k.*, t.turad, y.yazarad, y.yazarsoyad FROM kitaplar k " +
                     "LEFT JOIN kitaptur t ON k.turno = t.turno " +
                     "LEFT JOIN yazar y ON k.yazarno = y.yazarno";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Kitap kitap = new Kitap();
                kitap.setIsbn(rs.getString("isbn"));
                kitap.setKitapAd(rs.getString("kitapad"));
                kitap.setSayfaSayisi(rs.getInt("sayfasayisi"));
                kitap.setTurNo(rs.getString("turno"));
                kitap.setYazarNo(rs.getString("yazarno"));

                // Tür adını ayarla
                String turAd = rs.getString("turad");
                kitap.setTurAd(turAd != null ? turAd : "Belirsiz");

                // Yazar adını ayarla
                String yazarAd = rs.getString("yazarad");
                String yazarSoyad = rs.getString("yazarsoyad");

                if (yazarAd != null) {
                    kitap.setYazarAdSoyad(yazarSoyad != null ? yazarAd + " " + yazarSoyad : yazarAd);
                } else {
                    kitap.setYazarAdSoyad("Bilinmeyen Yazar");
                }

                kitaplar.add(kitap);
            }
        } catch (SQLException e) {
            System.out.println("Kitapları getirme hatası: " + e.getMessage());
        }

        return kitaplar;
    }

    @Override
    public Kitap kitapGetir(String isbn) {
        Kitap kitap = null;
        String sql = "SELECT k.*, t.turad, y.yazarad, y.yazarsoyad FROM kitaplar k " +
                     "LEFT JOIN kitaptur t ON k.turno = t.turno " +
                     "LEFT JOIN yazar y ON k.yazarno = y.yazarno " +
                     "WHERE k.isbn = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    kitap = new Kitap();
                    kitap.setIsbn(rs.getString("isbn"));
                    kitap.setKitapAd(rs.getString("kitapad"));
                    kitap.setSayfaSayisi(rs.getInt("sayfasayisi"));
                    kitap.setTurNo(rs.getString("turno"));
                    kitap.setYazarNo(rs.getString("yazarno"));

                    // Tür adını ayarla
                    String turAd = rs.getString("turad");
                    kitap.setTurAd(turAd != null ? turAd : "Belirsiz");

                    // Yazar adını ayarla
                    String yazarAd = rs.getString("yazarad");
                    String yazarSoyad = rs.getString("yazarsoyad");

                    if (yazarAd != null) {
                        kitap.setYazarAdSoyad(yazarSoyad != null ? yazarAd + " " + yazarSoyad : yazarAd);
                    } else {
                        kitap.setYazarAdSoyad("Bilinmeyen Yazar");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Kitap getirme hatası: " + e.getMessage());
        }

        return kitap;
    }

    @Override
    public boolean kitapEkle(Kitap kitap) {
        try {
            // Tür ve yazar kontrolü
            String turNo = kitap.getTurAd() != null ? turKontrol(kitap.getTurAd()) : DEFAULT_TURNO;
            String yazarNo = kitap.getYazarAdSoyad() != null ? yazarKontrol(kitap.getYazarAdSoyad()) : DEFAULT_YAZARNO;

            // Kitabı ekle
            String sql = "INSERT INTO kitaplar (isbn, kitapad, sayfasayisi, turno, yazarno) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, kitap.getIsbn());
                stmt.setString(2, kitap.getKitapAd());
                stmt.setInt(3, kitap.getSayfaSayisi());
                stmt.setString(4, turNo);
                stmt.setString(5, yazarNo);

                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            System.out.println("Kitap ekleme hatası: " + e.getMessage());
            return false;
        }
    }

    //
    private String turKontrol(String turAd) {
        if (turAd == null || turAd.trim().isEmpty()) {
            return DEFAULT_TURNO;
        }

        try {
            // Kontrol et
            String sql = "SELECT turno FROM kitaptur WHERE LOWER(turad) = LOWER(?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, turAd.trim());

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("turno");
                    }
                }
            }

            // Yoksa varsayılan tür kullan
            return DEFAULT_TURNO;

        } catch (SQLException e) {
            return DEFAULT_TURNO;
        }
    }


    private String yazarKontrol(String yazarAdSoyad) {
        if (yazarAdSoyad == null || yazarAdSoyad.trim().isEmpty()) {
            return DEFAULT_YAZARNO;
        }

        try {
            // Ad ve soyadı ayır
            String[] parts = yazarAdSoyad.trim().split("\\s+", 2);
            String ad = parts[0];
            String soyad = parts.length > 1 ? parts[1] : "";

            // Yazar tablosunda ara
            String sql = "SELECT yazarno FROM yazar WHERE LOWER(yazarad) = LOWER(?)";

            if (!soyad.isEmpty()) {
                sql += " AND LOWER(yazarsoyad) = LOWER(?)";
            }

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, ad);
                if (!soyad.isEmpty()) {
                    stmt.setString(2, soyad);
                }

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("yazarno");
                    }
                }
            }

            // Bulunamadıysa varsayılan yazar
            return DEFAULT_YAZARNO;

        } catch (SQLException e) {
            return DEFAULT_YAZARNO;
        }
    }

    @Override
    public boolean kitapGuncelle(Kitap kitap) {
        String sql = "UPDATE kitaplar SET kitapad = ?, sayfasayisi = ?, turno = ?, yazarno = ? WHERE isbn = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, kitap.getKitapAd());
            stmt.setInt(2, kitap.getSayfaSayisi());
            stmt.setString(3, kitap.getTurNo() != null ? kitap.getTurNo() : DEFAULT_TURNO);
            stmt.setString(4, kitap.getYazarNo() != null ? kitap.getYazarNo() : DEFAULT_YAZARNO);
            stmt.setString(5, kitap.getIsbn());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Kitap güncelleme hatası: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean kitapSil(String isbn) {
        // Doğrudan silme işlemi yap
        String sql = "DELETE FROM kitaplar WHERE isbn = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Kitap silme hatası: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean kitapOduncAlindiOlarakIsaretle(String isbn) {
        // Bu metodu sizin veritabanı yapınıza göre düzenleyin
        // Örnek: Bir "Stok" sütunu varsa azaltın veya "OduncDurumu" sütunu güncelleyin
        // Şu anki örnek veritabanınızda bu işlev direkt kitaptakipveanaliz tablosundan sorgulanabilir
        return true;
    }

    @Override
    public boolean kitapIadeEdildiOlarakIsaretle(String isbn) {
        // Bu metodu sizin veritabanı yapınıza göre düzenleyin
        // Örnek: Bir "Stok" sütunu varsa artırın veya "OduncDurumu" sütunu güncelleyin
        // Şu anki örnek veritabanınızda bu işlev direkt kitaptakipveanaliz tablosundan sorgulanabilir
        return true;
    }
}