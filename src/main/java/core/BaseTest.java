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
    protected BrowserUpProxyServer proxy;

    /**
     * Метод инициализации и настройки web-драйвера перед каждым тестом.
     * @author Кирилл Желтышев
     */
    @BeforeEach
    public void setUp() {
        proxy = new BrowserUpProxyServer();
        proxy.start(0);
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

        WebDriverManager.chromedriver().setup();
        //driver = new ChromeDriver(getOptionsChrome());
        driver = new ChromeDriver(ChromeDriverService.createDefaultService(), getOptionsChrome());

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
        proxy.stop();
    }

    private ChromeOptions getOptionsChrome() {
        final Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        final String proxyAddress = resolveProxyAddress(proxy);

        seleniumProxy.setHttpProxy(proxyAddress);
        seleniumProxy.setSslProxy(proxyAddress);

        ChromeOptions options = new ChromeOptions();
        options.setProxy(seleniumProxy);
        options.setAcceptInsecureCerts(true);
        //options.addArguments("--remote-allow-origins=*");

        return options;
    }

    private String resolveProxyAddress(BrowserUpProxy proxy) {
        try {
            String hostIp = Inet4Address.getLocalHost().getHostAddress();
            return hostIp + ":" + proxy.getPort();
        } catch (UnknownHostException e) {
            throw new RuntimeException("Wasn't able to resolve proxy address", e);
        }

    }
}
