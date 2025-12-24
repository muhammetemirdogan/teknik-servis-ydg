package com.example.teknikservis.integration;

import com.example.teknikservis.entity.Cihaz;
import com.example.teknikservis.entity.Kullanici;
import com.example.teknikservis.entity.ServisKaydi;
import com.example.teknikservis.repository.CihazRepository;
import com.example.teknikservis.repository.KullaniciRepository;
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
        // 1) HAZIR MÜŞTERİ KULLAN: Ali Musteri
        Kullanici musteri = kullaniciRepository.findByEmail("ali.musteri@example.com")
                .orElseThrow(() -> new IllegalStateException(
                        "Test icin 'Ali Musteri' kullanicisi bulunamadi (data.sql / seed data kontrol et)"
                ));

        // 2) Bu müşteriye ait yeni bir cihaz oluştur
        Cihaz cihaz = new Cihaz();
        cihaz.setMarka("TestMarka-IT");
        cihaz.setModel("TestModel-IT");
        cihaz.setSeriNo("SN-IT-001");
        cihaz.setMusteri(musteri);
        cihaz = cihazRepository.save(cihaz);

        // 3) Servis kaydı oluştur
        ServisKaydi kayit = servisKaydiService.createServisKaydi(
                musteri.getId(),
                cihaz.getId(),
                null, // teknisyen zorunlu değilse null
                "Integration testi ariza kaydi",
                LocalDateTime.now()
        );

        assertNotNull(kayit.getId(), "Servis kaydinin bir ID'si olmali");

        // 4) Musteriye gore kayitlari çekince bu kayıt gelmeli
        List<ServisKaydi> musteriKayitlari =
                servisKaydiService.getServisKayitlariForMusteri(musteri.getId());

        assertFalse(musteriKayitlari.isEmpty(), "Musterinin en az bir servis kaydi olmali");
        assertTrue(
                musteriKayitlari.stream().anyMatch(k -> k.getId().equals(kayit.getId())),
                "Olusturulan servis kaydi musterinin listesinde bulunmali"
        );
    }
}
