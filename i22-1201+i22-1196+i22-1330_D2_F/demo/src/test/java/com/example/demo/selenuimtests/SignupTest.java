package com.example.demo.selenuimtests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.assertTrue;

public class SignupTest {
    private WebDriver driver;

    @BeforeClass
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    private void fillSignupForm(String firstName, String lastName, String email, String password, String userType) {
        driver.get("http://localhost:3000/signup");

        driver.findElement(By.name("firstName")).clear();
        driver.findElement(By.name("firstName")).sendKeys(firstName);

        driver.findElement(By.name("lastName")).clear();
        driver.findElement(By.name("lastName")).sendKeys(lastName);

        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys(email);

        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys(password);

        WebElement userTypeDropdown = driver.findElement(By.name("userType"));
        Select userTypeSelect = new Select(userTypeDropdown);
        userTypeSelect.selectByVisibleText(userType);

        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    private String captureAlertText() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.alertIsPresent());
        String alertText = driver.switchTo().alert().getText();
        driver.switchTo().alert().accept();
        return alertText;
    }

    // --- VALID Tests (Expected Success) ---
    @Test
    public void testValidSignup() {
        fillSignupForm("Test", "User", "validuser" + System.currentTimeMillis() + "@example.com", "Password123", "Participant");
        String alertText = captureAlertText();
        assertTrue(alertText.toLowerCase().contains("successful"));
    }

    @Test
    public void testBoundaryPasswordLength8() {
        fillSignupForm("John", "Doe", "boundary8" + System.currentTimeMillis() + "@example.com", "Passwrd1", "Participant");
        String alertText = captureAlertText();
        assertTrue(alertText.toLowerCase().contains("successful"));
    }

    @Test
    public void testBoundaryPasswordLength15() {
        fillSignupForm("Alice", "Smith", "boundary15" + System.currentTimeMillis() + "@example.com", "Password1234567", "Participant");
        String alertText = captureAlertText();
        assertTrue(alertText.toLowerCase().contains("successful"));
    }

    // --- INVALID Tests (Expected Failure) ---
    @Test
    public void testInvalidFirstNameWithNumber() {
        fillSignupForm("Test1", "User", "invalidfn" + System.currentTimeMillis() + "@example.com", "Password123", "Participant");
        String alertText = captureAlertText();
        assertTrue(alertText.toLowerCase().contains("first name"));
    }

    @Test
    public void testInvalidLastNameWithSpecialCharacter() {
        fillSignupForm("Test", "User@", "invalidln" + System.currentTimeMillis() + "@example.com", "Password123", "Participant");
        String alertText = captureAlertText();
        assertTrue(alertText.toLowerCase().contains("last name"));
    }

    @Test
    public void testInvalidEmailWithoutAlphabet() {
        fillSignupForm("Test", "User", "invalidemail", "Password123", "Participant");

        // Instead of waiting for alert, wait for some indication that form is NOT submitted
        boolean stillOnSignupPage = driver.getCurrentUrl().contains("/signup"); // URL didn't change

        assertTrue(stillOnSignupPage, "Form was submitted even with invalid email!");
    }


    @Test
    public void testPasswordTooShort() {
        fillSignupForm("Test", "User", "shortpass" + System.currentTimeMillis() + "@example.com", "Pwd1", "Participant");
        String alertText = captureAlertText();
        assertTrue(alertText.toLowerCase().contains("password must be between"));
    }

    @Test
    public void testPasswordTooLong() {
        fillSignupForm("Test", "User", "longpass" + System.currentTimeMillis() + "@example.com", "Password123456789", "Participant");
        String alertText = captureAlertText();
        assertTrue(alertText.toLowerCase().contains("password must be between"));
    }

    @Test
    public void testPasswordWithoutUppercase() {
        fillSignupForm("Test", "User", "noupper" + System.currentTimeMillis() + "@example.com", "password123", "Participant");
        String alertText = captureAlertText();
        assertTrue(alertText.toLowerCase().contains("uppercase"));
    }

    @Test
    public void testPasswordWithoutNumber() {
        fillSignupForm("Test", "User", "nonumber" + System.currentTimeMillis() + "@example.com", "Password", "Participant");
        String alertText = captureAlertText();
        assertTrue(alertText.toLowerCase().contains("number"));
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
