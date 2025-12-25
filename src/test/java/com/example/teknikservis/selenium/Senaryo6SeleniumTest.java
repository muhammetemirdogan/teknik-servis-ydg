package com.example.teknikservis.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Senaryo6SeleniumTest extends BaseSeleniumTest {

    @Test
    @DisplayName("Senaryo 6: Servis kayitlari JSON listesi bos olmamali")
    void servis_kayitlari_listesi_bos_olmamali() {
        driver.get(baseUrl + "/api/servis-kayitlari");

        String pageSource = driver.getPageSource();

        // Beklenti: JSON bir liste ve icinde en az bir obje var
        boolean notEmptyList =
                pageSource.contains("[") &&
                        pageSource.contains("]") &&
                        pageSource.contains("{") &&
                        pageSource.contains("}");

        assertTrue(
                notEmptyList,
                "Servis kayitlari JSON listesi bos veya beklenen formatta degil!"
        );
    }
}
