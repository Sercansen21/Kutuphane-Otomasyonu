package com.kutuphane.controller;

import java.io.IOException;

import com.kutuphane.dao.DAOFactory;
import com.kutuphane.dao.KitapDAO;
import com.kutuphane.model.Kitap;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/addBook")
public class AddBookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private KitapDAO kitapDAO;

    public AddBookServlet() {
        super();
        // Factory kullanarak KitapDAO nesnesini oluştur
        kitapDAO = DAOFactory.getInstance().createKitapDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("personel") == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            // Form verilerini al
            String isbn = request.getParameter("isbn");
            String kitapAd = request.getParameter("kitapAd");
            String sayfaSayisiStr = request.getParameter("sayfaSayisi");
            String turAd = request.getParameter("turAd");
            String yazarAdSoyad = request.getParameter("yazarAdSoyad");

            System.out.println("Formdan gelen veriler:");
            System.out.println("ISBN: " + isbn);
            System.out.println("Kitap Adı: " + kitapAd);
            System.out.println("Sayfa Sayısı: " + sayfaSayisiStr);
            System.out.println("Tür Adı: " + turAd);
            System.out.println("Yazar Adı Soyadı: '" + yazarAdSoyad + "'");

            // Validasyon
            if (isbn == null || isbn.trim().isEmpty() ||
                kitapAd == null || kitapAd.trim().isEmpty() ||
                sayfaSayisiStr == null || sayfaSayisiStr.trim().isEmpty() ||
                turAd == null || turAd.trim().isEmpty()) {

                session.setAttribute("errorMessage", "Tüm alanları doldurunuz!");
                response.sendRedirect("adminDashboard");
                return;
            }

            int sayfaSayisi;
            try {
                sayfaSayisi = Integer.parseInt(sayfaSayisiStr);
            } catch (NumberFormatException e) {
                session.setAttribute("errorMessage", "Geçersiz sayfa sayısı!");
                response.sendRedirect("adminDashboard");
                return;
            }

            // Kitap nesnesi oluştur
            Kitap kitap = new Kitap();
            kitap.setIsbn(isbn);
            kitap.setKitapAd(kitapAd);
            kitap.setSayfaSayisi(sayfaSayisi);
            kitap.setTurAd(turAd);
            kitap.setYazarAdSoyad(yazarAdSoyad);

            // Kitabı ekle
            boolean basarili = kitapDAO.kitapEkle(kitap);

            if (basarili) {
                session.setAttribute("successMessage", "Kitap başarıyla eklendi.");
            } else {
                session.setAttribute("errorMessage", "Kitap eklenirken bir hata oluştu.");
            }
        } catch (Exception e) {
            session.setAttribute("errorMessage", "İşlem sırasında bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }

        // Admin panel sayfasına yönlendir
        response.sendRedirect("adminDashboard");
    }
}