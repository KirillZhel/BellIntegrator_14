package pages;

import core.BasePage;
import core.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.elements.ManufacturerFilter;
import pages.elements.PriceFilter;
import pages.elements.Snippet;

import java.time.Duration;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class YandexMarketSERPFactory extends BasePage {
    private final String productSnippetLocator = "//*[@data-autotest-id='product-snippet']";
    private final String spinnerLocator = "//*[@data-auto='spinner']";
    private final String goodSnippetItemLocator = "//*[@data-test-id='virtuoso-item-list']";
    private final String snippetTitleLocator = ".//*[@data-auto='snippet-title-header']";
    private final String snippetPriceLocator = ".//*[@data-auto='price-value']";

    public PriceFilter priceFilter;
    public ManufacturerFilter manufacturerFilter;

    public YandexMarketSERPFactory(WebDriver webDriver) {
        super(webDriver);
        priceFilter = new PriceFilter(webDriver);
        manufacturerFilter = new ManufacturerFilter(webDriver);
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
        //WaitUtils.waitForState(spinner, WebElement::isDisplayed);
        WaitUtils.waitForState(spinner, it -> !it.isDisplayed());
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
    }
}
