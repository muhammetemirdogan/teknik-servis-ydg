package com.example.teknikservis.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Senaryo 3:
 *   H2 veritabanı konsoluna ulasilabiliyor mu kontrol eder.
 *
 * Beklenen: /h2-console sayfasinda "H2 Console" veya
 * "Login" gibi bir metin bulunmali.
 */
public class Senaryo3SeleniumTest extends BaseSeleniumTest {

    @Test
    @DisplayName("H2 veritabani konsoluna erisilebilmeli")
    void h2_console_aciliyor_mu() {
        driver.get(baseUrl + "/h2-console");

        String pageSource = driver.getPageSource();

        assertTrue(
                pageSource.contains("H2 Console") || pageSource.contains("Login"),
                "H2 konsol sayfasina ulasilamadi!"
        );
    }
}
