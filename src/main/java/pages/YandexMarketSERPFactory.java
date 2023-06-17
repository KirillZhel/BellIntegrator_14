package pages;

import core.BasePage;
import core.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.elements.Checker;
import pages.elements.ManufacturerFilter;
import pages.elements.PriceFilter;
import pages.elements.Snippet;

import java.time.Duration;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static core.WaitUtils.waitFor;

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
    private String spinnerLocator = "//*[@data-auto='spinner']";

    public PriceFilter priceFilter;
    public ManufacturerFilter manufacturerFilter;
    public Map<String, String> map;
    public List<Snippet> snippets;

    public Checker checker;
    private final String goodSnippetItemLocator = "//*[@data-test-id='virtuoso-item-list']";
    private final String snippetTitleLocator = ".//*[@data-auto='snippet-title-header']";
    private final String snippetPriceLocator = ".//*[@data-auto='price-value']";

    public YandexMarketSERPFactory(WebDriver webDriver) {
        super(webDriver);
        priceFilter = new PriceFilter(webDriver);
        manufacturerFilter = new ManufacturerFilter(webDriver);

        map = new HashMap<String, String>();
        snippets = new ArrayList<>();
        checker = new Checker();
    }

    public Map<String, String> getAllSnippetsMap()
    {

        Map<String, String> itemNameToPrice = new LinkedHashMap<>();

        waitFor(this.areSnippetsVisible());
        List<WebElement> webSnippets = driver.findElements(By.xpath(productSnippetLocator));

        String name;
        String price;
        for (WebElement e: webSnippets) {
            name = e.findElement(By.xpath(".//*[@data-auto='snippet-title-header']")).getText();
            price = e.findElement(By.xpath(".//*[@data-auto='price-block']//*[@data-auto='price-value']")).getText();
            itemNameToPrice.putIfAbsent(name, price);
        }

        return itemNameToPrice;
    }

    public int getSnippetsCount() {
        return driver.findElements(By.xpath(goodSnippetItemLocator)).size();
    }

    public boolean areSnippetsVisible() {
        return this.getSnippetsCount() > 0;
    }

    public List<Snippet> getShownSnippetsList()
    {
        List<WebElement> elements = driver.findElements(By.xpath(productSnippetLocator));
        return elements.stream().map(e -> {
            String name = e.findElement(By.xpath(snippetTitleLocator)).getText();
            String rawPrice = e.findElement(By.xpath(snippetPriceLocator)).getText().replaceAll("\\D*", "");
            int price = Integer.parseInt(rawPrice);
            return new Snippet(name, price);
        })
                .collect(Collectors.toList());
    }

    public void waitForDataLoaded() {
        Supplier<WebElement> spinner = () -> driver.findElement(By.xpath(spinnerLocator));
        WaitUtils.waitForState(spinner, WebElement::isDisplayed);
        WaitUtils.waitForState(spinner, it -> !it.isDisplayed());
    }

    // //*[@data-autotest-id="product-snippet"]
    // //*[@data-item-index="1"]
    // //*[@data-auto="snippet-title-header"]
    public String getProductNameByIndex(int index)
    {
        String locator = "//*[@data-item-index='" + index + "']";
        String nameLocator = "//*[@data-auto='snippet-title-header']";
        WebElement element = driver.findElement(By.xpath(locator));
        String name = element.findElement(By.xpath(nameLocator)).getText();
        return name;
    }

    public void scroolToFooter()
    {
        actions.scrollToElement(driver.findElement(By.tagName("footer"))).perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//footer]")));
        actions.scrollByAmount(0,2000);
    }

    public void scrollAllSnippets() {
        List<WebElement> previousSnippetsList = this.driver.findElements(By.xpath(productSnippetLocator));

        while(true) {
            WebElement lastSnippet = previousSnippetsList.get(previousSnippetsList.size() - 1);
            actions
                    .scrollToElement(lastSnippet)
                    .scrollByAmount(0, lastSnippet.getRect().height / 2)
                    .perform();
            WaitUtils.wait(Duration.ofMillis(500));
            List<WebElement> currentSnippetsList = this.driver.findElements(By.xpath(productSnippetLocator));
            if (previousSnippetsList.size() == currentSnippetsList.size()) return;
            previousSnippetsList = currentSnippetsList;
        }
        /*actions.scrollToElement(driver.findElement(By.xpath("//*[@data-auto='pager-more']"))).perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@data-auto='pager-more']")));
        actions.scrollByAmount(0,2000);*/
    }
}
