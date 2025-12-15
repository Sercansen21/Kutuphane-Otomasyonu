package com.kutuphane.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Singleton instance
    private static DBConnection instance;
    private Connection connection;

    // Bağlantı bilgileri
    private static final String URL = "jdbc:postgresql://localhost:5432/Kutuphane";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345678"; // PostgreSQL şifrenizi buraya yazın

    // Özel constructor (private) - dışarıdan erişimi engeller
    private DBConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Veritabanı bağlantısı başarılı");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC sürücüsü bulunamadı: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Veritabanı bağlantı hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Singleton instance için static method
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    // Bağlantıyı döndür
    public Connection getConnection() {
        return connection;
    }

    // Bağlantıyı kapat
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                instance = null; // Instance'ı sıfırla
                System.out.println("Veritabanı bağlantısı kapatıldı");
            } catch (SQLException e) {
                System.out.println("Bağlantı kapatma hatası: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}