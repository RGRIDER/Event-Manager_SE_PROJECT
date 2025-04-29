package com.example.demo.selenuimtests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.time.Duration;

import static org.testng.Assert.*;

public class AddAnnouncementTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Navigate to a valid page first to avoid localStorage set error
        driver.get("http://localhost:3000/login");

        // Inject organizer user into localStorage
        ((JavascriptExecutor) driver).executeScript(
                "window.localStorage.setItem('user', JSON.stringify({" +
                        "'email': 'organizer@example.com'," +
                        "'userType': 'Organizer'," +
                        "'firstName': 'John'}));");

        // Navigate to the AddAnnouncement page
        driver.navigate().to("http://localhost:3000/add-announcement");
    }

    @Test
    public void testHeaderPresent() {
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));
        assertEquals(heading.getText(), "Add Announcement");
    }

    @Test
    public void testDropdownAndTextareaPresence() {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("select")));
        WebElement textarea = driver.findElement(By.tagName("textarea"));
        assertTrue(dropdown.isDisplayed());
        assertTrue(textarea.isDisplayed());
    }



    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
