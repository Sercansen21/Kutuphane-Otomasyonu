# 📚 Kütüphane Otomasyon Sistemi

Bu proje, geleneksel kütüphane yönetim süreçlerini dijitalleştirmek; kitap takibi, kullanıcı kayıtları ve ödünç alma/iade işlemlerini daha düzenli, hızlı ve hatasız hale getirmek amacıyla geliştirilmiş bir web uygulamasıdır.

## 👥 Proje Ekibi
* **Sercan Şen**
* **Batuhan Nacitarhan**

---

## ✨ Temel Özellikler

* **Güvenli Kimlik Doğrulama:** Kullanıcı (Üye) ve Personel (Yönetici) rolleri için ayrı giriş sistemleri.
* **Envanter Yönetimi:** Personel için kitap ekleme, silme ve güncelleme yetkileri.
* **Ödünç/İade Takibi:** Kullanıcıların kitap ödünç alabilmesi ve iade süreçlerinin sistem üzerinden takibi.
* **Arama ve Filtreleme:** Kitapların ISBN, yazar ve tür bilgilerine göre kolayca bulunabilmesi.

---

## 🛠️ Kullanılan Teknolojiler

* **Dil:** Java
* **Web Teknolojileri:** Java Servlet, JSP (JavaServer Pages)
* **Veritabanı:** PostgreSQL
* **Bağımlılık Yönetimi:** Maven
* **Sunucu:** Apache Tomcat
* **IDE:** Eclipse

---

## 🧩 Uygulanan Tasarım Kalıpları (Design Patterns)

Proje kapsamında **Nesneye Yönelik Yazılım Mühendisliği** prensiplerine uygun olarak şu kalıplar kullanılmıştır:

1.  **Singleton Pattern (Oturum Yönetimi):** Uygulama genelinde oturum ve kimlik doğrulama işlemlerinin tek bir merkezden tutarlı şekilde yönetilmesini sağlar.
2.  **Facade Pattern (Kitap İşlemleri):** Arka plandaki karmaşık veritabanı ve iş mantığı süreçlerini sadeleştirerek tek bir arayüz üzerinden kitap yönetimini kolaylaştırır.
3.  **Observer Pattern (Bildirim Sistemi):** Kitapların iade süreleri yaklaştığında veya geciktiğinde ilgili tarafları bilgilendirmek için kullanılan dinamik bir yapı sunar.

---

## 📈 Yazılım Geliştirme Metodolojisi

Proje süreci **Scrum Metodolojisi** üzerine kurgulanmıştır. 
* 3 haftalık kısa iterasyonlar (sprint) kullanılmıştır.
* Özellikler önceliklendirilerek her aşamada test edilebilir modüller geliştirilmiştir.
* Ekip içi koordinasyon ve özellik entegrasyonu Scrum prensipleriyle yönetilmiştir.

---

## 🚀 Kurulum ve Çalıştırma

1.  **Veritabanı:** PostgreSQL üzerinde bir veritabanı oluşturun ve `DBConnection.java` dosyasındaki kullanıcı adı/şifre bilgilerini güncelleyin.
2.  **Bağımlılıklar:** Proje klasöründeki `pom.xml` dosyasını kullanarak Maven bağımlılıklarını (özellikle PostgreSQL sürücüsünü) yükleyin.
3.  **Deployment:** Projeyi Tomcat gibi bir sunucuya deploy edin.
4.  **Erişim:** Tarayıcınızdan `http://localhost:8080/KutuphaneYonetimSistemi/` adresine giderek uygulamayı başlatın.
