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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ServisKaydiIntegrationIT {

    @Autowired
    private ServisKaydiService servisKaydiService;

    @Autowired
    private KullaniciRepository kullaniciRepository;

    @Autowired
    private CihazRepository cihazRepository;

    @Test
    void yeniServisKaydiOlusturupMusteriyeGoreListeleyebilmeliyiz() {
        String uniq = UUID.randomUUID().toString().substring(0, 8);

        // 1) Test musterisi olustur
        Kullanici musteri = new Kullanici();
        musteri.setAd("Integration Musteri");
        musteri.setEmail("integration.musteri." + uniq + "@test.com");
        musteri.setSifre("1234");
        musteri.setRol(Kullanici.Rol.MUSTERI);
        musteri = kullaniciRepository.save(musteri);

        // 2) Teknisyen olustur (opsiyonel ama saglam)
        Kullanici teknisyen = new Kullanici();
        teknisyen.setAd("Integration Teknisyen");
        teknisyen.setEmail("integration.teknisyen." + uniq + "@test.com");
        teknisyen.setSifre("1234");
        teknisyen.setRol(Kullanici.Rol.TEKNISYEN);
        teknisyen = kullaniciRepository.save(teknisyen);

        // 3) Musteriye ait cihaz olustur
        Cihaz cihaz = new Cihaz();
        cihaz.setMusteri(musteri);
        cihaz.setMarka("IntegrationTest Marka");
        cihaz.setModel("IntegrationTest Model");
        cihaz.setSeriNo("INT-" + uniq);
        cihaz = cihazRepository.save(cihaz);

        // 4) Servis kaydi olustur
        ServisKaydi kayit = servisKaydiService.createServisKaydi(
                musteri.getId(),
                cihaz.getId(),
                teknisyen.getId(),
                "Integration test ariza kaydi",
                LocalDateTime.now()
        );

        assertNotNull(kayit.getId(), "Servis kaydi kaydedilmeli ve ID donmeli");

        // 5) Musterinin kayitlarini listele
        List<ServisKaydi> musteriKayitlari =
                servisKaydiService.getServisKayitlariForMusteri(musteri.getId());

        assertFalse(musteriKayitlari.isEmpty(), "Musteri icin en az bir servis kaydi donebilmeli");
        assertTrue(
                musteriKayitlari.stream().anyMatch(k -> k.getId().equals(kayit.getId())),
                "Olusturdugumuz kayit liste icinde bulunmali"
        );
    }
}
