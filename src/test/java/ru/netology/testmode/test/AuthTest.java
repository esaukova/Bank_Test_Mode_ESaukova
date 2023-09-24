package ru.netology.testmode.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage"); //overcome limited resource problems
        options.addArguments("--no-sandbox");  //Bypass OS security model
        options.addArguments("--headless=new");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {  //после выполнения тестов
        driver.quit();
        driver = null;
    }


    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        // TODO: добавить логику теста, в рамках которого будет выполнена попытка входа в личный кабинет с учётными
        //  данными зарегистрированного активного пользователя, для заполнения полей формы используйте
        //  пользователя registeredUser
        driver.findElement(By.cssSelector("[data-test-id='login'] input")).sendKeys(registeredUser.getLogin());
        driver.findElement(By.cssSelector("[data-test-id='password'] input")).sendKeys(registeredUser.getPassword());
        driver.findElement(By.cssSelector("[data-test-id='action-login']")).click();
        String text = driver.findElement(By.cssSelector("[id='root'] h2")).getText();
        assertEquals("Личный кабинет", text.trim());

    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет
        //  незарегистрированного пользователя, для заполнения полей формы используйте пользователя notRegisteredUser
        driver.findElement(By.cssSelector("[data-test-id='login'] input")).sendKeys(notRegisteredUser.getLogin());
        driver.findElement(By.cssSelector("[data-test-id='password'] input")).sendKeys(notRegisteredUser.getPassword());
        driver.findElement(By.cssSelector("[data-test-id='action-login']")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("[data-test-id='error-notification']"))));

    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет,
        //  заблокированного пользователя, для заполнения полей формы используйте пользователя blockedUser
        driver.findElement(By.cssSelector("[data-test-id='login'] input")).sendKeys(blockedUser.getLogin());
        driver.findElement(By.cssSelector("[data-test-id='password'] input")).sendKeys(blockedUser.getPassword());
        driver.findElement(By.cssSelector("[data-test-id='action-login']")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("[data-test-id='error-notification']"))));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет с неверным
        //  логином, для заполнения поля формы "Логин" используйте переменную wrongLogin,
        //  "Пароль" - пользователя registeredUser
        driver.findElement(By.cssSelector("[data-test-id='login'] input")).sendKeys(wrongLogin);
        driver.findElement(By.cssSelector("[data-test-id='password'] input")).sendKeys(registeredUser.getPassword());
        driver.findElement(By.cssSelector("[data-test-id='action-login']")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("[data-test-id='error-notification']"))));

    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет с неверным
        //  паролем, для заполнения поля формы "Логин" используйте пользователя registeredUser,
        //  "Пароль" - переменную wrongPassword
        driver.findElement(By.cssSelector("[data-test-id='login'] input")).sendKeys(registeredUser.getLogin());
        driver.findElement(By.cssSelector("[data-test-id='password'] input")).sendKeys(wrongPassword);
        driver.findElement(By.cssSelector("[data-test-id='action-login']")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("[data-test-id='error-notification']"))));
    }
}