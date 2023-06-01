package pages;

import core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class YandexMainPage extends BasePage {
    //переместить в проперти
    private String url = "https://ya.ru/";
    private String searchFieldLocator = "//*[@id='text']";
    private String moreServicesButtonLocator = "//*[@class='services-suggest__icons-more']";
    private String popupMarketButtonLocator = "//*[text()='Маркет']/ancestor::span";

    public WebElement searchField;
    public WebElement moreServicesButton;
    public WebElement popupMarketButton;


    public YandexMainPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void open() {
        driver.get(url);
        getAllElements();
    }

    private void getAllElements() {
        searchField = driver.findElement(By.xpath(searchFieldLocator));
        searchField.click();
        moreServicesButton = driver.findElement(By.xpath(moreServicesButtonLocator));
        moreServicesButton.click();
        popupMarketButton = driver.findElement(By.xpath(popupMarketButtonLocator));
    }
}
