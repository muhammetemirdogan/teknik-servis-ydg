package com.example.teknikservis.repository;

import com.example.teknikservis.entity.Cihaz;
import com.example.teknikservis.entity.ServisKaydi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ServisKaydiRepository extends JpaRepository<ServisKaydi, Long> {

    boolean existsByCihazAndDurumAndAcilisTarihiBetween(
            Cihaz cihaz,
            ServisKaydi.Durum durum,
            LocalDateTime bas,
            LocalDateTime bit
    );

    // Senaryo 2 için
    List<ServisKaydi> findByCihaz_Musteri_Id(Long musteriId);

    List<ServisKaydi> findByCihaz(Cihaz cihaz);
}
