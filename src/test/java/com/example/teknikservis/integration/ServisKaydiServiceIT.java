package com.example.teknikservis.integration;

import com.example.teknikservis.entity.Cihaz;
import com.example.teknikservis.entity.Kullanici;
import com.example.teknikservis.entity.ServisDurumu;
import com.example.teknikservis.entity.ServisKaydi;
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
        // 1) Hazir musteriyi ve teknisyeni kullan (seed data)
        Kullanici musteri = kullaniciRepository.findByEmail("ali.musteri@example.com")
                .orElseThrow(() -> new IllegalStateException(
                        "Test icin 'Ali Musteri' kullanicisi bulunamadi"
                ));

        Kullanici teknisyen = kullaniciRepository.findByEmail("ayse.teknisyen@example.com")
                .orElseThrow(() -> new IllegalStateException(
                        "Test icin 'Ayse Teknisyen' kullanicisi bulunamadi"
                ));

        // 2) Bu musterinin bir cihazini olustur
        Cihaz cihaz = new Cihaz();
        cihaz.setMarka("TestMarka-ServisIT");
        cihaz.setModel("TestModel-ServisIT");
        cihaz.setSeriNo("SN-IT-002");
        cihaz.setMusteri(musteri);
        cihaz = cihazRepository.save(cihaz);

        // 3) Servis kaydi olustur
        ServisKaydi kayit = servisKaydiService.createServisKaydi(
                musteri.getId(),
                cihaz.getId(),
                teknisyen.getId(),
                "Servis kaydi IT testi",
                LocalDateTime.now()
        );

        assertNotNull(kayit.getId(), "Olusan servis kaydinin ID'si olmali");
        assertEquals(ServisDurumu.ACIK, kayit.getDurum(), "Yeni servis kaydi ACIK baslamali");

        // 4) İptal akisini dene (servisKaydiService'de böyle bir metot varsa)
        kayit = servisKaydiService.cancelServisKaydi(kayit.getId(), "Musteri iptal etti");

        assertEquals(ServisDurumu.IPTAL, kayit.getDurum(), "Iptal sonrasi durum IPTAL olmali");
        assertNotNull(kayit.getKapanisTarihi(), "Iptal edilen kaydin kapanis tarihi set edilmeli");
    }
}
