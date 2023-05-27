package pages;

import core.BasePage;
import org.openqa.selenium.WebDriver;

public class YandexMainPage extends BasePage {
    //переместить в проперти
    public String url = "https://ya.ru/";
    public String searchField = "//*[@id='text']";
    public String moreServicesButton = "//*[@class='services-suggest__icons-more']";
    public String popupMarketButton = "//*[text()='Маркет']/ancestor::span";

    public YandexMainPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void open() {
        driver.get(url);
    }
}
