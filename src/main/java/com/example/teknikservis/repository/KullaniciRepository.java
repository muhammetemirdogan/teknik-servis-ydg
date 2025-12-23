package com.example.teknikservis.repository;

import com.example.teknikservis.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KullaniciRepository extends JpaRepository<Kullanici, Long> {
}
