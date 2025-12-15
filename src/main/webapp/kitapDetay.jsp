<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.kutuphane.model.Kullanici" %>
<%@ page import="com.kutuphane.model.Kitap" %>
<%
    // Kullanıcı kontrolü
    Kullanici kullanici = (Kullanici) session.getAttribute("kullanici");
    if (kullanici == null) {
        response.sendRedirect("login");
        return;
    }
    
    // Kitap detayı
    Kitap kitap = (Kitap) request.getAttribute("kitap");
    if (kitap == null) {
        response.sendRedirect("dashboard");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Kitap Detayı - <%= kitap.getKitapAd() %></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .sidebar {
            height: 100vh;
            background-color: #212529;
            color: white;
            position: fixed;
            padding-top: 20px;
        }
        .sidebar .nav-link {
            color: #adb5bd;
            padding: 10px 20px;
        }
        .sidebar .nav-link:hover {
            color: white;
        }
        .sidebar .nav-link.active {
            color: white;
            background-color: #0d6efd;
        }
        .content {
            margin-left: 250px;
            padding: 20px;
        }
        @media (max-width: 768px) {
            .sidebar {
                width: 100%;
                height: auto;
                position: relative;
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
            <div class="col-md-2 sidebar">
                <h4 class="text-center mb-4">Kütüphane Sistemi</h4>
                <div class="text-center mb-4">
                    <i class="fas fa-user-circle fa-3x mb-2"></i>
                    <p><%= kullanici.getUyeAd() + " " + kullanici.getUyeSoyad() %></p>
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
                        <a class="nav-link" href="logout">
                            <i class="fas fa-sign-out-alt me-2"></i> Çıkış Yap
                        </a>
                    </li>
                </ul>
            </div>
            
            <!-- Main Content -->
            <div class="col-md-10 content">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h1>Kitap Detayı</h1>
                    <a href="dashboard" class="btn btn-secondary">
                        <i class="fas fa-arrow-left me-2"></i> Geri Dön
                    </a>
                </div>
                
                <div class="row">
                    <div class="col-md-8 offset-md-2">
                        <div class="card">
                            <div class="card-header bg-primary text-white">
                                <h3 class="mb-0"><%= kitap.getKitapAd() %></h3>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-4 text-center">
                                        <i class="fas fa-book fa-6x text-primary mb-3"></i>
                                    </div>
                                    <div class="col-md-8">
                                        <ul class="list-group list-group-flush">
                                            <li class="list-group-item"><strong>ISBN:</strong> <%= kitap.getIsbn() %></li>
                                            <li class="list-group-item"><strong>Yazar No:</strong> <%= kitap.getYazarNo() %></li>
                                            <li class="list-group-item"><strong>Tür No:</strong> <%= kitap.getTurNo() %></li>
                                            <li class="list-group-item"><strong>Sayfa Sayısı:</strong> <%= kitap.getSayfaSayisi() %></li>
                                        </ul>
                                    </div>
                                </div>
                                <div class="mt-4">
                                    <a href="#" class="btn btn-success">Ödünç Al</a>
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