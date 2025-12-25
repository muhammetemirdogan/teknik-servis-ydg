package com.example.teknikservis.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public abstract class BaseSeleniumTest {

    protected static WebDriver driver;

    // !!! BUNU 8081 YAP !!!
    protected static final String baseUrl = "http://localhost:8081";

    @BeforeAll
    static void setupDriver() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
