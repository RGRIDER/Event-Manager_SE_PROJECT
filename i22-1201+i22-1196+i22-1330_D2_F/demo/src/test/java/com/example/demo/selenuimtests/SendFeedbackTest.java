package com.example.demo.selenuimtests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.time.Duration;

public class SendFeedbackTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:3000/login");  // Must be valid domain before localStorage

        // Set user in localStorage
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "localStorage.setItem('user', JSON.stringify({" +
                        "email: 'hz@example.com'," +
                        "userType: 'Participant'" +
                        "}));"
        );

        driver.navigate().to("http://localhost:3000/send-feedback");
    }

    @Test
    public void testPageLoadsCorrectly() {
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));
        assert heading.getText().contains("Send Feedback");
    }

    @Test
    public void testFeedbackFormInteraction() {
        // Wait and click the first 'Give Feedback' button
        WebElement feedbackButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Give Feedback']"))
        );
        feedbackButton.click();

        // Click the third star for rating
        WebElement thirdStar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//span[text()='★'])[3]"))
        );
        thirdStar.click();

        // Enter feedback text
        WebElement textarea = wait.until(ExpectedConditions.elementToBeClickable(By.tagName("textarea")));
        textarea.sendKeys("This was a great event!");

        // Submit feedback
        WebElement submitButton = driver.findElement(By.xpath("//button[text()='Submit']"));
        submitButton.click();

        // Accept confirmation alert if present
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (TimeoutException ignored) {}
    }

    @Test
    public void testCancelFeedback() {
        // Open feedback modal again
        WebElement feedbackButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Give Feedback']"))
        );
        feedbackButton.click();

        // Click Cancel button
        WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Cancel']"))
        );
        cancelButton.click();

        // Ensure feedback form is hidden
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[text()='Give Feedback']"))
        );
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
