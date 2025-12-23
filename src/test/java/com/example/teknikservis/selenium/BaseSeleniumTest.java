package com.example.teknikservis.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Tum Selenium senaryolari icin ortak alt yapi.
 * Jenkins uzerinde headless Chrome ile calismaya uygundur.
 */
public abstract class BaseSeleniumTest {

    protected WebDriver driver;

    /**
     * Testler calisirken gideceğimiz temel URL.
     * -DbaseUrl=http://host:port ile degistirilebilir.
     * Varsayilan: lokal makinede 8081 portu.
     */
    protected String baseUrl = System.getProperty("baseUrl", "http://localhost:8081");

    @BeforeAll
    static void setupDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void openBrowser() {
        ChromeOptions options = new ChromeOptions();
        // Jenkins / CI ortamı için headless çalıştırıyoruz
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
    }

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
