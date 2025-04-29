package com.example.demo.selenuimtests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.time.Duration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class HomeOrganizerTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Navigate to valid domain first before setting localStorage
        driver.get("http://localhost:3000/login");
        ((JavascriptExecutor) driver).executeScript("localStorage.setItem('user', JSON.stringify({userType:'Organizer', firstName:'TestOrganizer'}));");
        driver.navigate().to("http://localhost:3000/home-organizer");
    }

    @Test
    public void testWelcomeMessage() {
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
        assertTrue(heading.getText().contains("Welcome, Organizer"));
    }

    @Test
    public void testCreateEventCardButtonClick() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//h5[text()='Create Event']/ancestor::div[contains(@class,'card')]//button")));
        button.click();
        wait.until(ExpectedConditions.urlContains("/create-event"));
        assertTrue(driver.getCurrentUrl().contains("/create-event"));
    }

    @Test
    public void testModifyEventCardButtonClick() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//h5[text()='Modify Event']/ancestor::div[contains(@class,'card')]//button")));
        button.click();
        wait.until(ExpectedConditions.urlContains("/modify-event"));
        assertTrue(driver.getCurrentUrl().contains("/modify-event"));
    }

    @Test
    public void testDeleteEventCardButtonClick() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//h5[text()='Delete Event']/ancestor::div[contains(@class,'card')]//button")));
        button.click();
        wait.until(ExpectedConditions.urlContains("/delete-event"));
        assertTrue(driver.getCurrentUrl().contains("/delete-event"));
    }

    @Test
    public void testFeedbackReportCardButtonClick() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//h5[text()='View Feedback Report']/ancestor::div[contains(@class,'card')]//button")));
        button.click();
        wait.until(ExpectedConditions.urlContains("/feedback-report"));
        assertTrue(driver.getCurrentUrl().contains("/feedback-report"));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null)
            driver.quit();
    }
}
