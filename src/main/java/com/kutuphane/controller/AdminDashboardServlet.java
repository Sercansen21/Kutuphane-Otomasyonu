package com.kutuphane.controller;

import java.io.IOException;
import java.util.List;

import com.kutuphane.dao.DAOFactory;
import com.kutuphane.dao.KitapDAO;
import com.kutuphane.model.Kitap;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/adminDashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private KitapDAO kitapDAO;

    public AdminDashboardServlet() {
        super();
        // Factory kullanarak KitapDAO nesnesini oluştur
        kitapDAO = DAOFactory.getInstance().createKitapDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Session kontrolü
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("personel") == null) {
            response.sendRedirect("login");
            return;
        }

        // Başarı/Hata mesajlarını kontrol et ve request'e ekle
        if (session.getAttribute("successMessage") != null) {
            request.setAttribute("successMessage", session.getAttribute("successMessage"));
            session.removeAttribute("successMessage");
        }

        if (session.getAttribute("errorMessage") != null) {
            request.setAttribute("errorMessage", session.getAttribute("errorMessage"));
            session.removeAttribute("errorMessage");
        }

        // Tüm kitapları getir
        List<Kitap> kitaplar = kitapDAO.tumKitaplariGetir();
        request.setAttribute("kitaplar", kitaplar);

        // Admin panelini göster
        request.getRequestDispatcher("/adminDashboard.jsp").forward(request, response);
    }
}