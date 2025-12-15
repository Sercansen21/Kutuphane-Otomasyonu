package com.kutuphane.controller;

import java.io.IOException;
import java.util.List;

import com.kutuphane.dao.KitapDAO;
import com.kutuphane.factory.DAOFactory;
import com.kutuphane.model.Kitap;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/kitaplar")
public class KitaplarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private KitapDAO kitapDAO;

    public KitaplarServlet() {
        super();
        kitapDAO = DAOFactory.getInstance().createKitapDAO();
    }

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Oturum kontrolü
        HttpSession session = request.getSession(false);
        if (session == null || (session.getAttribute("kullanici") == null && session.getAttribute("personel") == null)) {
            response.sendRedirect("login");
            return;
        }

        // Kullanıcı türüne göre bilgileri al
        boolean isPersonel = (session.getAttribute("personel") != null);

        // Kitapları getir
        List<Kitap> kitaplar = kitapDAO.tumKitaplariGetir();

        // Arama yapılmışsa filtreleme yap
        String search = request.getParameter("search");
        if (search != null && !search.trim().isEmpty()) {
            search = search.toLowerCase();

            // Filtreleme işlemi için yeni bir liste oluştur
            List<Kitap> filtrelenmisKitaplar = new java.util.ArrayList<>();

            for (Kitap kitap : kitaplar) {
                // Kitap adı kontrolü
                boolean kitapAdiUyuyor = kitap.getKitapAd() != null &&
                    kitap.getKitapAd().toLowerCase().contains(search);

                // Yazar adı kontrolü
                boolean yazarAdiUyuyor = kitap.getYazarAdSoyad() != null &&
                    kitap.getYazarAdSoyad().toLowerCase().contains(search);

                // ISBN kontrolü
                boolean isbnUyuyor = kitap.getIsbn() != null &&
                    kitap.getIsbn().toLowerCase().contains(search);

                // Tür adı kontrolü
                boolean turAdiUyuyor = kitap.getTurAd() != null &&
                    kitap.getTurAd().toLowerCase().contains(search);

                if (kitapAdiUyuyor || yazarAdiUyuyor || isbnUyuyor || turAdiUyuyor) {
                    filtrelenmisKitaplar.add(kitap);
                }
            }

            kitaplar = filtrelenmisKitaplar;
        }

        // View'a bilgileri gönder
        request.setAttribute("kitaplar", kitaplar);
        request.setAttribute("isPersonel", isPersonel);

        // Kullanıcı türüne göre farklı sayfaya yönlendir
        if (isPersonel) {
            request.getRequestDispatcher("kitaplarAdmin.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("kitaplarUser.jsp").forward(request, response);
        }
    }
}