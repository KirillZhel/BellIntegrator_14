package pages.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PriceFilter {
    private final String minPriceInputLocator = "//*[text()='Цена, ₽ от']//following-sibling::*//input";
    private final String maxPriceInputLocator = "//*[text()='Цена, ₽ до']//following-sibling::*//input";

    private final WebDriver driver;

    public PriceFilter(WebDriver driver) {
        this.driver = driver;
    }

    public void setMinPrice(int minPrice) {
        filterInput(minPriceInputLocator, minPrice);
    }

    public void setMaxPrice(int maxPrice) {
        filterInput(maxPriceInputLocator, maxPrice);
    }

    private void filterInput(String locator, int price) {
        WebElement input =  driver.findElement(By.xpath(locator));
        input.clear();
        input.sendKeys(Integer.toString(price));
    }
}
