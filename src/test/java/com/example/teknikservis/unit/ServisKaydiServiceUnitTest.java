package com.example.teknikservis.integration;

import com.example.teknikservis.entity.Cihaz;
import com.example.teknikservis.entity.Kullanici;
import com.example.teknikservis.entity.ServisKaydi;
import com.example.teknikservis.repository.CihazRepository;
import com.example.teknikservis.repository.KullaniciRepository;
import com.example.teknikservis.repository.ServisKaydiRepository;
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

    @Autowired
    private ServisKaydiRepository servisKaydiRepository;

    @Test
    void servis_kaydi_olustur_ve_iptal_akisi_basarili() {
        // 1) Önce müşteri ve teknisyen oluştur
        Kullanici musteri = new Kullanici();
        musteri.setAd("IT Test Musteri");
        musteri.setEmail("it.musteri@example.com");
        musteri.setSifre("123456");
        musteri.setRol(Kullanici.Rol.MUSTERI);
        musteri = kullaniciRepository.save(musteri);

        Kullanici teknisyen = new Kullanici();
        teknisyen.setAd("IT Test Teknisyen");
        teknisyen.setEmail("it.teknisyen@example.com");
        teknisyen.setSifre("123456");
        teknisyen.setRol(Kullanici.Rol.TEKNISYEN);
        teknisyen = kullaniciRepository.save(teknisyen);

        // 2) Müşteriye ait cihaz ekle
        Cihaz cihaz = new Cihaz();
        cihaz.setMarka("Samsung");
        cihaz.setModel("TV");
        cihaz.setSeriNo("ABC123IT");
        cihaz.setMusteri(musteri);
        cihaz = cihazRepository.save(cihaz);

        // 3) Servis kaydını servis katmanı üzerinden oluştur
        LocalDateTime acilis = LocalDateTime.of(2025, 1, 10, 10, 0);

        ServisKaydi kayit = servisKaydiService.createServisKaydi(
                musteri.getId(),
                cihaz.getId(),
                teknisyen.getId(),
                "Goruntu gitme sorunu",
                acilis
        );

        assertNotNull(kayit.getId(), "Kayit olusurken id atanmis olmali");
        assertEquals(ServisKaydi.Durum.ACIK, kayit.getDurum(), "Yeni kaydin durumu ACIK olmali");

        // 4) Kaydı iptal et
        servisKaydiService.cancelServisKaydi(kayit.getId(), musteri.getId());

        // 5) Veritabanından tekrar çekip durumunu doğrula
        ServisKaydi guncel = servisKaydiRepository.findById(kayit.getId())
                .orElseThrow(() -> new IllegalStateException("Kayit bulunamadi"));

        assertEquals(ServisKaydi.Durum.IPTAL, guncel.getDurum(), "Iptal edilen kaydin durumu IPTAL olmali");
        assertNotNull(guncel.getKapanisTarihi(), "Iptal edilen kaydin kapanisTarihi set edilmeli");
    }
}
