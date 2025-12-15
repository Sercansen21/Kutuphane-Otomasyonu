<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.kutuphane.model.Kullanici" %>
<%@ page import="com.kutuphane.model.Kitap" %>
<%@ page import="java.util.List" %>
<%
    // Oturum kontrolü
    Kullanici kullanici = (Kullanici) session.getAttribute("kullanici");
    if (kullanici == null) {
        response.sendRedirect("login");
        return;
    }
    
    List<Kitap> kitaplar = (List<Kitap>) request.getAttribute("kitaplar");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Kütüphane Sistemi - Kullanıcı Paneli</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background-color: #f5f5f5;
        }
        .navbar {
            background-color: #0d6efd;
        }
        .navbar-brand, .nav-link {
            color: white;
        }
        .nav-link:hover {
            color: rgba(255,255,255,0.8);
        }
        .card {
            transition: transform 0.3s;
            margin-bottom: 20px;
        }
        .card:hover {
            transform: translateY(-5px);
        }
    </style>
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container">
            <a class="navbar-brand" href="#"><i class="fas fa-book me-2"></i>Kütüphane Sistemi</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link active" href="dashboard"><i class="fas fa-home me-1"></i>Ana Sayfa</a>
                    </li>
                </ul>
                <div class="d-flex align-items-center">
                    <span class="text-white me-3"><i class="fas fa-user-circle me-1"></i><%= kullanici.getUyeAd() + " " + kullanici.getUyeSoyad() %></span>
                    <a href="logout" class="btn btn-outline-light btn-sm"><i class="fas fa-sign-out-alt me-1"></i>Çıkış</a>
                </div>
            </div>
        </div>
    </nav>
    
    <!-- Ana İçerik -->
    <div class="container py-4">
        <div class="row mb-4">
            <div class="col-md-6">
                <h2>Hoş Geldiniz, <%= kullanici.getUyeAd() %></h2>
                <p>Kütüphane sistemimizde aradığınız kitapları bulabilirsiniz.</p>
            </div>
            <div class="col-md-6">
                <form action="dashboard" method="get" class="d-flex">
                    <input type="text" name="search" class="form-control me-2" placeholder="Kitap adı, yazar veya ISBN..." value="<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>">
                    <button type="submit" class="btn btn-primary"><i class="fas fa-search me-1"></i>Ara</button>
                </form>
            </div>
        </div>
        
        <h3 class="mb-3">Kitaplar</h3>
        
        <div class="row">
            <% if (kitaplar != null && !kitaplar.isEmpty()) { %>
                <% for (Kitap kitap : kitaplar) { %>
                    <div class="col-md-4">
                        <div class="card h-100">
                            <div class="card-body">
                                <h5 class="card-title"><%= kitap.getKitapAd() %></h5>
                                <h6 class="card-subtitle mb-2 text-muted"><%= kitap.getYazarAdSoyad() != null ? kitap.getYazarAdSoyad() : kitap.getYazarNo() %></h6>
                                <p class="card-text">
                                    <span class="badge bg-info"><%= kitap.getTurAd() %></span><br>
                                    <small>ISBN: <%= kitap.getIsbn() %></small><br>
                                    <small>Sayfa Sayısı: <%= kitap.getSayfaSayisi() %></small>
                                </p>
                            </div>
                        </div>
                    </div>
                <% } %>
            <% } else { %>
                <div class="col-12">
                    <div class="alert alert-info">
                        <i class="fas fa-info-circle me-2"></i>Kitap bulunamadı.
                    </div>
                </div>
            <% } %>
        </div>
    </div>
    
    <!-- Footer -->
    <footer class="bg-dark text-white py-4 mt-5">
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <h5>Kütüphane Yönetim Sistemi</h5>
                    <p>Online kütüphane yönetim sistemi</p>
                </div>
                <div class="col-md-6 text-end">
                    <p class="mb-0">&copy; 2024 Kütüphane Yönetim Sistemi</p>
                </div>
            </div>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>