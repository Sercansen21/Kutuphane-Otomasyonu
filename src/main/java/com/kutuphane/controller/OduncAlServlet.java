package com.kutuphane.controller;

import java.io.IOException;

import com.kutuphane.facade.OduncFacade;
import com.kutuphane.model.Kullanici;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/oduncAl")
public class OduncAlServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OduncFacade oduncFacade;

    public OduncAlServlet() {
        super();
        oduncFacade = new OduncFacade();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Oturum kontrolü
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("kullanici") == null) {
            response.sendRedirect("login");
            return;
        }

        // Kullanıcı bilgilerini al
        Kullanici kullanici = (Kullanici) session.getAttribute("kullanici");

        // ISBN parametresini al
        String isbn = request.getParameter("isbn");
        if (isbn == null || isbn.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Geçersiz kitap bilgisi!");
            response.sendRedirect("kitaplar");
            return;
        }

        // Ödünç alma işlemini gerçekleştir
        boolean sonuc = oduncFacade.kitapOduncAl(kullanici.getOgrenciNo(), isbn, 14); // 14 günlük ödünç

        if (sonuc) {
            session.setAttribute("successMessage", "Kitap başarıyla ödünç alındı.");
        } else {
            session.setAttribute("errorMessage", "Kitap ödünç alma işlemi başarısız oldu. Bu kitap zaten ödünç alınmış olabilir.");
        }

        // Kitap detay sayfasına geri dön
        response.sendRedirect("kitapDetay?isbn=" + isbn);
    }
}