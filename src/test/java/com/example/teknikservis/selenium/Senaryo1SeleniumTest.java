package com.example.teknikservis.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Senaryo 1:
 *   /api/servis-kayitlarii endpoint'i calisiyor mu ve
 *   ornek servis kayitlarini donuyor mu kontrol eder.
 *
 * Beklenen: data.sql icindeki "Ekran kirik" aciklamasi
 * JSON icinde gorunmeli.
 */
public class Senaryo1SeleniumTest extends BaseSeleniumTest {

    @Test
    @DisplayName("Servis kayitlari JSON listesi 'Ekran kirik' aciklamasini icermeli")
    void tum_servis_kayitlari_listelenebiliyor_mu() {
        driver.get(baseUrl + "/api/servis-kayitlari");

        String pageSource = driver.getPageSource();
        System.out.println("=== PAGE SOURCE START ===");
        System.out.println(pageSource);
        System.out.println("=== PAGE SOURCE END ===");

        assertTrue(pageSource.contains("Ekran kirik"),
                "Servis kayitlari JSON'unda 'Ekran kirik' aciklamasi bulunamadi!");
    }

}
