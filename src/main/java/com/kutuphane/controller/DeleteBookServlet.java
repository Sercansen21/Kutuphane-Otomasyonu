package com.kutuphane.controller;

import java.io.IOException;

import com.kutuphane.dao.DAOFactory;
import com.kutuphane.dao.KitapDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/deleteBook")
public class DeleteBookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private KitapDAO kitapDAO;

    public DeleteBookServlet() {
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

        // ISBN parametresini al
        String isbn = request.getParameter("isbn");
        if (isbn == null || isbn.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Geçersiz kitap ID'si!");
            response.sendRedirect("adminDashboard");
            return;
        }

        // Kitabı sil
        boolean success = kitapDAO.kitapSil(isbn);

        if (success) {
            session.setAttribute("successMessage", "Kitap başarıyla silindi!");
        } else {
            session.setAttribute("errorMessage", "Kitap silinemedi! Kitap ödünç kayıtlarında kullanılıyor olabilir.");
        }

        // Admin paneline yönlendir
        response.sendRedirect("adminDashboard");
    }
}