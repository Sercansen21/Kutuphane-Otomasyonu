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
    <title>Kitaplar - Kütüphane Sistemi</title>
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
        .card {
            transition: transform 0.3s;
            margin-bottom: 20px;
        }
        .card:hover {
            transform: translateY(-5px);
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
                        <a class="nav-link" href="logout">
                            <i class="fas fa-sign-out-alt me-2"></i> Çıkış Yap
                        </a>
                    </li>
                </ul>
            </div>
            
            <!-- Main Content -->
            <div class="col-md-9 col-lg-10 content">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h1>Kütüphane Kitapları</h1>
                    <form action="kitaplar" method="get" class="d-flex">
                        <input type="text" name="search" class="form-control me-2" placeholder="Kitap ara..." value="<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>">
                        <button type="submit" class="btn btn-primary">Ara</button>
                    </form>
                </div>
                
                <div class="row">
                    <% if (kitaplar != null && !kitaplar.isEmpty()) { %>
                        <% for (Kitap kitap : kitaplar) { %>
                            <div class="col-md-4">
                                <div class="card h-100">
                                    <div class="card-body">
                                        <h5 class="card-title"><%= kitap.getKitapAd() %></h5>
                                        <h6 class="card-subtitle mb-2 text-muted"><%= kitap.getYazarAdSoyad() != null ? kitap.getYazarAdSoyad() : "" %></h6>
                                        <p class="card-text">
                                            <span class="badge bg-info"><%= kitap.getTurAd() != null ? kitap.getTurAd() : "Belirsiz" %></span><br>
                                            <small><strong>ISBN:</strong> <%= kitap.getIsbn() %></small><br>
                                            <small><strong>Sayfa Sayısı:</strong> <%= kitap.getSayfaSayisi() %></small>
                                        </p>
                                        <a href="kitapDetay?isbn=<%= kitap.getIsbn() %>" class="btn btn-primary btn-sm">Detaylar</a>
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
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>