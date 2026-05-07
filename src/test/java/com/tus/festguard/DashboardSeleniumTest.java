package com.tus.festguard;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class DashboardSeleniumTest {

    private static final String BASE_URL = "http://localhost:8080";

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    void submitCrowdReport_appearsInRecentReportsTable() {
        driver.get(BASE_URL + "/index.html");

        // Click the Crowd Reports nav link via JS to avoid visibility/clickability issues
        WebElement reportsNav = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[@data-section='reports']")));
        js.executeScript("arguments[0].click();", reportsNav);

        // Wait for area dropdown to be populated with real areas from DB
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.cssSelector("#reportArea option"), 1));

        // Fill in the form
        new Select(driver.findElement(By.id("reportArea"))).selectByIndex(1);
        new Select(driver.findElement(By.id("crowdLevel"))).selectByValue("MEDIUM");
        driver.findElement(By.id("reportNote")).sendKeys("Selenium test report");

        // Submit via JS click
        js.executeScript("arguments[0].click();", driver.findElement(By.id("reportSubmitBtn")));

        // Wait for the recent reports table to show the new entry
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("reportsTable")));

        String tableText = driver.findElement(By.id("reportsBody")).getText();
        assertThat(tableText).contains("Selenium test report");
        assertThat(tableText).contains("MEDIUM");
    }
}
