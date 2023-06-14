package pages;

import core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.elements.ManufacturerFilter;
import pages.elements.PriceFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YandexMarketSERPFactory extends BasePage {
    /*private String manufacturerFilterLocator = "//*[text()='Производитель']/ancestor::fieldset";
    private String testXPath = "//*[text()='Производитель']/ancestor::fieldset//span[text()]";*/

    /*public WebElement manufacturerFilter;
    public List<WebElement> manufacturers;*/

    // "//*[@data-autotest-id="product-snippet"]" - карточка товара
    // "//*[@data-auto="snippet-title-header"]" - название товара
    // "//*[@data-auto="price-block"]//*[@data-auto="price-value"]/text()" - цена в виде текста
    // 1. новый класс/метод для проверки товаров на странице
    // 2. гетеры/сетеры для классов фильтра, что бы можно было достать и использовать их при проверке товаров

    private String productSnippetLocator = "//*[@data-autotest-id='product-snippet']";

    public PriceFilter priceFilter;
    public ManufacturerFilter manufacturerFilter;
    public Map<String, String> map;

    public YandexMarketSERPFactory(WebDriver webDriver) {
        super(webDriver);
        priceFilter = new PriceFilter(webDriver);
        manufacturerFilter = new ManufacturerFilter(webDriver);
        map = new HashMap<String, String>();
    }



    public Map<String, String> getAllSnippets()
    {
        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@data-test-id='virtuoso-item-list']")));
        //WebElement itemList = driver.findElement(By.xpath("//*[@data-test-id='virtuoso-item-list']"));
        //List<WebElement> snippets = driver.findElements(By.xpath(productSnippetLocator));
        List<WebElement> snippets = driver.findElements(By.xpath(productSnippetLocator));

        String name;
        String price;
        for (WebElement e: snippets) {
            name = e.findElement(By.xpath(".//*[@data-auto='snippet-title-header']")).getText();
            price = e.findElement(By.xpath(".//*[@data-auto='price-block']//*[@data-auto='price-value']")).getText();
            if (!map.containsKey(name)){
                map.put(name, price);
            }
        }

        return map;
    }

    public void scroolToFooter()
    {
        actions.scrollToElement(driver.findElement(By.xpath("//*[@data-auto='pager-more']"))).perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@data-auto='pager-more']")));
    }
}
