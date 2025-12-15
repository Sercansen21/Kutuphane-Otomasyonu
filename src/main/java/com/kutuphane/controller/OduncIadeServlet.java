package com.kutuphane.controller;

import java.io.IOException;

import com.kutuphane.facade.OduncFacade;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/oduncIade")
public class OduncIadeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OduncFacade oduncFacade;

    public OduncIadeServlet() {
        super();
        oduncFacade = new OduncFacade();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Oturum kontrolü
        HttpSession session = request.getSession(false);
        if (session == null || (session.getAttribute("kullanici") == null && session.getAttribute("personel") == null)) {
            response.sendRedirect("login");
            return;
        }

        // ISBN parametresini al
        String isbn = request.getParameter("isbn");
        if (isbn == null || isbn.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Geçersiz kitap bilgisi!");
            response.sendRedirect("oduncListesi");
            return;
        }

        // İade işlemini gerçekleştir
        boolean sonuc = oduncFacade.kitapIadeEt(isbn);

        if (sonuc) {
            session.setAttribute("successMessage", "Kitap başarıyla iade edildi.");
        } else {
            session.setAttribute("errorMessage", "Kitap iade işlemi başarısız oldu. Bu kitap zaten iade edilmiş olabilir.");
        }

        // Ödünç listesi sayfasına geri dön
        response.sendRedirect("oduncListesi");
    }
}