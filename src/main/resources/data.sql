-- 1) Musteri (kullanici tablosu)
INSERT INTO kullanici (id, ad, email, sifre, rol)
VALUES (1, 'Mehmet', 'mehmet@test.com', '1234', 'MUSTERI');

-- 2) Teknisyen
INSERT INTO kullanici (id, ad, email, sifre, rol)
VALUES (2, 'Ali Usta', 'ali@test.com', '1234', 'TEKNISYEN');

-- 3) Cihaz (musteri_id FK -> kullanici.id)
INSERT INTO cihaz (id, musteri_id, marka, model, seri_no)
VALUES (1, 1, 'Lenovo', 'ThinkPad', 'SN-001');

-- 4) Servis kaydi (cihaz_id, teknisyen_id FK)
INSERT INTO servis_kaydi (id, acilis_tarihi, kapanis_tarihi, aciklama, durum, cihaz_id, teknisyen_id)
VALUES (1, CURRENT_TIMESTAMP, NULL, 'Ekran kırık', 'ACIK', 1, 2);
