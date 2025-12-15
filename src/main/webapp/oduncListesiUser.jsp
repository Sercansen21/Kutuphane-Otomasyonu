<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.kutuphane.model.Kullanici" %>
<%@ page import="com.kutuphane.model.Odunc" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%
    // Oturum kontrolü
    Kullanici kullanici = (Kullanici) session.getAttribute("kullanici");
    if (kullanici == null) {
        response.sendRedirect("login");
        return;
    }
    
    List<Odunc> oduncListesi = (List<Odunc>) request.getAttribute("oduncListesi");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date today = new Date();
    
    // Mesajları al
    String successMessage = (String) session.getAttribute("successMessage");
    String errorMessage = (String) session.getAttribute("errorMessage");
    
    // Kullan ve temizle
    session.removeAttribute("successMessage");
    session.removeAttribute("errorMessage");
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
                            <i class="fas fa-book-reader me-2"></i> Ödünç Kitaplarım
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
                    <h1>Ödünç Aldığım Kitaplar</h1>
                    <a href="kitaplar" class="btn btn-primary">
                        <i class="fas fa-book me-2"></i>Kitapları Görüntüle
                    </a>
                </div>
                
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
                
                <% if (oduncListesi != null && !oduncListesi.isEmpty()) { %>
                    <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <thead class="table-dark">
                                <tr>
                                    <th>Kitap Adı</th>
                                    <th>Yazar</th>
                                    <th>Ödünç Tarihi</th>
                                    <th>İade Tarihi</th>
                                    <th>Durum</th>
                                    <th>İşlem</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Odunc odunc : oduncListesi) { 
                                    boolean isGecikme = odunc.isGecikme();
                                    boolean isIadeEdilmis = odunc.isIadeEdilmis();
                                %>
                                <tr>
                                    <td><%= odunc.getKitapAd() %></td>
                                    <td><%= odunc.getAlimTarihi() != null ? dateFormat.format(odunc.getAlimTarihi()) : "-" %></td>
                                    <td><%= odunc.getSonIadeTarihi() != null ? dateFormat.format(odunc.getSonIadeTarihi()) : "-" %></td>
                                    <td>
                                        <% if (isIadeEdilmis) { %>
                                            <span class="badge bg-success">İade Edildi</span>
                                        <% } else if (isGecikme) { %>
                                            <span class="badge bg-danger">Gecikmiş</span>
                                        <% } else { %>
                                            <span class="badge bg-primary">Ödünç Alındı</span>
                                        <% } %>
                                    </td>
                                    <td>
                                        <% if (!isIadeEdilmis) { %>
                                            <a href="oduncIade?isbn=<%= odunc.getIsbn() %>" class="btn btn-sm btn-warning">
                                                <i class="fas fa-undo me-1"></i>İade Et
                                            </a>
                                        <% } else { %>
                                            <span class="text-muted">İade edildi</span>
                                        <% } %>
                                    </td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                <% } else { %>
                    <div class="alert alert-info">
                        <i class="fas fa-info-circle me-2"></i>Şu anda ödünç aldığınız kitap bulunmamaktadır.
                    </div>
                <% } %>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>