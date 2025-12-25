package com.example.teknikservis.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Senaryo4SeleniumTest extends BaseSeleniumTest {

    @Test
    @DisplayName("Senaryo 4: Servis kaydi JSON'unda temel alanlar olmali")
    void servis_kaydi_jsonunda_alanlar_olmali() {
        driver.get(baseUrl + "/api/servis-kayitlari");

        String pageSource = driver.getPageSource();

        // En azindan aciklama ve durum alan adlari bulunmali
        assertTrue(
                pageSource.contains("\"aciklama\""),
                "JSON icinde 'aciklama' alan adi bulunamadi!"
        );

        assertTrue(
                pageSource.contains("\"durum\""),
                "JSON icinde 'durum' alan adi bulunamadi!"
        );
    }
}
