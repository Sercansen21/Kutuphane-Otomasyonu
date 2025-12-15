package com.kutuphane.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.kutuphane.dao.DAOFactory;
import com.kutuphane.dao.KitapDAO;
import com.kutuphane.model.Kitap;
import com.kutuphane.model.Kullanici;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private KitapDAO kitapDAO;

    public DashboardServlet() {
        super();
        // Factory kullanarak KitapDAO nesnesini oluştur
        kitapDAO = DAOFactory.getInstance().createKitapDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kullanıcı kontrolü
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("kullanici") == null) {
            response.sendRedirect("login");
            return;
        }

        // Kullanıcı bilgisini al
        Kullanici kullanici = (Kullanici) session.getAttribute("kullanici");

        // Kitapları getir
        List<Kitap> kitaplar = kitapDAO.tumKitaplariGetir();
        List<Kitap> filtrelenmisKitaplar = kitaplar;

        // Arama yapılmışsa kitapları filtrele
        String search = request.getParameter("search");
        if (search != null && !search.trim().isEmpty()) {
            search = search.toLowerCase();
            filtrelenmisKitaplar = new ArrayList<>();


            for (Kitap kitap : kitaplar) {
                boolean kitapAdiUyuyor = kitap.getKitapAd() != null &&
                                         kitap.getKitapAd().toLowerCase().contains(search);

                boolean yazarAdiUyuyor = kitap.getYazarAdSoyad() != null &&
                                         kitap.getYazarAdSoyad().toLowerCase().contains(search);

                boolean isbnUyuyor = kitap.getIsbn() != null &&
                                     kitap.getIsbn().toLowerCase().contains(search);

                if (kitapAdiUyuyor || yazarAdiUyuyor || isbnUyuyor) {
                    filtrelenmisKitaplar.add(kitap);
                }
            }
        }

        // View'a veri gönder
        request.setAttribute("kitaplar", filtrelenmisKitaplar);
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}