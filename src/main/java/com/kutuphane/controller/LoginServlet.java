package com.kutuphane.controller;

import java.io.IOException;

import com.kutuphane.dao.DAOFactory;
import com.kutuphane.dao.KullaniciDAO;
import com.kutuphane.dao.PersonelDAO;
import com.kutuphane.model.Kullanici;
import com.kutuphane.model.Personel;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private KullaniciDAO kullaniciDAO;
    private PersonelDAO personelDAO;

    public LoginServlet() {
        super();
        // Factory kullanarak DAO nesnelerini oluştur
        DAOFactory daoFactory = DAOFactory.getInstance();
        kullaniciDAO = daoFactory.createKullaniciDAO();
        personelDAO = daoFactory.createPersonelDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kullanıcı zaten giriş yapmışsa dashboard'a yönlendir
        HttpSession session = request.getSession(false);
        if (session != null && (session.getAttribute("kullanici") != null || session.getAttribute("personel") != null)) {
            response.sendRedirect("dashboard");
            return;
        }

        // Giriş sayfasını göster
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kullanıcı tipi parametresini al
        String userType = request.getParameter("userType");
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        // Form kontrolü
        if (userId == null || userId.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Kullanıcı ID ve şifre gereklidir!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Session oluştur
        HttpSession session = request.getSession();

        // Kullanıcı tipine göre doğrulama yap
        if ("student".equals(userType)) {
            // Öğrenci girişi
            Kullanici kullanici = kullaniciDAO.kullaniciGiris(userId, password);

            if (kullanici != null) {
                session.setAttribute("kullanici", kullanici);
                response.sendRedirect("dashboard");
            } else {
                request.setAttribute("errorMessage", "Geçersiz öğrenci numarası veya şifre!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } else if ("admin".equals(userType)) {
            // Personel girişi
            Personel personel = personelDAO.personelGiris(userId, password);

            if (personel != null) {
                session.setAttribute("personel", personel);
                response.sendRedirect("adminDashboard");
            } else {
                request.setAttribute("errorMessage", "Geçersiz personel TC veya şifre!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "Geçersiz kullanıcı tipi!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}