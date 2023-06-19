package core;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.bup.BrowserUpProxyServer;
import com.browserup.bup.client.ClientUtil;
import com.browserup.bup.proxy.CaptureType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.Duration;

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
     * прокси.
     * @author Кирилл Желтышев
     */
    protected BrowserUpProxyServer proxy;

    /**
     * Метод инициализации и настройки web-драйвера и прокси перед каждым тестом.
     * @author Кирилл Желтышев
     */
    @BeforeEach
    public void setUp() {
        proxy = new BrowserUpProxyServer();
        proxy.start(0);
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(ChromeDriverService.createDefaultService(), getOptionsChrome());

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().setScriptTimeout(Duration.ofSeconds(30));
    }

    /**
     * Метод закрытия web-драйвера и прокси после каждого теста.
     * @author Кирилл Желтышев
     */
    @AfterEach
    public void tearDown(){
        driver.quit();
        proxy.stop();
    }

    /**
     * Метод настроек web-драйвера после каждого теста.
     * @author Кирилл Желтышев
     */
    private ChromeOptions getOptionsChrome() {
        final Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        final String proxyAddress = resolveProxyAddress(proxy);

        seleniumProxy.setHttpProxy(proxyAddress);
        seleniumProxy.setSslProxy(proxyAddress);

        ChromeOptions options = new ChromeOptions();
        options.setProxy(seleniumProxy);
        options.setAcceptInsecureCerts(true);

        return options;
    }

    /**
     * Метод возвращает адресс прокси
     * @param proxy прокси
     * @return адресс прокси в виде строки
     */
    private String resolveProxyAddress(BrowserUpProxy proxy) {
        try {
            String hostIp = Inet4Address.getLocalHost().getHostAddress();
            return hostIp + ":" + proxy.getPort();
        } catch (UnknownHostException e) {
            throw new RuntimeException("Не удалось создать адресс прокси:", e);
        }
    }
}
