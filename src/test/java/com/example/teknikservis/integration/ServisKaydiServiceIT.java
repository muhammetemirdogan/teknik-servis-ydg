package com.example.teknikservis.integration;

import com.example.teknikservis.entity.Cihaz;
import com.example.teknikservis.entity.Kullanici;
import com.example.teknikservis.entity.ServisKaydi;
import com.example.teknikservis.entity.Durum; // enum senin projendeki pakete gore olsun
import com.example.teknikservis.repository.CihazRepository;
import com.example.teknikservis.repository.KullaniciRepository;
import com.example.teknikservis.service.ServisKaydiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    void servis_kaydi_olustur_ve_iptal_akisi_basarili() {
        // 1) data.sql'den gelen hazir musteri ve teknisyen
        Kullanici musteri = kullaniciRepository
                .findByEmail("ali.musteri@example.com")
                .orElseThrow(() -> new IllegalStateException("Musteri bulunamadi"));

        Kullanici teknisyen = kullaniciRepository
                .findByEmail("ayse.teknisyen@example.com")
                .orElseThrow(() -> new IllegalStateException("Teknisyen bulunamadi"));

        // 2) Bu musterinin cihazini olustur
        Cihaz cihaz = new Cihaz();
        cihaz.setMarka("Test Marka");
        cihaz.setModel("Model X");
        cihaz.setSeriNo("SN-0001");
        cihaz.setMusteri(musteri);
        cihaz = cihazRepository.save(cihaz);

        // 3) Servis kaydi olustur
        ServisKaydi kayit = servisKaydiService.createServisKaydi(
                musteri.getId(),
                cihaz.getId(),
                teknisyen.getId(),
                "Test ariza",
                LocalDateTime.now()
        );

        assertNotNull(kayit.getId());
        assertEquals(Durum.ACIK, kayit.getDurum());

        // 4) Musteri kendi kaydini iptal ediyor
        servisKaydiService.cancelServisKaydi(kayit.getId(), musteri.getId());

        ServisKaydi iptalEdilmis =
                servisKaydiService.getServisKaydiById(kayit.getId()).orElseThrow();

        assertEquals(Durum.IPTAL, iptalEdilmis.getDurum());
    }
}
