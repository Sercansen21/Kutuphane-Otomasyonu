package com.kutuphane.dao;

import com.kutuphane.dao.impl.KitapDAOImpl;
import com.kutuphane.dao.impl.KitapTakipDAOImpl;
import com.kutuphane.dao.impl.KullaniciDAOImpl;
import com.kutuphane.dao.impl.PersonelDAOImpl;

/**
 * Factory Method Pattern uygulaması.
 * Bu sınıf, tüm DAO nesnelerinin oluşturulmasından sorumludur.
 */
public class DAOFactory {
    // Singleton instance
    private static DAOFactory instance;

    // Private constructor
    private DAOFactory() {
        // Private constructor to prevent external instantiation
    }

    // Singleton instance için method
    public static synchronized DAOFactory getInstance() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }

    // Factory Methods for creating DAO objects
    public KitapDAO createKitapDAO() {
        return new KitapDAOImpl();
    }

    public KullaniciDAO createKullaniciDAO() {
        return new KullaniciDAOImpl();
    }

    public PersonelDAO createPersonelDAO() {
        return new PersonelDAOImpl();
    }

    public KitapTakipDAO createKitapTakipDAO() {
        return new KitapTakipDAOImpl();
    }
}