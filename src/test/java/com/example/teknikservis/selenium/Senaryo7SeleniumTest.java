package com.example.teknikservis.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Senaryo7SeleniumTest extends BaseSeleniumTest {

    @Test
    @DisplayName("Senaryo 7: Servis kayitlarinda Samsung marka gorunmeli")
    void servis_kayitlarinda_samsung_marka_gorunmeli() {
        driver.get(baseUrl + "/api/servis-kayitlari");

        String pageSource = driver.getPageSource();

        assertTrue(
                pageSource.contains("Samsung"),
                "Servis kayitlari JSON'unda 'Samsung' marka bilgisi bulunamadi!"
        );
    }
}
