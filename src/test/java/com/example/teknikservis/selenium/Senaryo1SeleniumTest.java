package com.example.teknikservis.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Senaryo 1:
 *   /api/servis-kayitlari endpoint'i calisiyor mu ve
 *   ornek servis kayitlarini donuyor mu kontrol eder.
 *
 * Beklenen: data.sql icindeki "Ekran kirik" aciklamasi
 * JSON icinde gorunmeli.
 */
public class Senaryo1SeleniumTest extends BaseSeleniumTest {

    @Test
    @DisplayName("Tum servis kayitlari listelenebilmeli")
    void tum_servis_kayitlari_listelenebiliyor_mu() {
        // REST endpoint'ine git
        driver.get(baseUrl + "/api/servis-kayitlari");

        // Sayfa kaynagini (JSON metni) al
        String pageSource = driver.getPageSource();

        // Ornek veri: data.sql'den geliyor
        assertTrue(
                pageSource.contains("Ekran kirik"),
                "Servis kayitlari JSON'unda 'Ekran kirik' aciklamasi bulunamadi!"
        );
    }
}
