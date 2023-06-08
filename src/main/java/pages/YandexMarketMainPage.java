package pages;

import core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class YandexMarketMainPage extends BasePage {
    private String catalogButtonID = "catalogPopupButton";
    private String  categoryLocator;
    private String  subcategoryLocator;

    private WebElement catalogButton;
    private WebElement categoryLink;
    private WebElement subcategoryLink;

    public YandexMarketMainPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void clickToCatalogButton() {
        catalogButton = driver.findElement(By.id(catalogButtonID));
        catalogButton.click();
    }

    public void hoverToCategory(String category) {
        createCategoryLocatorString(category);
        categoryLink = findLink(categoryLocator);
        actions.moveToElement(categoryLink);
    }

    public void clickToSubcategory(String subcategory) {
        createSubcategoryLocatorString(subcategory);
        subcategoryLink = findLink(subcategoryLocator);
        subcategoryLink.click();
    }

    private  WebElement findLink(String locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
        return driver.findElement(By.xpath(locator));
    }

    private void createCategoryLocatorString(String category) {
        // переделать локатор под более приятный вид без явного указания оси ancestor
        categoryLocator = "//*[text()='" + category + "']/ancestor::li";
    }

    private void createSubcategoryLocatorString(String subcategory) {
        // переделать локатор под более приятный вид без явного указания оси ancestor
        subcategoryLocator = "//*[text()='" + subcategory + "']/ancestor::li";
    }
}
