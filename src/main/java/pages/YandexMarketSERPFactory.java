package pages;

import core.BasePage;
import org.openqa.selenium.WebDriver;
import pages.elements.ManufacturerFilter;
import pages.elements.PriceFilter;

public class YandexMarketSERPFactory extends BasePage {
    /*private String manufacturerFilterLocator = "//*[text()='Производитель']/ancestor::fieldset";
    private String testXPath = "//*[text()='Производитель']/ancestor::fieldset//span[text()]";*/

    /*public WebElement manufacturerFilter;
    public List<WebElement> manufacturers;*/


    public PriceFilter priceFilter;
    public ManufacturerFilter manufacturerFilter;

    public YandexMarketSERPFactory(WebDriver webDriver) {
        super(webDriver);
        priceFilter = new PriceFilter(webDriver);
        manufacturerFilter = new ManufacturerFilter(webDriver);
    }

    // методы для установки цен

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
