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
class ServisKaydiServiceIT {

    @Autowired
    private ServisKaydiService servisKaydiService;

    @Autowired
    private KullaniciRepository kullaniciRepository;

    @Autowired
    private CihazRepository cihazRepository;

    @Test
    void servis_kaydi_olustur_ve_musteriye_gore_listeleyebiliriz() {
        // data.sql icindeki hazir musteri (id=1) kullaniliyor
        Kullanici musteri = kullaniciRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("Test icin musteri bulunamadi (id=1)"));

        // Musteriye ait yeni bir cihaz olustur
        Cihaz cihaz = new Cihaz();
        cihaz.setMusteri(musteri);
        cihaz.setMarka("TestMarka");
        cihaz.setModel("TestModel");
        cihaz.setSeriNo("SER123");
        cihaz = cihazRepository.save(cihaz);

        // Servis kaydi olustur
        ServisKaydi kayit = servisKaydiService.createServisKaydi(
                musteri.getId(),
                cihaz.getId(),
                null, // teknisyenId simdilik null
                "Test ariza kaydi",
                LocalDateTime.now()
        );

        assertNotNull(kayit.getId(), "Servis kaydinin ID'si dolu olmalı");

        // Musteri icin servis kayitlari listelendiginizde bu kayit gelmeli
        List<ServisKaydi> musteriKayitlari =
                servisKaydiService.getServisKayitlariForMusteri(musteri.getId());

        assertFalse(musteriKayitlari.isEmpty(), "Musterinin en az bir servis kaydi olmali");
    }
}
