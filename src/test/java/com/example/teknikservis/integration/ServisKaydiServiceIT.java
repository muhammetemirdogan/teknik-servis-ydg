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
import java.util.Optional;

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

    /**
     * Amaç: Servis kaydı oluşturulurken;
     *  - hazır müşteriyi (id=1) kullanalım
     *  - o müşteriye bağlı yeni bir cihaz oluşturalım
     *  - createServisKaydi ile kayıt açalım
     *  - ilişkilerin doğru kurulduğunu doğrulayalım
     */
    @Test
    void servis_kaydi_olusturulurken_musteri_ve_cihaz_baglantisi_kurulur() {

        // data.sql içindeki hazır müşteri: id = 1, Ali Musteri
        Optional<Kullanici> musteriOpt = kullaniciRepository.findById(1L);
        assertTrue(musteriOpt.isPresent(), "id=1 musterisi hazir olmali");
        Kullanici musteri = musteriOpt.get();

        // Bu müşteriye ait bir cihaz oluşturalım
        Cihaz cihaz = new Cihaz();
        cihaz.setMusteri(musteri);
        cihaz.setMarka("ServiceIT Marka");
        cihaz.setModel("ServiceIT Model");
        cihaz.setSeriNo("SVC-001");
        cihaz = cihazRepository.save(cihaz);

        // Servis kaydı oluştur
        ServisKaydi kayit = servisKaydiService.createServisKaydi(
                musteri.getId(),
                cihaz.getId(),
                null, // teknisyen opsiyonel, null gönderdik
                "Service IT ariza kaydi",
                LocalDateTime.now()
        );

        // ASSERTIONLAR
        assertNotNull(kayit.getId(), "Servis kaydi kaydedilmeli");
        assertNotNull(kayit.getCihaz(), "Servis kaydinin bagli oldugu bir cihaz olmali");
        assertEquals(musteri.getId(),
                kayit.getCihaz().getMusteri().getId(),
                "Servis kaydinin musterisi bekledigimiz musteri olmali");
    }
}
