package com.example.demo.selenuimtests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.time.Duration;

public class EnrollEventTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @BeforeMethod
    public void loginAsParticipant() {
        // Step 1: Navigate to a valid page to enable localStorage
        driver.get("http://localhost:3000/login");

        // Step 2: Set localStorage
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("localStorage.setItem('user', JSON.stringify({userType:'Participant', firstName:'TestUser', email:'test@example.com'}));");

        // Step 3: Navigate to enroll page
        driver.get("http://localhost:3000/enroll");
    }

    @Test
    public void testEnrollPageLoads() {
        // Verify page header
        WebElement heading = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h2")));
        assert heading.getText().contains("Enroll in an Event");
    }

    @Test
    public void testSearchFormVisible() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form")));
        WebElement searchBtn = driver.findElement(By.xpath("//button[text()='Search']"));
        assert searchBtn.isDisplayed();
    }

    @Test
    public void testNoEventsMessageIfEmpty() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("container")));
        boolean infoShown = driver.findElements(By.className("alert-info")).size() > 0;
        if (infoShown) {
            WebElement msg = driver.findElement(By.className("alert-info"));
            assert msg.getText().contains("No events found");
        }
    }

    @Test
    public void testBackToDashboardButton() {
        WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Back to Dashboard']")));
        backButton.click();
        wait.until(ExpectedConditions.urlContains("/home-participant"));
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
