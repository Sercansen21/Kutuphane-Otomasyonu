<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.kutuphane.model.Kullanici" %>
<%@ page import="com.kutuphane.model.Odunc" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDate" %>
<%
    // Oturum kontrolü
    Kullanici kullanici = (Kullanici) session.getAttribute("kullanici");
    if (kullanici == null) {
        response.sendRedirect("login");
        return;
    }
    
    List<Odunc> oduncListesi = (List<Odunc>) request.getAttribute("oduncListesi");
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
    <title>Ödünç Aldığım Kitaplar - Kütüphane Sistemi</title>
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
        .table-row-overdue {
            background-color: #ffe6e6 !important;
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
                        <a class="nav-link" href="kitaplar">
                            <i class="fas fa-book me-2"></i> Kitaplar
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="oduncListesi">
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
                <h1 class="mb-4">Ödünç Aldığım Kitaplar</h1>
                
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
                
                <!-- Ödünç Listesi -->
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0"><i class="fas fa-list me-2"></i>Ödünç Kitaplarım</h5>
                    </div>
                    <div class="card-body">
                        <% if (oduncListesi != null && !oduncListesi.isEmpty()) { %>
                            <div class="table-responsive">
                                <table class="table table-striped table-hover">
                                    <thead>
                                        <tr>
                                            <th>Kitap Adı</th>
                                            <th>Alım Tarihi</th>
                                            <th>Son İade Tarihi</th>
                                            <th>Durum</th>
                                            <th>İşlemler</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% for (Odunc odunc : oduncListesi) { 
                                            boolean isOverdue = odunc.isGecikme();
                                            boolean isReturned = odunc.isIadeEdilmis();
                                            String rowClass = isOverdue ? "table-row-overdue" : "";
                                        %>
                                            <tr class="<%= rowClass %>">
                                                <td><%= odunc.getKitapAd() != null ? odunc.getKitapAd() : "Bilinmeyen Kitap" %></td>
                                                <td><%= odunc.getAlimTarihi() != null ? odunc.getAlimTarihi() : "Belirtilmemiş" %></td>
                                                <td><%= odunc.getSonIadeTarihi() != null ? odunc.getSonIadeTarihi() : "Belirtilmemiş" %></td>
                                                <td>
                                                    <% if (isReturned) { %>
                                                        <span class="badge bg-success">İade Edildi</span>
                                                    <% } else if (isOverdue) { %>
                                                        <span class="badge bg-danger">Gecikmiş</span>
                                                    <% } else { %>
                                                        <span class="badge bg-warning">Ödünçte</span>
                                                    <% } %>
                                                </td>
                                                <td>
                                                    <% if (!isReturned) { %>
                                                        <a href="oduncIade?isbn=<%= odunc.getIsbn() %>" class="btn btn-primary btn-sm">
                                                            <i class="fas fa-undo me-1"></i> İade Et
                                                        </a>
                                                    <% } else { %>
                                                        <span class="text-muted">İşlem Yok</span>
                                                    <% } %>
                                                </td>
                                            </tr>
                                        <% } %>
                                    </tbody>
                                </table>
                            </div>
                        <% } else { %>
                            <div class="alert alert-info">
                                <i class="fas fa-info-circle me-2"></i>Henüz ödünç aldığınız kitap bulunmamaktadır.
                            </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>