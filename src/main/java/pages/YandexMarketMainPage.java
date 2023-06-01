package pages;

import core.BasePage;
import org.openqa.selenium.WebDriver;

public class YandexMarketMainPage extends BasePage {
    private String catalogButtonID = "catalogPopupButton";
    private String  categoryLocator;
    private String  subcategoryLocator;

    public YandexMarketMainPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void createCategoryLocatorString(String category) {
        // переделать локатор под более приятный вид без явного указания оси ancestor
        categoryLocator = "//*[text()='" + category + "']/ancestor::li";
    }

    public void createCategoryLocatorString() {
        // переделать локатор под более приятный вид без явного указания оси ancestor
        categoryLocator = "//*[text()='Ноутбуки и компьютеры']/ancestor::li";
    }

    public void subcreateCategoryLocatorString(String subcategory) {
        // переделать локатор под более приятный вид без явного указания оси ancestor
        categoryLocator = "//*[text()='" + subcategory + "']/ancestor::li";
    }

    public void subcreateCategoryLocatorString() {
        // переделать локатор под более приятный вид без явного указания оси ancestor
        categoryLocator = "//*[text()='Ноутбуки']/ancestor::li";
    }


}
