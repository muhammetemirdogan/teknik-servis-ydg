package com.example.teknikservis.integration;

import com.example.teknikservis.entity.Kullanici;
import com.example.teknikservis.entity.Cihaz;
import com.example.teknikservis.entity.ServisKaydi;
import com.example.teknikservis.repository.KullaniciRepository;
import com.example.teknikservis.repository.CihazRepository;
import com.example.teknikservis.service.ServisKaydiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ServisKaydiIntegrationIT {

    @Autowired
    private ServisKaydiService servisKaydiService;

    @Autowired
    private KullaniciRepository kullaniciRepository;

    @Autowired
    private CihazRepository cihazRepository;

    @Test
    void yeniServisKaydiOlusturupMusteriyeGoreListeleyebilmeliyiz() {
        // 1) Veritabanina test icin bir musteri ve cihaz kaydet
        Kullanici musteri = new Kullanici();
        musteri = kullaniciRepository.save(musteri);

        Cihaz cihaz = new Cihaz();
        cihaz = cihazRepository.save(cihaz);

        // 2) ServisKaydiService uzerinden yeni bir servis kaydi olustur
        ServisKaydi kayit = servisKaydiService.createServisKaydi(
                musteri.getId(),
                cihaz.getId(),
                null,                           // teknisyenId zorunlu degilse null
                "Test ariza kaydi",
                LocalDateTime.now()
        );

        // 3) Kaydin gerçekten olustugunu kontrol et
        assertNotNull(kayit.getId(), "Servis kaydi kaydedilmeli ve ID donmeli");

        // 4) Musteriye gore listeleyince bu kayit gelmeli
        List<ServisKaydi> musteriKayitlari =
                servisKaydiService.getServisKayitlariForMusteri(musteri.getId());

        assertFalse(musteriKayitlari.isEmpty(), "Musteri icin en az bir servis kaydi donebilmeli");
    }
}
