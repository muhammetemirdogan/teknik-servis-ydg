package com.example.teknikservis.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Senaryo 10:
 *   H2 konsol sayfasinda baglanti ayarlari goruntulenebiliyor mu
 *   (ornegin 'JDBC URL' yazisi).
 */
public class Senaryo10SeleniumTest extends BaseSeleniumTest {

    @Test
    @DisplayName("H2 konsolunda JDBC URL bilgisi gorunmeli")
    void h2_console_jdbc_url_gorunmeli() {
        driver.get(baseUrl + "/h2-console");

        String pageSource = driver.getPageSource();

        assertTrue(
                pageSource.contains("JDBC URL") || pageSource.contains("JDBC Url"),
                "H2 konsolunda 'JDBC URL' yazisi gorunmuyor!"
        );
    }
}
