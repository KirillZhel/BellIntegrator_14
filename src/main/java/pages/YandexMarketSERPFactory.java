package pages;

import core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import pages.elements.ManufacturerFilter;

import java.util.List;

public class YandexMarketSERPFactory extends BasePage {
    private String manufacturerFilterLocator = "//*[text()='Производитель']/ancestor::fieldset";
    private String testXPath = "//*[text()='Производитель']/ancestor::fieldset//span[text()]";

    public WebElement manufacturerFilter;
    public List<WebElement> manufacturers;

    public ManufacturerFilter manufacturerFilter2;

    public YandexMarketSERPFactory(WebDriver webDriver) {
        super(webDriver);
        this.manufacturerFilter2 = new ManufacturerFilter(webDriver);
    }
    
    public void getAllElements() {
        /*showAllButton = driver.findElement(By.xpath(showAllButtonLocator));
        showAllButton.click();
        manufacturerFilter = driver.findElement(By.xpath(manufacturerFilterLocator));
        //manufacturers = manufacturerFilter.findElements(By.xpath(testXPath));
        actions.moveToElement(manufacturerFilter);*/

       /*for (WebElement e :
                manufacturers) {
            System.out.println(e.getText());
       }*/


    }
    // кнопка "показать всё" в "производители":
    // "//*[text()='Производитель']/ancestor::fieldset//*[text()='Показать всё']/ancestor::button"
    // "//*[text()='Производитель']/ancestor::fieldset//span[text()]"
    // "//*[text()='Производитель']/ancestor::fieldset//span[text()]"
}
