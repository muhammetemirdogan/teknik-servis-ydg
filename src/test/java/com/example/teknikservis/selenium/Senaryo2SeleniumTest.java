package com.example.teknikservis.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Senaryo 2:
 *   /api/servis-kayitlari/musteri/{id} endpoint'i ile
 *   belirli bir musterinin kayitlari filtrelenebiliyor mu kontrol eder.
 *
 * Beklenen: id=1 olan musterinin adi "Ali Musteri" (data.sql).
 */
public class Senaryo2SeleniumTest extends BaseSeleniumTest {

    @Test
    @DisplayName("Belirli musterinin servis kayitlari listelenebilmeli")
    void musteriye_gore_filtreleme_calismali() {
        // id=1 olan musteri icin
        driver.get(baseUrl + "/api/servis-kayitlari/musteri/1");

        String pageSource = driver.getPageSource();

        assertTrue(
                pageSource.contains("Ali Musteri"),
                "Musteri bazli kayitlarda 'Ali Musteri' bilgisi bulunamadi!"
        );
    }
}
