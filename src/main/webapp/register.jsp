<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Kütüphane Sistemi - Kayıt Ol</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f5f5f5;
        }
        .register-container {
            max-width: 500px;
            margin: 50px auto;
            padding: 30px;
            border-radius: 10px;
            background-color: white;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .register-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .register-header h2 {
            color: #0d6efd;
        }
        .error-message {
            color: red;
            margin-bottom: 15px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="register-container">
            <div class="register-header">
                <h2>Kütüphane Sistemine Kayıt</h2>
                <p>Kullanıcı bilgilerinizi girin</p>
            </div>
            
            <% if(request.getAttribute("errorMessage") != null) { %>
                <div class="error-message text-center">
                    <%= request.getAttribute("errorMessage") %>
                </div>
            <% } %>
            
            <form action="register" method="post">
                <div class="mb-3">
                    <label for="ogrenciNo" class="form-label">Öğrenci Numarası</label>
                    <input type="text" class="form-control" id="ogrenciNo" name="ogrenciNo" required pattern="[0-9]{11}" title="Öğrenci numarası 11 haneli olmalıdır">
                    <div class="form-text">Öğrenci numarası 11 haneli olmalıdır.</div>
                </div>
                <div class="mb-3">
                    <label for="uyeAd" class="form-label">Ad</label>
                    <input type="text" class="form-control" id="uyeAd" name="uyeAd" required>
                </div>
                <div class="mb-3">
                    <label for="uyeSoyad" class="form-label">Soyad</label>
                    <input type="text" class="form-control" id="uyeSoyad" name="uyeSoyad" required>
                </div>
                <div class="mb-3">
                    <label for="iletisim" class="form-label">İletişim (E-posta)</label>
                    <input type="email" class="form-control" id="iletisim" name="iletisim" required>
                </div>
                <div class="mb-3">
                    <label for="sifre" class="form-label">Şifre</label>
                    <input type="password" class="form-control" id="sifre" name="sifre" required minlength="4">
                </div>
                <div class="mb-3">
                    <label for="sifreTekrar" class="form-label">Şifre Tekrar</label>
                    <input type="password" class="form-control" id="sifreTekrar" name="sifreTekrar" required minlength="4">
                </div>
                <button type="submit" class="btn btn-primary w-100 mb-3">Kayıt Ol</button>
                <div class="text-center mt-3">
                    <p>Zaten hesabınız var mı? <a href="login">Giriş Yapın</a></p>
                </div>
            </form>
        </div>
    </div>
    
    <script>
        // Şifre uyumu kontrolü
        document.querySelector('form').addEventListener('submit', function(e) {
            const sifre = document.getElementById('sifre').value;
            const sifreTekrar = document.getElementById('sifreTekrar').value;
            
            if (sifre !== sifreTekrar) {
                e.preventDefault();
                alert('Şifreler eşleşmiyor!');
            }
        });
    </script>
</body>
</html>