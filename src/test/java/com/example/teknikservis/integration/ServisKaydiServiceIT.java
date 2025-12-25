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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
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
    void servis_kaydi_olusturulup_dbde_bulunmali() {
        String uniq = UUID.randomUUID().toString().substring(0, 8);

        // 1) Test musterisi olustur
        Kullanici musteri = new Kullanici();
        musteri.setAd("IT Musteri");
        musteri.setEmail("it.musteri." + uniq + "@test.com");
        musteri.setSifre("1234");
        musteri.setRol(Kullanici.Rol.MUSTERI);
        musteri = kullaniciRepository.save(musteri);

        // 2) Musteriye bagli cihaz olustur
        Cihaz cihaz = new Cihaz();
        cihaz.setMusteri(musteri);
        cihaz.setMarka("IT-MARKA");
        cihaz.setModel("IT-MODEL");
        cihaz.setSeriNo("IT-SERI-" + uniq);
        cihaz = cihazRepository.save(cihaz);

        // 3) Servis kaydi olustur (service uzerinden)
        ServisKaydi kayit = servisKaydiService.createServisKaydi(
                musteri.getId(),
                cihaz.getId(),
                null, // teknisyen yok
                "Integration test ariza kaydi",
                LocalDateTime.now()
        );

        assertNotNull(kayit.getId(), "Olusan kaydin ID'si dolu olmali");

        // 4) Kaydi DB'den tekrar oku ve kontrol et
        ServisKaydi dbKayit = servisKaydiRepository.findById(kayit.getId())
                .orElseThrow(() -> new IllegalStateException("Kayit DB'de bulunamadi"));

        assertEquals(musteri.getId(), dbKayit.getCihaz().getMusteri().getId());
        assertEquals(cihaz.getId(), dbKayit.getCihaz().getId());
        assertEquals("Integration test ariza kaydi", dbKayit.getAciklama());
    }
}
