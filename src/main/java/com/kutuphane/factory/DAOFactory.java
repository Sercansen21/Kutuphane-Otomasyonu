package com.kutuphane.factory;

import com.kutuphane.dao.KitapDAO;
import com.kutuphane.dao.KullaniciDAO;
import com.kutuphane.dao.OduncDAO;
import com.kutuphane.dao.PersonelDAO;
import com.kutuphane.dao.impl.KitapDAOImpl;
import com.kutuphane.dao.impl.KullaniciDAOImpl;
import com.kutuphane.dao.impl.OduncDAOImpl;
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

    public OduncDAO createOduncDAO() {
        return new OduncDAOImpl();
    }
}