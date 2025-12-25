package com.example.teknikservis.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Senaryo9SeleniumTest extends BaseSeleniumTest {

    @Test
    @DisplayName("Senaryo 9: Servis kayitlarinda durum ACIK olmali")
    void servis_kayitlarinda_durum_acik_olmali() {
        driver.get(baseUrl + "/api/servis-kayitlari");

        String pageSource = driver.getPageSource();

        assertTrue(
                pageSource.contains("ACIK"),
                "Servis kayitlari JSON'unda 'ACIK' durum bilgisi bulunamadi!"
        );
    }
}
