package com.example.teknikservis.service.impl;

import com.example.teknikservis.entity.Cihaz;
import com.example.teknikservis.entity.Kullanici;
import com.example.teknikservis.entity.ServisKaydi;
import com.example.teknikservis.repository.CihazRepository;
import com.example.teknikservis.repository.KullaniciRepository;
import com.example.teknikservis.repository.ServisKaydiRepository;
import com.example.teknikservis.service.ServisKaydiService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ServisKaydiServiceImpl implements ServisKaydiService {

    private final ServisKaydiRepository servisKaydiRepository;
    private final KullaniciRepository kullaniciRepository;
    private final CihazRepository cihazRepository;

    public ServisKaydiServiceImpl(ServisKaydiRepository servisKaydiRepository,
                                  KullaniciRepository kullaniciRepository,
                                  CihazRepository cihazRepository) {
        this.servisKaydiRepository = servisKaydiRepository;
        this.kullaniciRepository = kullaniciRepository;
        this.cihazRepository = cihazRepository;
    }

    @Override
    @Transactional
    public ServisKaydi createServisKaydi(Long musteriId,
                                         Long cihazId,
                                         Long teknisyenId,
                                         String aciklama,
                                         LocalDateTime acilisTarihi) {

        Kullanici musteri = kullaniciRepository.findById(musteriId)
                .orElseThrow(() -> new IllegalArgumentException("Musteri bulunamadi"));

        Cihaz cihaz = cihazRepository.findById(cihazId)
                .orElseThrow(() -> new IllegalArgumentException("Cihaz bulunamadi"));

        if (cihaz.getMusteri() == null || !cihaz.getMusteri().getId().equals(musteri.getId())) {
            throw new IllegalStateException("Bu cihaz bu musteriye ait degil");
        }

        LocalDate gun = acilisTarihi.toLocalDate();
        LocalDateTime bas = gun.atStartOfDay();
        LocalDateTime bit = gun.atTime(LocalTime.MAX);

        boolean cakisma = servisKaydiRepository.existsByCihazAndDurumAndAcilisTarihiBetween(
                cihaz,
                ServisKaydi.Durum.ACIK,
                bas,
                bit
        );

        if (cakisma) {
            throw new IllegalStateException("Bu cihaz icin bugun zaten acik servis kaydi var");
        }

        ServisKaydi kayit = new ServisKaydi();
        kayit.setCihaz(cihaz);
        kayit.setTeknisyen(null); // teknisyen atamasi daha sonra yapilabilir
        kayit.setAciklama(aciklama);
        kayit.setAcilisTarihi(acilisTarihi);
        kayit.setDurum(ServisKaydi.Durum.ACIK);

        return servisKaydiRepository.save(kayit);
    }

    @Override
    @Transactional
    public void cancelServisKaydi(Long servisKaydiId, Long musteriId) {
        ServisKaydi kayit = servisKaydiRepository.findById(servisKaydiId)
                .orElseThrow(() -> new IllegalArgumentException("Servis kaydi bulunamadi"));

        if (kayit.getCihaz() == null || kayit.getCihaz().getMusteri() == null) {
            throw new IllegalStateException("Kayit ile iliskili musteri bulunamadi");
        }

        Long cihazMusteriId = kayit.getCihaz().getMusteri().getId();
        if (!cihazMusteriId.equals(musteriId)) {
            throw new IllegalStateException("Bu kaydi iptal etmeye yetkiniz yok");
        }

        kayit.setDurum(ServisKaydi.Durum.IPTAL);
        kayit.setKapanisTarihi(LocalDateTime.now());
        servisKaydiRepository.save(kayit);
    }

    @Override
    public List<ServisKaydi> getServisKayitlariForMusteri(Long musteriId) {
        Kullanici musteri = kullaniciRepository.findById(musteriId)
                .orElseThrow(() -> new IllegalArgumentException("Musteri bulunamadi"));

        List<Cihaz> cihazlar = cihazRepository.findByMusteri(musteri);
        return cihazlar.stream()
                .flatMap(c -> servisKaydiRepository.findByCihaz(c).stream())
                .toList();
    }

    @Override
    public List<ServisKaydi> getAllServisKayitlari() {
        return servisKaydiRepository.findAll();
    }
}
