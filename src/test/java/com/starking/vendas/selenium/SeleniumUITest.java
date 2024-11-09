package com.starking.vendas.selenium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.assertj.core.api.Assertions.assertThat;

public class SeleniumUITest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);

        driver.get("http://localhost:4200");
    }

    @Test
    public void testHomePage() {
        WebElement button = driver.findElement(By.id("buttonId"));
        button.click();

        WebElement result = driver.findElement(By.id("resultTextId"));
        assertThat(result.getText()).isEqualTo("Texto Esperado");
    }
}
