package com.example.teknikservis.repository;

import com.example.teknikservis.entity.Cihaz;
import com.example.teknikservis.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CihazRepository extends JpaRepository<Cihaz, Long> {

    java.util.List<Cihaz> findByMusteri(Kullanici musteri);
}
