package com.kutuphane.controller;

import java.io.IOException;

import com.kutuphane.dao.DAOFactory;
import com.kutuphane.dao.KullaniciDAO;
import com.kutuphane.model.Kullanici;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private KullaniciDAO kullaniciDAO;

    public RegisterServlet() {
        super();
        // Factory kullanarak KullaniciDAO nesnesini oluştur
        kullaniciDAO = DAOFactory.getInstance().createKullaniciDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kayıt sayfasını göster
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Form verilerini al
        String ogrenciNo = request.getParameter("ogrenciNo");
        String uyeAd = request.getParameter("uyeAd");
        String uyeSoyad = request.getParameter("uyeSoyad");
        String iletisim = request.getParameter("iletisim");
        String sifre = request.getParameter("sifre");
        String sifreTekrar = request.getParameter("sifreTekrar");

        // Form doğrulama
        if (ogrenciNo == null || ogrenciNo.trim().isEmpty() ||
            uyeAd == null || uyeAd.trim().isEmpty() ||
            uyeSoyad == null || uyeSoyad.trim().isEmpty() ||
            iletisim == null || iletisim.trim().isEmpty() ||
            sifre == null || sifre.trim().isEmpty()) {

            request.setAttribute("errorMessage", "Lütfen tüm alanları doldurun.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Şifre uzunluğu kontrolü
        if (sifre.length() < 4) {
            request.setAttribute("errorMessage", "Şifre en az 4 karakter olmalıdır.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Şifre uyum kontrolü
        if (!sifre.equals(sifreTekrar)) {
            request.setAttribute("errorMessage", "Şifreler eşleşmiyor.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Öğrenci numarası uzunluk kontrolü
        if (ogrenciNo.length() != 11) {
            request.setAttribute("errorMessage", "Öğrenci numarası 11 haneli olmalıdır.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Kullanıcı zaten var mı kontrolü
        Kullanici mevcutKullanici = kullaniciDAO.kullaniciGetir(ogrenciNo);
        if (mevcutKullanici != null) {
            request.setAttribute("errorMessage", "Bu öğrenci numarası ile kayıtlı bir kullanıcı zaten var.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Yeni kullanıcı oluştur
        Kullanici yeniKullanici = new Kullanici();
        yeniKullanici.setOgrenciNo(ogrenciNo);
        yeniKullanici.setUyeAd(uyeAd);
        yeniKullanici.setUyeSoyad(uyeSoyad);
        yeniKullanici.setIletisim(iletisim);
        yeniKullanici.setSifre(sifre);

        // Kullanıcıyı kaydet
        boolean basarili = kullaniciDAO.kullaniciEkle(yeniKullanici);

        if (basarili) {
            // Başarılı kayıt, giriş sayfasına yönlendir
            response.sendRedirect("login");
        } else {
            // Kayıt başarısız
            request.setAttribute("errorMessage", "Kayıt sırasında bir hata oluştu, lütfen tekrar deneyin.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}