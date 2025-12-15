package com.kutuphane.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kutuphane.dao.KitapDAO;
import com.kutuphane.dao.OduncDAO;
import com.kutuphane.factory.DAOFactory;
import com.kutuphane.model.Odunc;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/kitapAnaliz")
public class KitapAnalizServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OduncDAO oduncDAO;
    private KitapDAO kitapDAO;

    public KitapAnalizServlet() {
        super();
        DAOFactory factory = DAOFactory.getInstance();
        oduncDAO = factory.createOduncDAO();
        kitapDAO = factory.createKitapDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Sadece personel erişebilmeli
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("personel") == null) {
            response.sendRedirect("login");
            return;
        }

        // Ödünç kayıtlarını getir
        List<Odunc> oduncListesi = oduncDAO.tumOduncKayitlariniGetir();

        // Kitap popülerlik istatistikleri
        Map<String, Integer> kitapIstatistikleri = new HashMap<>();
        Map<String, String> kitapIsimleri = new HashMap<>(); // ISBN -> Kitap Adı eşleştirmesi

        // Kullanıcı istatistikleri
        Map<String, Integer> kullaniciIstatistikleri = new HashMap<>();
        Map<String, String> kullaniciIsimleri = new HashMap<>(); // OgrenciNo -> Ad Soyad eşleştirmesi

        // İstatistikleri hesapla
        for (Odunc odunc : oduncListesi) {
            // Kitap istatistikleri
            String isbn = odunc.getIsbn();
            kitapIstatistikleri.put(isbn, kitapIstatistikleri.getOrDefault(isbn, 0) + 1);
            kitapIsimleri.put(isbn, odunc.getKitapAd());

            // Kullanıcı istatistikleri
            String ogrenciNo = odunc.getOgrenciNo();
            kullaniciIstatistikleri.put(ogrenciNo, kullaniciIstatistikleri.getOrDefault(ogrenciNo, 0) + 1);
            kullaniciIsimleri.put(ogrenciNo, odunc.getKullaniciAd() + " " + odunc.getKullaniciSoyad());
        }

        // View'e veri gönder
        request.setAttribute("oduncListesi", oduncListesi);
        request.setAttribute("kitapIstatistikleri", kitapIstatistikleri);
        request.setAttribute("kitapIsimleri", kitapIsimleri);
        request.setAttribute("kullaniciIstatistikleri", kullaniciIstatistikleri);
        request.setAttribute("kullaniciIsimleri", kullaniciIsimleri);
        request.setAttribute("kitapSayisi", kitapDAO.tumKitaplariGetir().size());

        // JSP sayfasına yönlendir
        request.getRequestDispatcher("kitapAnaliz.jsp").forward(request, response);
    }
}