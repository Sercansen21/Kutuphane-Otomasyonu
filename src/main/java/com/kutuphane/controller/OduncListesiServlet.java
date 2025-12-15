package com.kutuphane.controller;

import java.io.IOException;
import java.util.List;

import com.kutuphane.dao.OduncDAO;
import com.kutuphane.factory.DAOFactory;
import com.kutuphane.model.Kullanici;
import com.kutuphane.model.Odunc;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/oduncListesi")
public class OduncListesiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OduncDAO oduncDAO;

    public OduncListesiServlet() {
        super();
        oduncDAO = DAOFactory.getInstance().createOduncDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Oturum kontrolü
        HttpSession session = request.getSession(false);
        if (session == null || (session.getAttribute("kullanici") == null && session.getAttribute("personel") == null)) {
            response.sendRedirect("login");
            return;
        }

        // Kullanıcı türünü belirle
        boolean isPersonel = (session.getAttribute("personel") != null);

        List<Odunc> oduncListesi;
        if (isPersonel) {
            // Personel tüm ödünç kayıtlarını görür
            oduncListesi = oduncDAO.tumOduncKayitlariniGetir();

            // Gecikmeli listesi için filtre
            String type = request.getParameter("type");
            if ("overdue".equals(type)) {
                // Sadece gecikmiş kitapları filtrele
                List<Odunc> gecikmisList = new java.util.ArrayList<>();
                for (Odunc odunc : oduncListesi) {
                    if (odunc.isGecikme()) {
                        gecikmisList.add(odunc);
                    }
                }
                oduncListesi = gecikmisList;
                request.setAttribute("listTitle", "Gecikmiş Kitaplar");
            } else {
                request.setAttribute("listTitle", "Tüm Ödünç Kitaplar");
            }
        } else {
            // Kullanıcı sadece kendi ödünç aldığı kitapları görür
            Kullanici kullanici = (Kullanici) session.getAttribute("kullanici");
            oduncListesi = oduncDAO.kullaniciOduncListesi(kullanici.getOgrenciNo());
        }

        // JSP sayfasına verileri gönder
        request.setAttribute("oduncListesi", oduncListesi);

        // Kullanıcı türüne göre farklı sayfaya yönlendir
        if (isPersonel) {
            request.getRequestDispatcher("oduncListesiAdmin.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("oduncListesiUser.jsp").forward(request, response);
        }
    }
}