package core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverService;
import org.openqa.selenium.remote.CapabilityType;

import java.io.File;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.Duration;
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
    Proxy seleniumProxy;

    /**
     * Метод инициализации и настройки web-драйвера перед каждым тестом.
     * @author Кирилл Желтышев
     */
    @BeforeEach
    public void setUp() throws UnknownHostException {
        //System.setProperty("webdriver.chrome.driver", System.getenv("CHROME_DRIVER"));

        //proxy = new BrowserMobProxyServer();
        //proxy.start(8080);
        //seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        //String hostIp = Inet4Address.getLocalHost().getHostAddress();
        //seleniumProxy.setHttpProxy(hostIp + ":" + proxy.getPort());
        //seleniumProxy.setSslProxy(hostIp + ":" + proxy.getPort());
        //proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT, CaptureType.RESPONSE_BINARY_CONTENT);

        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().setScriptTimeout(Duration.ofSeconds(30));
    }

    /**
     * Метод закрытия web-драйвера после каждого теста.
     * @author Кирилл Желтышев
     */
    @AfterEach
    public void tearDown(){
        driver.quit();
        //proxy.stop();
    }
}
