package com.example.demo.selenuimtests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.time.Duration;

import static org.testng.Assert.*;

public class HomeAdminTest {
    private WebDriver driver;

    @BeforeClass
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    private void loginAsAdmin() {
        // Step 1: Open a real page from your app domain
        driver.get("http://localhost:3000/login");

        // Step 2: Set localStorage (from a same-origin page!)
        ((JavascriptExecutor) driver).executeScript(
                "window.localStorage.setItem('user', JSON.stringify({userType:'Admin', firstName:'TestAdmin'}));"
        );

        // Step 3: Now go to the protected page
        driver.get("http://localhost:3000/home-admin");
    }


    @Test
    public void testAdminWelcomeHeader() throws InterruptedException {
        loginAsAdmin();
        Thread.sleep(1000);
        WebElement header = driver.findElement(By.tagName("h1"));
        assertTrue(header.getText().contains("Welcome, Admin TestAdmin!"));
    }

    @Test
    public void testFullReportCardExists() throws InterruptedException {
        loginAsAdmin();
        Thread.sleep(1000);
        WebElement card = driver.findElement(By.className("card-title"));
        assertEquals(card.getText(), "Full Report");
    }

    @Test
    public void testFullReportDescriptionExists() throws InterruptedException {
        loginAsAdmin();
        Thread.sleep(1000);
        WebElement cardText = driver.findElement(By.className("card-text"));
        assertTrue(cardText.getText().contains("detailed reports"));
    }

    @Test
    public void testFullReportCardButtonText() throws InterruptedException {
        loginAsAdmin();
        Thread.sleep(1000);
        WebElement button = driver.findElement(By.cssSelector(".card-body .btn-primary"));
        assertEquals(button.getText(), "View Full Report");
    }

    @Test
    public void testFullReportCardButtonClick() throws InterruptedException {
        loginAsAdmin();
        Thread.sleep(1000);
        WebElement button = driver.findElement(By.cssSelector(".card-body .btn-primary"));
        button.click();
        Thread.sleep(1000);
        assertTrue(driver.getCurrentUrl().contains("/admin-full-report"));
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
