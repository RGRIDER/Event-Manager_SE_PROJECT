package com.example.demo.selenuimtests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.time.Duration;

public class NotificationsPageTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:3000/login");  // must be valid path

        // Set localStorage for participant
        ((JavascriptExecutor) driver).executeScript(
                "localStorage.setItem('user', JSON.stringify({" +
                        "email: 'participant@example.com'," +
                        "userType: 'Participant'" +
                        "}));"
        );

        driver.navigate().to("http://localhost:3000/notifications");
    }

    @Test
    public void testNotificationsPageLoads() {
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));
        assert heading.getText().equals("Your Notifications");
    }

    @Test
    public void testAnnouncementsDisplay() {
        WebElement container = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("container")));
        String bodyText = container.getText();

        boolean hasAnnouncement = bodyText.contains("No announcements found.")
                || bodyText.contains("Loading announcements...")
                || container.findElements(By.className("list-group-item")).size() > 0;

        assert hasAnnouncement;
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
