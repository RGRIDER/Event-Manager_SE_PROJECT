package com.example.demo.selenuimtests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.time.Duration;
import static org.testng.Assert.assertTrue;

public class SigninTest {
    private WebDriver driver;

    @BeforeClass
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    private void login(String email, String password, String userType) {
        driver.get("http://localhost:3000/login");
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("userType")).sendKeys(userType);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    private void handleAlertIfPresent() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            System.out.println("Alert text: " + alert.getText());
            alert.accept(); // Click OK
        } catch (TimeoutException e) {
            System.out.println("No alert found.");
        }
    }

    @Test
    public void testValidLoginParticipant() {
        login("haider@gmail.com", "Pass1234", "Participant");
        handleAlertIfPresent();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean loginSuccess = wait.until(driver -> driver.getCurrentUrl().contains("/home-participant"));
        assertTrue(loginSuccess, "Login with valid credentials failed.");
    }

    @Test
    public void testInvalidPassword() {
        login("haider@gmail.com", "WrongPass", "Participant");
        handleAlertIfPresent();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        boolean stillOnLoginPage = driver.getCurrentUrl().contains("/login");
        assertTrue(stillOnLoginPage, "Login passed with wrong password!");
    }

    @Test
    public void testInvalidEmailFormat() {
        login("haider.com", "Pass1234", "Participant");
        handleAlertIfPresent();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        boolean stillOnLoginPage = driver.getCurrentUrl().contains("/login");
        assertTrue(stillOnLoginPage, "Login passed with invalid email format!");
    }

    @Test
    public void testWrongUserTypeSelected() {
        login("haider@gmail.com", "Pass1234", "Admin"); // Wrong userType
        handleAlertIfPresent();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        boolean stillOnLoginPage = driver.getCurrentUrl().contains("/login");
        assertTrue(stillOnLoginPage, "Login passed even with wrong user type selected!");
    }

    @Test
    public void testUnregisteredEmail() {
        login("unknownuser@gmail.com", "Pass1234", "Participant");
        handleAlertIfPresent();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        boolean stillOnLoginPage = driver.getCurrentUrl().contains("/login");
        assertTrue(stillOnLoginPage, "Login passed with unregistered email!");
    }

    @Test
    public void testPasswordBoundaryLength7() {
        login("haider@gmail.com", "Pass123", "Participant"); // 7 characters
        handleAlertIfPresent();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        boolean stillOnLoginPage = driver.getCurrentUrl().contains("/login");
        assertTrue(stillOnLoginPage, "Login passed with short password!");
    }

    @Test
    public void testPasswordBoundaryLength8() {
        login("haider@gmail.com", "Pass1234", "Participant"); // 8 characters
        handleAlertIfPresent();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean loginSuccess = driver.getCurrentUrl().contains("/home-participant");
        assertTrue(loginSuccess, "Login failed with 8 character valid password!");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
