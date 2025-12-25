package com.example.teknikservis.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Senaryo5SeleniumTest extends BaseSeleniumTest {

    @Test
    @DisplayName("Senaryo 5: Servis kaydi JSON'unda cihaz ve id olmali")
    void servis_kaydi_jsonunda_cihaz_ve_id_olmali() {
        driver.get(baseUrl + "/api/servis-kayitlari");

        String pageSource = driver.getPageSource();

        assertTrue(
                pageSource.contains("\"cihaz\""),
                "JSON icinde 'cihaz' alan adi bulunamadi!"
        );

        assertTrue(
                pageSource.contains("\"id\""),
                "JSON icinde 'id' alan adi bulunamadi!"
        );
    }
}
