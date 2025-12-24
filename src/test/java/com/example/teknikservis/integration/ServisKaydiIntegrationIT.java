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

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void yeniServisKaydiOlusturabilmeliyiz() {
        // data.sql icindeki hazir musteri (id=1) kullan
        Kullanici musteri = kullaniciRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("Test icin musteri bulunamadi (id=1)"));

        // Musteriye bagli yeni cihaz
        Cihaz cihaz = new Cihaz();
        cihaz.setMusteri(musteri);
        cihaz.setMarka("IT-MARKA");
        cihaz.setModel("IT-MODEL");
        cihaz.setSeriNo("IT-SERI-999");
        cihaz = cihazRepository.save(cihaz);

        // Servis kaydi olustur
        ServisKaydi kayit = servisKaydiService.createServisKaydi(
                musteri.getId(),
                cihaz.getId(),
                null,
                "Integration test ariza kaydi",
                LocalDateTime.now()
        );

        assertNotNull(kayit.getId(), "Olusan servis kaydinin ID'si dolu olmali");
    }
}
