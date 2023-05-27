package core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

/**
 * Базовый класс теста.
 * @author Кирилл Желтышев
 */
public class BaseTest {
    /**
     * Web-драйвер.
     * @author Кирилл Желтышев
     */
    protected WebDriver driver;

    /**
     * Метод инициализации и настройки web-драйвера перед каждым тестом.
     * @author Кирилл Желтышев
     */
    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", System.getenv("CHROME_DRIVER"));
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
    }

    /**
     * Метод закрытия web-драйвера после каждого теста.
     * @author Кирилл Желтышев
     */
    @AfterEach
    public void tearDown(){
        driver.quit();
    }
}
