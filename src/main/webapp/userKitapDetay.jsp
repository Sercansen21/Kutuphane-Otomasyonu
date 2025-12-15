<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.kutuphane.model.Kullanici" %>
<%@ page import="com.kutuphane.model.Kitap" %>
<%
    // Oturum kontrolü
    Kullanici kullanici = (Kullanici) session.getAttribute("kullanici");
    if (kullanici == null) {
        response.sendRedirect("login");
        return;
    }
    
    Kitap kitap = (Kitap) request.getAttribute("kitap");
    if (kitap == null) {
        response.sendRedirect("kitaplar");
        return;
    }
    
    String successMessage = (String) session.getAttribute("successMessage");
    String errorMessage = (String) session.getAttribute("errorMessage");
    
    // Mesaj gösterildikten sonra oturumdan temizle
    if (successMessage != null) {
        session.removeAttribute("successMessage");
    }
    if (errorMessage != null) {
        session.removeAttribute("errorMessage");
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= kitap.getKitapAd() %> - Kütüphane Sistemi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background-color: #f5f5f5;
        }
        .navbar {
            background-color: #212529;
        }
        .sidebar {
            height: 100vh;
            background-color: #212529;
            color: white;
            position: fixed;
            width: 250px;
        }
        .sidebar .nav-link {
            color: white;
        }
        .sidebar .nav-link:hover {
            background-color: #343a40;
        }
        .sidebar .nav-link.active {
            background-color: #0d6efd;
        }
        .content {
            margin-left: 250px;
            padding: 20px;
        }
        .profile-img {
            width: 100px;
            height: 100px;
            border-radius: 50%;
            background-color: #6c757d;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 20px auto;
            font-size: 40px;
            color: white;
        }
        .book-icon {
            font-size: 100px;
            color: #0d6efd;
            margin-bottom: 20px;
        }
        @media (max-width: 768px) {
            .sidebar {
                position: static;
                height: auto;
                width: 100%;
            }
            .content {
                margin-left: 0;
            }
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-3 col-lg-2 p-0 sidebar">
                <div class="d-flex flex-column align-items-center py-4">
                    <div class="profile-img">
                        <i class="fas fa-user"></i>
                    </div>
                    <h5 class="text-center"><%= kullanici.getUyeAd() + " " + kullanici.getUyeSoyad() %></h5>
                </div>
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link" href="dashboard">
                            <i class="fas fa-home me-2"></i> Ana Sayfa
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="kitaplar">
                            <i class="fas fa-book me-2"></i> Kitaplar
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="oduncListesi">
                            <i class="fas fa-list me-2"></i> Ödünç Listem
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="logout">
                            <i class="fas fa-sign-out-alt me-2"></i> Çıkış Yap
                        </a>
                    </li>
                </ul>
            </div>
            
            <!-- Main Content -->
            <div class="col-md-9 col-lg-10 content">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h1>Kitap Detayları</h1>
                    <a href="kitaplar" class="btn btn-secondary">
                        <i class="fas fa-arrow-left me-2"></i>Kitap Listesine Dön
                    </a>
                </div>
                
                <!-- Mesajlar -->
                <% if (successMessage != null && !successMessage.isEmpty()) { %>
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fas fa-check-circle me-2"></i><%= successMessage %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                <% } %>
                
                <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle me-2"></i><%= errorMessage %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                <% } %>
                
                <div class="row">
                    <div class="col-md-4 text-center">
                        <div class="card p-4">
                            <div class="book-icon">
                                <i class="fas fa-book"></i>
                            </div>
                            <h3 class="mb-0"><%= kitap.getKitapAd() %></h3>
                            <p class="text-muted"><%= kitap.getYazarAdSoyad() != null ? kitap.getYazarAdSoyad() : "" %></p>
                        </div>
                    </div>
                    <div class="col-md-8">
                        <div class="card">
                            <div class="card-header bg-primary text-white">
                                <h5 class="mb-0">Kitap Bilgileri</h5>
                            </div>
                            <div class="card-body">
                                <div class="row mb-3">
                                    <div class="col-md-3"><strong>ISBN:</strong></div>
                                    <div class="col-md-9"><%= kitap.getIsbn() %></div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-3"><strong>Yazar:</strong></div>
                                    <div class="col-md-9"><%= kitap.getYazarAdSoyad() != null ? kitap.getYazarAdSoyad() : "Belirtilmemiş" %></div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-3"><strong>Tür:</strong></div>
                                    <div class="col-md-9"><span class="badge bg-info"><%= kitap.getTurAd() != null ? kitap.getTurAd() : "Belirsiz" %></span></div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-3"><strong>Sayfa Sayısı:</strong></div>
                                    <div class="col-md-9"><%= kitap.getSayfaSayisi() %></div>
                                </div>
                                <div class="row">
                                    <div class="col-md-12 mt-3">
                                        <a href="oduncAl?isbn=<%= kitap.getIsbn() %>" class="btn btn-success">
                                            <i class="fas fa-book me-2"></i>Ödünç Al
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>