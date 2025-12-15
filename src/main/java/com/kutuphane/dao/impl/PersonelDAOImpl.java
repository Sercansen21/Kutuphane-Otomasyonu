package com.kutuphane.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kutuphane.dao.PersonelDAO;
import com.kutuphane.model.Personel;
import com.kutuphane.util.DBConnection;

public class PersonelDAOImpl implements PersonelDAO {
    private Connection connection;

    public PersonelDAOImpl() {
        connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public List<Personel> tumPersonelleriGetir() {
        List<Personel> personeller = new ArrayList<>();
        String sql = "SELECT * FROM personel";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Personel personel = new Personel();
                personel.setPersonelTC(rs.getString("personeltc"));
                personel.setPersonelAd(rs.getString("personelad"));
                personel.setPersonelSoyad(rs.getString("personelsoyad"));
                personel.setSifre(rs.getString("sifre"));

                personeller.add(personel);
            }
        } catch (SQLException e) {
            System.out.println("Personelleri getirme hatası: " + e.getMessage());
            e.printStackTrace();
        }

        return personeller;
    }

    @Override
    public Personel personelGetir(String personelTC) {
        Personel personel = null;
        String sql = "SELECT * FROM personel WHERE personeltc = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, personelTC);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    personel = new Personel();
                    personel.setPersonelTC(rs.getString("personeltc"));
                    personel.setPersonelAd(rs.getString("personelad"));
                    personel.setPersonelSoyad(rs.getString("personelsoyad"));
                    personel.setSifre(rs.getString("sifre"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Personel getirme hatası: " + e.getMessage());
            e.printStackTrace();
        }

        return personel;
    }

    @Override
    public Personel personelGiris(String personelTC, String sifre) {
        Personel personel = null;
        String sql = "SELECT * FROM personel WHERE personeltc = ? AND sifre = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, personelTC);
            stmt.setString(2, sifre);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    personel = new Personel();
                    personel.setPersonelTC(rs.getString("personeltc"));
                    personel.setPersonelAd(rs.getString("personelad"));
                    personel.setPersonelSoyad(rs.getString("personelsoyad"));
                    personel.setSifre(rs.getString("sifre"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Personel giriş hatası: " + e.getMessage());
            e.printStackTrace();
        }

        return personel;
    }

    @Override
    public boolean personelEkle(Personel personel) {
        String sql = "INSERT INTO personel (personeltc, personelad, personelsoyad, sifre) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, personel.getPersonelTC());
            stmt.setString(2, personel.getPersonelAd());
            stmt.setString(3, personel.getPersonelSoyad());
            stmt.setString(4, personel.getSifre());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Personel ekleme hatası: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean personelSil(String personelTC) {
        String sql = "DELETE FROM personel WHERE personeltc = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, personelTC);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Personel silme hatası: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}