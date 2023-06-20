package pages;

import core.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Класс главной страницы Яндекс Маркета.
 * @author Кирилл Желтышев
 */
public class YandexMarketMainPage extends BasePage {
    /**
     * ID кнопки "Каталог".
     * @author Кирилл Желтышев
     */
    private final String catalogButtonId = "catalogPopupButton";
    /**
     * Локатор категории.
     * @author Кирилл Желтышев
     */
    private String  categoryLocator;
    /**
     * Локатор подкатегории.
     * @author Кирилл Желтышев
     */
    private String  subcategoryLocator;

    /**
     * Конструктор класса.
     * @author Кирилл Желтышев
     * @param webDriver Web-драйвер
     */
    public YandexMarketMainPage(WebDriver webDriver) {
        super(webDriver);
    }

    /**
     * Метод открытия меню каталога.
     * @author Кирилл Желтышев
     */
    @Step("Открываем каталог по нажатию на кнопку \"Каталог\".")
    public void openCatalog() {
        WebElement catalogButton = driver.findElement(By.id(catalogButtonId));
        catalogButton.click();
    }

    /**
     * Метод перехода к категории.
     * @author Кирилл Желтышев
     * @param category Категория, к которой надо перейти
     */
    @Step("Перемещаемся на категорию: \"{category}\".")
    public void hoverToCategory(String category) {
        createCategoryLocatorString(category);
        WebElement categoryLink = findElement(categoryLocator);
        actions.moveToElement(categoryLink);
    }

    /**
     * Метод открытия подкатегории.
     * @author Кирилл Желтышев
     * @param subcategory Подкатегория, в которую нужно перейти.
     */
    @Step("Переходим на подкатегорию: \"{subcategory}\".")
    public void openSubcategory(String subcategory) {
        createSubcategoryLocatorString(subcategory);
        WebElement subcategoryLink = findElement(subcategoryLocator);
        subcategoryLink.click();
    }

    /**
     * Метод поиска элемента.
     * @author Кирилл Желтышев
     * @param locator Локатор искомого элемента
     * @return Искомый элемент
     */
    @Step("Ищем элемент на странице по локатору: {locator}.")
    private  WebElement findElement(String locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
        return driver.findElement(By.xpath(locator));
    }

    /**
     * Метод составления локатора категории.
     * @author Кирилл Желтышев
     * @param category Категория, под которыю требуется составить локатор
     */
    @Step("Составляем локатор для поиска \"{category}\"")
    private void createCategoryLocatorString(String category) {
        categoryLocator = "//*[text()='" + category + "']/ancestor::li";
    }

    /**
     * Метод составления локатора подкатегории.
     * @author Кирилл Желтышев
     * @param subcategory Подкатегория, под которыю требуется составить локатор
     */
    @Step("Составляем локатор для поиска \"{subcategory}\"")
    private void createSubcategoryLocatorString(String subcategory) {
        subcategoryLocator = "//*[text()='" + subcategory + "']/ancestor::li";
    }
}
