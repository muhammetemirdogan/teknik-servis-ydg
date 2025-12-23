package com.example.teknikservis.service;

import com.example.teknikservis.entity.ServisKaydi;

import java.time.LocalDateTime;
import java.util.List;

public interface ServisKaydiService {

    ServisKaydi createServisKaydi(Long musteriId,
                                  Long cihazId,
                                  Long teknisyenId,
                                  String aciklama,
                                  LocalDateTime acilisTarihi);

    void cancelServisKaydi(Long servisKaydiId, Long musteriId);

    java.util.List<ServisKaydi> getServisKayitlariForMusteri(Long musteriId);

    java.util.List<ServisKaydi> getAllServisKayitlari();
}
