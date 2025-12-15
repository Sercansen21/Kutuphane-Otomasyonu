package com.kutuphane.controller;

import java.io.IOException;

import com.kutuphane.dao.KitapDAO;
import com.kutuphane.factory.DAOFactory;
import com.kutuphane.model.Kitap;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/kitapDetay")
public class KitapDetayServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private KitapDAO kitapDAO;

    public KitapDetayServlet() {
        super();
        kitapDAO = DAOFactory.getInstance().createKitapDAO();
    }

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("KitapDetayServlet - doGet metodu çağrıldı");

        // Oturum kontrolü
        HttpSession session = request.getSession(false);
        if (session == null || (session.getAttribute("kullanici") == null && session.getAttribute("personel") == null)) {
            System.out.println("Oturum bulunamadı, login sayfasına yönlendiriliyor");
            response.sendRedirect("login");
            return;
        }

        // ISBN parametresini al
        String isbn = request.getParameter("isbn");
        System.out.println("İstek parametresi ISBN: " + isbn);

        if (isbn == null || isbn.trim().isEmpty()) {
            System.out.println("ISBN parametre sorunu - kitaplar sayfasına yönlendiriliyor");
            response.sendRedirect("kitaplar");
            return;
        }

        // Kitap bilgilerini getir
        Kitap kitap = kitapDAO.kitapGetir(isbn);
        System.out.println("Kitap bilgisi alındı: " + (kitap != null ? kitap.getKitapAd() : "null"));

        if (kitap == null) {
            System.out.println("Kitap bulunamadı - kitaplar sayfasına yönlendiriliyor");
            response.sendRedirect("kitaplar");
            return;
        }

        // Kullanıcı türünü belirle
        boolean isPersonel = (session.getAttribute("personel") != null);
        System.out.println("Kullanıcı türü: " + (isPersonel ? "Personel" : "Kullanıcı"));

        // View'a veri gönder
        request.setAttribute("kitap", kitap);
        request.setAttribute("isPersonel", isPersonel);

        // Kullanıcı türüne göre farklı sayfaya yönlendir
        String jspPage = isPersonel ? "adminKitapDetay.jsp" : "userKitapDetay.jsp";
        System.out.println("Yönlendirilecek JSP: " + jspPage);

        request.getRequestDispatcher(jspPage).forward(request, response);
    }
}