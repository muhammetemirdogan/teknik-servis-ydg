package com.example.teknikservis.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Senaryo8SeleniumTest extends BaseSeleniumTest {

    @Test
    @DisplayName("Senaryo 8: Servis kayitlarinda seri numarasi gorunmeli")
    void servis_kayitlarinda_seri_numarasi_gorunmeli() {
        driver.get(baseUrl + "/api/servis-kayitlari");

        String pageSource = driver.getPageSource();

        assertTrue(
                pageSource.contains("SN123"),
                "Servis kayitlari JSON'unda 'SN123' seri numarasi bulunamadi!"
        );
    }
}
