<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.kutuphane.model.Personel" %>
<%@ page import="com.kutuphane.model.Odunc" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%
    // Oturum kontrolü
    Personel personel = (Personel) session.getAttribute("personel");
    if (personel == null) {
        response.sendRedirect("login");
        return;
    }
    
    List<Odunc> oduncListesi = (List<Odunc>) request.getAttribute("oduncListesi");
    String listTitle = (String) request.getAttribute("listTitle");
    if (listTitle == null) {
        listTitle = "Ödünç Kitap Listesi";
    }
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date today = new Date();
    
    // Aktif sekmeyi belirle
    String type = request.getParameter("type");
    boolean isOverdue = "overdue".equals(type);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= listTitle %> - Kütüphane Yönetim Sistemi</title>
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
                        <i class="fas fa-user-shield"></i>
                    </div>
                    <h5 class="text-center"><%= personel.getPersonelAd() + " " + personel.getPersonelSoyad() %></h5>
                    <span class="badge bg-danger">Personel</span>
                </div>
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link" href="adminDashboard">
                            <i class="fas fa-tachometer-alt me-2"></i> Dashboard
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="kitaplar">
                            <i class="fas fa-book me-2"></i> Kitaplar
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="oduncListesi">
                            <i class="fas fa-book-reader me-2"></i> Ödünç Takip
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="kitapAnaliz">
                            <i class="fas fa-chart-bar me-2"></i> Kitap Analiz
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
                <h1 class="mb-4"><%= listTitle %></h1>
                
                <ul class="nav nav-tabs mb-4">
                    <li class="nav-item">
                        <a class="nav-link <%= !isOverdue ? "active" : "" %>" href="oduncListesi">Tüm Ödünç Kitaplar</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <%= isOverdue ? "active" : "" %>" href="oduncListesi?type=overdue">Gecikmiş Kitaplar</a>
                    </li>
                </ul>
                
                <% if (oduncListesi != null && !oduncListesi.isEmpty()) { %>
                    <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <thead class="table-dark">
                                <tr>
                                    <th>Öğrenci No</th>
                                    <th>Öğrenci Adı</th>
                                    <th>Kitap</th>
                                    <th>Ödünç Tarihi</th>
                                    <th>Son İade Tarihi</th>
                                    <th>Durum</th>
                                    <th>Gecikme</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Odunc odunc : oduncListesi) { 
                                    boolean isGecikme = odunc.isGecikme();
                                    boolean isIadeEdilmis = odunc.isIadeEdilmis();
                                    Date oduncTarihi = odunc.getAlimTarihi();
                                    Date iadeTarihi = odunc.getSonIadeTarihi();
                                    
                                    // Gecikme gün sayısı hesaplama
                                    long gecikmeGun = 0;
                                    double cezaTutari = 0;
                                    if (isGecikme) {
                                        long diff = today.getTime() - iadeTarihi.getTime();
                                        gecikmeGun = diff / (1000 * 60 * 60 * 24);
                                        cezaTutari = gecikmeGun * 5; // Gün başına 5 TL ceza
                                    }
                                %>
                                <tr <%= isGecikme && !isIadeEdilmis ? "class='table-danger'" : "" %>>
                                    <td><%= odunc.getOgrenciNo() %></td>
                                    <td><%= odunc.getKullaniciAd() + " " + odunc.getKullaniciSoyad() %></td>
                                    <td><%= odunc.getKitapAd() %></td>
                                    <td><%= oduncTarihi != null ? dateFormat.format(oduncTarihi) : "-" %></td>
                                    <td><%= iadeTarihi != null ? dateFormat.format(iadeTarihi) : "-" %></td>
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
                                        <% if (isGecikme) { %>
                                            <%= gecikmeGun %> gün (<%= cezaTutari %> TL ceza)
                                        <% } else { %>
                                            -
                                        <% } %>
                                    </td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                <% } else { %>
                    <div class="alert alert-info">
                        <i class="fas fa-info-circle me-2"></i>Listelenecek ödünç kitap bulunmamaktadır.
                    </div>
                <% } %>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>