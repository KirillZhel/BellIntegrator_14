package pages.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class ManufacturerFilter {
    private final String manufacturerFilterLocator = "//*[@data-baobab-name='filter'][.//legend[.='Производитель']]";
    private final String showAllButtonLocator = ".//*[@data-zone-name='LoadFilterValues']";
    private final String filterInputLocator = ".//input[./preceding-sibling::label[text()='Найти производителя']]";

    private final WebDriver driver;
    private final Actions actions;

    public ManufacturerFilter(WebDriver driver) {
        this.driver = driver;
        actions = new Actions(driver);
    }

    public void clickShowAllButton() {
        WebElement button = driver
                .findElement(By.xpath(manufacturerFilterLocator))
                .findElement(By.xpath(showAllButtonLocator));

        button.click();
    }
    public void chooseManufacturer(String name) {
        clearFilterInput();
        typeTextToInput(name);
        clickOption(name);
    }
    public void typeTextToInput(String text) {
        filterInput().sendKeys(text);
    }

    public void clickOption(String optionName) {
        WebElement option = filterContainer().findElement(By.xpath(filterOptionLabel(optionName)));
        option.click();
    }

    public void clearFilterInput() {
        filterInput().clear();
    }

    private WebElement filterInput() {
        return filterContainer()
                .findElement(By.xpath(filterInputLocator));
    }

    private String filterOptionLabel(String optionName) {
        return String.format(".//*[@data-baobab-name='filterValue'][(.)='%s']//label", optionName);
    }

    private WebElement filterContainer() {
        return driver.findElement(By.xpath(manufacturerFilterLocator));
    }

}