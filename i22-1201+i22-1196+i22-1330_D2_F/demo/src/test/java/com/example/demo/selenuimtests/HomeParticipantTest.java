package com.example.demo.selenuimtests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class HomeParticipantTest {
    private WebDriver driver;

    @BeforeClass
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Step 1: Load a valid page to establish origin for localStorage
        driver.get("http://localhost:3000/login");

        // Step 2: Inject localStorage mock user
        String script = """
            localStorage.setItem('user', JSON.stringify({
                userType: 'Participant',
                firstName: 'TestParticipant'
            }));
        """;
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(script);

        // Step 3: Navigate to HomeParticipant page
        driver.get("http://localhost:3000/home-participant");
    }

    @Test
    public void testWelcomeMessage() {
        WebElement welcome = driver.findElement(By.tagName("h1"));
        assertTrue(welcome.getText().contains("Welcome, Participant TestParticipant!"));
    }

    @Test
    public void testEnrollButtonExists() {
        WebElement enrollButton = driver.findElement(By.xpath("//button[text()='Enroll Now']"));
        assertTrue(enrollButton.isDisplayed());
    }

    @Test
    public void testUnenrollButtonExists() {
        WebElement unenrollButton = driver.findElement(By.xpath("//button[text()='Unenroll']"));
        assertTrue(unenrollButton.isDisplayed());
    }

    @Test
    public void testViewEventsButtonExists() {
        WebElement viewButton = driver.findElement(By.xpath("//button[text()='View Events']"));
        assertTrue(viewButton.isDisplayed());
    }

    @Test
    public void testFeedbackButtonExists() {
        WebElement feedbackButton = driver.findElement(By.xpath("//button[text()='Send Feedback']"));
        assertTrue(feedbackButton.isDisplayed());
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
