<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Kütüphane Sistemi - Giriş</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f5f5f5;
        }
        .login-container {
            max-width: 400px;
            margin: 100px auto;
            padding: 30px;
            border-radius: 10px;
            background-color: white;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .login-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .login-header h2 {
            color: #0d6efd;
        }
        .error-message {
            color: red;
            margin-bottom: 15px;
        }
        .user-type-selector {
            display: flex;
            margin-bottom: 20px;
            border-radius: 4px;
            overflow: hidden;
        }
        .user-type-btn {
            flex: 1;
            padding: 10px;
            border: none;
            background-color: #e9ecef;
            cursor: pointer;
            transition: all 0.3s;
        }
        .user-type-btn.active {
            background-color: #0d6efd;
            color: white;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="login-container">
            <div class="login-header">
                <h2>Kütüphane Sistemi</h2>
                <p>Giriş yaparak devam edin</p>
            </div>
            
            <% if(request.getAttribute("errorMessage") != null) { %>
                <div class="error-message text-center">
                    <%= request.getAttribute("errorMessage") %>
                </div>
            <% } %>
            
            <div class="user-type-selector">
                <button type="button" class="user-type-btn active" id="userBtn">Öğrenci Girişi</button>
                <button type="button" class="user-type-btn" id="adminBtn">Personel Girişi</button>
            </div>
            
            <form action="login" method="post" id="userForm">
                <input type="hidden" name="userType" value="student">
                <div class="mb-3">
                    <label for="userId" class="form-label">Öğrenci Numarası</label>
                    <input type="text" class="form-control" id="userId" name="userId" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Şifre</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                <button type="submit" class="btn btn-primary w-100 mb-3">Giriş Yap</button>
            </form>
            
            <form action="login" method="post" id="adminForm" style="display: none;">
                <input type="hidden" name="userType" value="admin">
                <div class="mb-3">
                    <label for="adminId" class="form-label">Personel TC</label>
                    <input type="text" class="form-control" id="adminId" name="userId" required>
                </div>
                <div class="mb-3">
                    <label for="adminPassword" class="form-label">Şifre</label>
                    <input type="password" class="form-control" id="adminPassword" name="password" required>
                </div>
                <button type="submit" class="btn btn-primary w-100 mb-3">Giriş Yap</button>
            </form>
            
            <!-- Kayıt olma linki ekledik -->
            <div class="text-center mt-3">
                <p>Hesabınız yok mu? <a href="register">Kayıt Olun</a></p>
            </div>
        </div>
    </div>

    <script>
        document.getElementById('userBtn').addEventListener('click', function() {
            this.classList.add('active');
            document.getElementById('adminBtn').classList.remove('active');
            document.getElementById('userForm').style.display = 'block';
            document.getElementById('adminForm').style.display = 'none';
        });
        
        document.getElementById('adminBtn').addEventListener('click', function() {
            this.classList.add('active');
            document.getElementById('userBtn').classList.remove('active');
            document.getElementById('userForm').style.display = 'none';
            document.getElementById('adminForm').style.display = 'block';
        });
    </script>
</body>
</html>