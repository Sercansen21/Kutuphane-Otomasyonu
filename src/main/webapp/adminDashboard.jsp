<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.kutuphane.model.Personel" %>
<%@ page import="com.kutuphane.model.Kitap" %>
<%@ page import="java.util.List" %>
<%
    // Oturum kontrolü
    Personel personel = (Personel) session.getAttribute("personel");
    if (personel == null) {
        response.sendRedirect("login");
        return;
    }
    
    List<Kitap> kitaplar = (List<Kitap>) request.getAttribute("kitaplar");
    String successMessage = (String) request.getAttribute("successMessage");
    String errorMessage = (String) request.getAttribute("errorMessage");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Panel - Kütüphane Yönetim Sistemi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background-color: #f5f5f5;
        }
        .navbar {
            background-color: #343a40;
        }
        .sidebar {
            height: 100vh;
            background-color: #343a40;
            color: white;
            position: fixed;
            width: 250px;
        }
        .sidebar .nav-link {
            color: white;
        }
        .sidebar .nav-link:hover {
            background-color: #4b545c;
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
                </div>
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link active" href="adminDashboard">
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
                <h1 class="mb-4">Yönetim Paneli</h1>
                
                <!-- Başarı/Hata Mesajları -->
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
                
                <!-- İstatistik Kartları -->
                <div class="row mb-4">
                    <div class="col-md-4">
                        <div class="stats-card primary">
                            <div class="icon"><i class="fas fa-book"></i></div>
                            <div class="number"><%= kitaplar != null ? kitaplar.size() : 0 %></div>
                            <div class="title">Toplam Kitap</div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="stats-card success">
                            <div class="icon"><i class="fas fa-users"></i></div>
                            <div class="number">0</div>
                            <div class="title">Aktif Üyeler</div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="stats-card danger">
                            <div class="icon"><i class="fas fa-clock"></i></div>
                            <div class="number">0</div>
                            <div class="title">Günü Geçen Kitaplar</div>
                        </div>
                    </div>
                </div>
                
                <!-- Kitap Ekleme Formu -->
                <div class="card mb-4">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0"><i class="fas fa-plus-circle me-2"></i>Yeni Kitap Ekle</h5>
                    </div>
                    <div class="card-body">
                        <form action="addBook" method="post">
                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label for="isbn" class="form-label">ISBN</label>
                                    <input type="text" class="form-control" id="isbn" name="isbn" required>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label for="kitapAd" class="form-label">Kitap Adı</label>
                                    <input type="text" class="form-control" id="kitapAd" name="kitapAd" required>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label for="sayfaSayisi" class="form-label">Sayfa Sayısı</label>
                                    <input type="number" class="form-control" id="sayfaSayisi" name="sayfaSayisi" required>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="turAd" class="form-label">Kitap Türü</label>
                                    <input type="text" class="form-control" id="turAd" name="turAd" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="yazarAdSoyad" class="form-label">Yazar Adı</label>
                                    <input type="text" class="form-control" id="yazarAdSoyad" name="yazarAdSoyad" required>
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary"><i class="fas fa-save me-2"></i>Kaydet</button>
                        </form>
                    </div>
                </div>
                
                <!-- Son Eklenen Kitaplar Tablosu -->
                <div class="card">
                    <div class="card-header bg-success text-white">
                        <h5 class="mb-0"><i class="fas fa-list me-2"></i>Son Eklenen Kitaplar</h5>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead>
                                    <tr>
                                        <th>ISBN</th>
                                        <th>Kitap Adı</th>
                                        <th>Yazar</th>
                                        <th>Tür</th>
                                        <th>Sayfa Sayısı</th>
                                        <th>İşlemler</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if (kitaplar != null && !kitaplar.isEmpty()) { 
                                        // Sadece son 5 kitabı göster
                                        int limit = Math.min(5, kitaplar.size());
                                        for (int i = 0; i < limit; i++) {
                                            Kitap kitap = kitaplar.get(i);
                                    %>
                                        <tr>
                                            <td><%= kitap.getIsbn() %></td>
                                            <td><%= kitap.getKitapAd() %></td>
                                            <td><%= kitap.getYazarAdSoyad() != null ? kitap.getYazarAdSoyad() : "Belirtilmemiş" %></td>
                                            <td><span class="badge bg-info"><%= kitap.getTurAd() != null ? kitap.getTurAd() : "Belirtilmemiş" %></span></td>
                                            <td><%= kitap.getSayfaSayisi() %></td>
                                            <td>
                                                <a href="kitapDetay?isbn=<%= kitap.getIsbn() %>" class="btn btn-sm btn-primary"><i class="fas fa-eye"></i></a>
                                                <a href="deleteBook?isbn=<%= kitap.getIsbn() %>" class="btn btn-sm btn-danger" onclick="return confirm('Bu kitabı silmek istediğinizden emin misiniz?');"><i class="fas fa-trash"></i></a>
                                            </td>
                                        </tr>
                                    <% } } else { %>
                                        <tr>
                                            <td colspan="6" class="text-center">Henüz kitap eklenmemiş.</td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                        <a href="kitaplar" class="btn btn-outline-primary mt-3">Tüm Kitapları Görüntüle <i class="fas fa-arrow-right ms-1"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>