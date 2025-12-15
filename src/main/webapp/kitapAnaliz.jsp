<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.kutuphane.model.Personel" %>
<%@ page import="com.kutuphane.model.Odunc" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.stream.Collectors" %>
<%
    // Oturum kontrolü
    Personel personel = (Personel) session.getAttribute("personel");
    if (personel == null) {
        response.sendRedirect("login");
        return;
    }
    
    // Veriler
    List<Odunc> oduncListesi = (List<Odunc>) request.getAttribute("oduncListesi");
    Map<String, Integer> kitapIstatistikleri = (Map<String, Integer>) request.getAttribute("kitapIstatistikleri");
    Map<String, String> kitapIsimleri = (Map<String, String>) request.getAttribute("kitapIsimleri");
    Map<String, Integer> kullaniciIstatistikleri = (Map<String, Integer>) request.getAttribute("kullaniciIstatistikleri");
    Map<String, String> kullaniciIsimleri = (Map<String, String>) request.getAttribute("kullaniciIsimleri");
    Integer kitapSayisi = (Integer) request.getAttribute("kitapSayisi");
    
    // En çok ödünç alınan kitaplar (ilk 5)
    List<Map.Entry<String, Integer>> enCokOduncAlinanKitaplar = null;
    if (kitapIstatistikleri != null) {
        enCokOduncAlinanKitaplar = kitapIstatistikleri.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(5)
            .collect(Collectors.toList());
    }
    
    // En çok kitap ödünç alan kullanıcılar (ilk 5)
    List<Map.Entry<String, Integer>> enCokKitapOduncAlanKullanicilar = null;
    if (kullaniciIstatistikleri != null) {
        enCokKitapOduncAlanKullanicilar = kullaniciIstatistikleri.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(5)
            .collect(Collectors.toList());
    }
    
    // Gecikmiş kitaplar
    List<Odunc> gecikmisBorclar = null;
    if (oduncListesi != null) {
        gecikmisBorclar = oduncListesi.stream()
            .filter(Odunc::isGecikme)
            .collect(Collectors.toList());
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Kitap Analizleri - Kütüphane Sistemi</title>
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
        .stats-card {
            border-radius: 15px;
            padding: 20px;
            margin-bottom: 20px;
            color: white;
            position: relative;
            overflow: hidden;
        }
        .stats-card.primary { background-color: #0d6efd; }
        .stats-card.success { background-color: #198754; }
        .stats-card.danger { background-color: #dc3545; }
        .stats-card .number {
            font-size: 36px;
            font-weight: bold;
        }
        .stats-card .title {
            font-size: 18px;
            opacity: 0.8;
        }
        .stats-card .icon {
            position: absolute;
            top: 20px;
            right: 20px;
            font-size: 48px;
            opacity: 0.3;
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
                        <a class="nav-link" href="oduncListesi">
                            <i class="fas fa-list-alt me-2"></i> Ödünç Listesi
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="kitapAnaliz">
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
                <h1 class="mb-4"><i class="fas fa-chart-bar me-2"></i>Kitap Takip ve Analizleri</h1>
                
                <!-- İstatistik Kartları -->
                <div class="row mb-4">
                    <div class="col-md-4">
                        <div class="stats-card primary">
                            <div class="icon"><i class="fas fa-book"></i></div>
                            <div class="number"><%= kitapSayisi != null ? kitapSayisi : 0 %></div>
                            <div class="title">Toplam Kitap</div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="stats-card success">
                            <div class="icon"><i class="fas fa-exchange-alt"></i></div>
                            <div class="number"><%= oduncListesi != null ? oduncListesi.size() : 0 %></div>
                            <div class="title">Toplam Ödünç İşlemi</div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="stats-card danger">
                            <div class="icon"><i class="fas fa-exclamation-triangle"></i></div>
                            <div class="number"><%= gecikmisBorclar != null ? gecikmisBorclar.size() : 0 %></div>
                            <div class="title">Gecikmiş Kitaplar</div>
                        </div>
                    </div>
                </div>
                
                <!-- İstatistik Grafikleri ve Tabloları -->
                <div class="row">
                    <!-- En Çok Ödünç Alınan Kitaplar -->
                    <div class="col-md-6 mb-4">
                        <div class="card">
                            <div class="card-header bg-primary text-white">
                                <h5 class="mb-0"><i class="fas fa-crown me-2"></i>En Çok Ödünç Alınan Kitaplar</h5>
                            </div>
                            <div class="card-body">
                                <% if (enCokOduncAlinanKitaplar != null && !enCokOduncAlinanKitaplar.isEmpty()) { %>
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>Kitap</th>
                                                <th>ISBN</th>
                                                <th>Ödünç Sayısı</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% for (Map.Entry<String, Integer> entry : enCokOduncAlinanKitaplar) { %>
                                                <tr>
                                                    <td><%= kitapIsimleri.getOrDefault(entry.getKey(), "Bilinmeyen Kitap") %></td>
                                                    <td><%= entry.getKey() %></td>
                                                    <td><span class="badge bg-primary"><%= entry.getValue() %></span></td>
                                                </tr>
                                            <% } %>
                                        </tbody>
                                    </table>
                                <% } else { %>
                                    <div class="alert alert-info">
                                        <i class="fas fa-info-circle me-2"></i>Henüz kitap ödünç alınmamış.
                                    </div>
                                <% } %>
                            </div>
                        </div>
                    </div>
                    
                    <!-- En Çok Kitap Ödünç Alan Kullanıcılar -->
                    <div class="col-md-6 mb-4">
                        <div class="card">
                            <div class="card-header bg-success text-white">
                                <h5 class="mb-0"><i class="fas fa-user-graduate me-2"></i>En Çok Kitap Ödünç Alan Kullanıcılar</h5>
                            </div>
                            <div class="card-body">
                                <% if (enCokKitapOduncAlanKullanicilar != null && !enCokKitapOduncAlanKullanicilar.isEmpty()) { %>
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>Kullanıcı</th>
                                                <th>Öğrenci No</th>
                                                <th>Ödünç Sayısı</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% for (Map.Entry<String, Integer> entry : enCokKitapOduncAlanKullanicilar) { %>
                                                <tr>
                                                    <td><%= kullaniciIsimleri.getOrDefault(entry.getKey(), "Bilinmeyen Kullanıcı") %></td>
                                                    <td><%= entry.getKey() %></td>
                                                    <td><span class="badge bg-success"><%= entry.getValue() %></span></td>
                                                </tr>
                                            <% } %>
                                        </tbody>
                                    </table>
                                <% } else { %>
                                    <div class="alert alert-info">
                                        <i class="fas fa-info-circle me-2"></i>Henüz kitap ödünç alan kullanıcı yok.
                                    </div>
                                <% } %>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Gecikmiş Kitaplar -->
                    <div class="col-md-12 mb-4">
                        <div class="card">
                            <div class="card-header bg-danger text-white">
                                <h5 class="mb-0"><i class="fas fa-exclamation-triangle me-2"></i>Gecikmiş Kitaplar</h5>
                            </div>
                            <div class="card-body">
                                <% if (gecikmisBorclar != null && !gecikmisBorclar.isEmpty()) { %>
                                    <div class="table-responsive">
                                        <table class="table table-striped table-hover">
                                            <thead>
                                                <tr>
                                                    <th>Kitap</th>
                                                    <th>Öğrenci</th>
                                                    <th>Alım Tarihi</th>
                                                    <th>Son İade Tarihi</th>
                                                    <th>Durum</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <% for (Odunc odunc : gecikmisBorclar) { %>
                                                    <tr>
                                                        <td><%= odunc.getKitapAd() != null ? odunc.getKitapAd() : "Bilinmeyen Kitap" %></td>
                                                        <td><%= odunc.getKullaniciAd() + " " + odunc.getKullaniciSoyad() %></td>
                                                        <td><%= odunc.getAlimTarihi() %></td>
                                                        <td><%= odunc.getSonIadeTarihi() %></td>
                                                        <td>
                                                            <% if (odunc.isIadeEdilmis()) { %>
                                                                <span class="badge bg-warning">Gecikmeli İade</span>
                                                            <% } else { %>
                                                                <span class="badge bg-danger">Gecikmiş</span>
                                                            <% } %>
                                                        </td>
                                                    </tr>
                                                <% } %>
                                            </tbody>
                                        </table>
                                    </div>
                                <% } else { %>
                                    <div class="alert alert-success">
                                        <i class="fas fa-check-circle me-2"></i>Gecikmiş kitap bulunmuyor.
                                    </div>
                                <% } %>
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