-- Kullanıcılar
INSERT INTO kullanici (id, ad, email, sifre, rol)
VALUES (1, 'Ali Musteri', 'ali@test.com', '123', 'MUSTERI');

INSERT INTO kullanici (id, ad, email, sifre, rol)
VALUES (2, 'Veli Teknisyen', 'veli@test.com', '123', 'TEKNISYEN');

-- Cihaz
INSERT INTO cihaz (id, musteri_id, marka, model, seri_no)
VALUES (1, 1, 'Samsung', 'A50', 'SN123');

-- Servis Kaydı (beklenen stringler burada)
INSERT INTO servis_kaydi (id, cihaz_id, teknisyen_id, aciklama, durum, acilis_tarihi, kapanis_tarihi)
VALUES (1, 1, 2, 'Ekran kirik', 'ACIK', CURRENT_TIMESTAMP, NULL);
