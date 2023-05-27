package core;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;

public class BasePage {
    protected WebDriver driver;

    public BasePage(WebDriver webDriver) {
        driver = webDriver;
    }

    protected Boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
           return false;
        }
    }

    //какой метод можно выкинуть и оставить: waitForElement или waitForElementLocated (?)
    protected void waitForElement(WebElement element) {
        new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForElementLocated(By by) {
        new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    //метод перемещения к элементу
    public void goToElement(WebElement webElement) {
        new Actions(driver).moveToElement(webElement);
        // new Actions(driver).moveToElement(allCurrenciesButton).click().build().perform();
    }
}
