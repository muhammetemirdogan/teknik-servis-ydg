package com.example.teknikservis.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public abstract class BaseSeleniumTest {

    protected static WebDriver driver;
    protected static String baseUrl;

    @BeforeAll
    static void setupDriver() {
        // baseUrl: önce -DbaseUrl, yoksa env BASE_URL, yoksa default
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = System.getProperty("baseUrl");
            if (baseUrl == null || baseUrl.isBlank()) {
                baseUrl = System.getenv("BASE_URL");
            }
            if (baseUrl == null || baseUrl.isBlank()) {
                baseUrl = "http://localhost:8081";
            }
        }

        // headless: önce -Dheadless, yoksa env SELENIUM_HEADLESS, yoksa true
        String headlessStr = System.getProperty("headless");
        if (headlessStr == null || headlessStr.isBlank()) {
            headlessStr = System.getenv("SELENIUM_HEADLESS");
        }
        boolean headless = (headlessStr == null || headlessStr.isBlank())
                ? true
                : Boolean.parseBoolean(headlessStr);

        // Driver'ı sadece 1 kere oluştur (tüm senaryolar aynı JVM'de koşar)
        if (driver == null) {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();

            // Jenkins/CI için stabil argümanlar
            if (headless) options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--remote-allow-origins=*");

            driver = new ChromeDriver(options);

            // JVM kapanırken temiz kapat
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    if (driver != null) driver.quit();
                } catch (Exception ignored) {}
            }));
        }
    }
}
