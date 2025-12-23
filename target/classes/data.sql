INSERT INTO kullanici (id, ad, email, sifre, rol)
        VALUES (1, 'Ali Musteri', 'ali.musteri@example.com', '123456', 'MUSTERI');

        INSERT INTO kullanici (id, ad, email, sifre, rol)
        VALUES (2, 'Ayse Teknisyen', 'ayse.teknisyen@example.com', '123456', 'TEKNISYEN');

        INSERT INTO cihaz (id, marka, model, seri_no, musteri_id)
        VALUES (1, 'Samsung', 'TV', 'SN123', 1);

        INSERT INTO servis_kaydi (id, aciklama, acilis_tarihi, durum, kapanis_tarihi, cihaz_id, teknisyen_id)
        VALUES (1, 'Ekran kirik', CURRENT_TIMESTAMP(), 'ACIK', NULL, 1, 2);
