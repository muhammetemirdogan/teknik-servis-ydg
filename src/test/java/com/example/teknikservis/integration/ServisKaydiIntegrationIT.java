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
import java.util.Optional;

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

    /**
     * Amaç:
     *  - Hazır müşteri (id=1) ile yeni bir cihaz ve servis kaydı oluştur
     *  - Sonra getServisKayitlariForMusteri ile listeleyip,
     *    oluşturulan kaydın listede geldiğini doğrula.
     */
    @Test
    void yeniServisKaydiOlusturupMusteriyeGoreListeleyebilmeliyiz() {

        // data.sql içindeki hazır müşteri: id = 1, Ali Musteri
        Optional<Kullanici> musteriOpt = kullaniciRepository.findById(1L);
        assertTrue(musteriOpt.isPresent(), "id=1 musterisi hazir olmali");
        Kullanici musteri = musteriOpt.get();

        // Bu müşteriye ait bir cihaz oluşturalım
        Cihaz cihaz = new Cihaz();
        cihaz.setMusteri(musteri);
        cihaz.setMarka("IntegrationTest Marka");
        cihaz.setModel("IntegrationTest Model");
        cihaz.setSeriNo("INT-001");
        cihaz = cihazRepository.save(cihaz);

        // Teknisyen zorunlu değilse null, zorunluysa data.sql' den 2. kullaniciyi alabiliriz
        Long teknisyenId = null;
        Optional<Kullanici> teknisyenOpt = kullaniciRepository.findById(2L);
        if (teknisyenOpt.isPresent()) {
            teknisyenId = teknisyenOpt.get().getId();
        }

        // Servis kaydı oluştur
        ServisKaydi kayit = servisKaydiService.createServisKaydi(
                musteri.getId(),
                cihaz.getId(),
                teknisyenId,
                "Integration test ariza kaydi",
                LocalDateTime.now()
        );

        assertNotNull(kayit.getId(), "Servis kaydi kaydedilmeli ve ID donmeli");

        // Müşterinin kayıtlarını listele
        List<ServisKaydi> musteriKayitlari =
                servisKaydiService.getServisKayitlariForMusteri(musteri.getId());

        assertFalse(musteriKayitlari.isEmpty(), "Musteri icin en az bir servis kaydi donebilmeli");
        assertTrue(
                musteriKayitlari.stream().anyMatch(k -> k.getId().equals(kayit.getId())),
                "Olusturdugumuz kayit liste icinde bulunmali"
        );
    }
}
