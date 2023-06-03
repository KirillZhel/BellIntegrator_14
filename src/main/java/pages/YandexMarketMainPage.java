package pages;

import core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class YandexMarketMainPage extends BasePage {
    private String catalogButtonID = "catalogPopupButton";
    private String  categoryLocator;
    private String  subcategoryLocator;

    public WebElement catalogButton;
    public WebElement category;
    public WebElement subcategory;

    public YandexMarketMainPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void getAllElements() {
        catalogButton = driver.findElement(By.id(catalogButtonID));
        catalogButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(categoryLocator)));
        category = driver.findElement(By.xpath(categoryLocator));
        actions.moveToElement(category);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(subcategoryLocator)));
        subcategory = driver.findElement(By.xpath(subcategoryLocator));
    }

    public void createCategoryLocatorString(String category) {
        // переделать локатор под более приятный вид без явного указания оси ancestor
        categoryLocator = "//*[text()='" + category + "']/ancestor::li";
    }

    public void createSubcategoryLocatorString(String subcategory) {
        // переделать локатор под более приятный вид без явного указания оси ancestor
        subcategoryLocator = "//*[text()='" + subcategory + "']/ancestor::li";
    }
}
