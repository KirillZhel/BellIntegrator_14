package ru.yandex.market;

import core.*;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import models.SearchResultsInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.YandexMainPage;
import pages.YandexMarketMainPage;
import pages.YandexMarketCategoryPage;
import models.Snippet;

import java.util.*;

import static steps.StepsAll.*;

/**
 * Класс, содержащий тесты Яндекс Маркета.
 * @author Кирилл Желтышев
 */
public class YandexMarketTests extends BaseTest {
    /**
     * Метод, начинающий запись нового файла HAR.
     * @author Кирилл Желтышев
     */
    @BeforeEach
    public void before() {
        proxy.newHar();
    }

    /**
     * Метод теста поиска товаров (задание 1.4).
     * @author Кирилл Желтышев
     * @param category Категория товара
     * @param subcategory Подкатегория товара
     * @param manufacturersForFilter Список производителей для фильтра
     * @param minPriceForFilter Минимальная цена для фильтра
     * @param maxPriceForFilter Максимальная цена для фильтра
     * @param snippetsNumber Граничное количество карточек товаров
     */
    @Epic("Яндекс Маркет")
    @Feature(value = "Выдача товаров Яндекс Маркета")
    @DisplayName(value = "Проверка выдачи товаров")
    @ParameterizedTest
    @MethodSource("helpers.DataProvider#provideCheckingMarket")
    public void searchingProductsTest(
            String category,
            String subcategory,
            List<String> manufacturersForFilter,
            int minPriceForFilter,
            int maxPriceForFilter,
            int snippetsNumber) {
        YandexMainPage yandexMainPage = new YandexMainPage(driver);
        yandexMainPage.open();
        yandexMainPage.openScrollbarServices();
        yandexMainPage.openMarket();

        YandexMarketMainPage yandexMarketMainPage = new YandexMarketMainPage(driver);
        yandexMarketMainPage.openCatalog();
        yandexMarketMainPage.hoverToCategory(category);
        yandexMarketMainPage.openSubcategory(subcategory);

        YandexMarketCategoryPage yandexMarketCategoryPage = new YandexMarketCategoryPage(driver);
        yandexMarketCategoryPage.getManufacturerFilter().clickShowAllButton();
        yandexMarketCategoryPage.getManufacturerFilter().chooseManufacturers(manufacturersForFilter);
        yandexMarketCategoryPage.getPriceFilter().setMinPrice(minPriceForFilter);
        yandexMarketCategoryPage.getPriceFilter().setMaxPrice(maxPriceForFilter);

        List<Snippet> snippets = yandexMarketCategoryPage.getAllSnippetFromPage();
        Assertions.assertTrue(snippets.size() > snippetsNumber, "На странице отображается меньше " + snippetsNumber + "элементов.");

        Assertions.assertTrue(areAllSnippetOnPageMatchTheFilter(snippets, manufacturersForFilter, minPriceForFilter, maxPriceForFilter),
                "Не все предложения на 1 странице соответствуют фильтру.");

        SearchResultsInfo info = getSearchResultsInfo(proxy);

        int randomPage = yandexMarketCategoryPage.goToRandomPage(info);
        snippets = yandexMarketCategoryPage.getAllSnippetFromPage();
        Assertions.assertTrue(areAllSnippetOnPageMatchTheFilter(snippets, manufacturersForFilter, minPriceForFilter, maxPriceForFilter),
                "Не все предложения на странице " + randomPage + " соответствуют фильтру.");

        int lastPage = yandexMarketCategoryPage.goToLastPage(info);
        snippets = yandexMarketCategoryPage.getAllSnippetFromPage();
        Assertions.assertTrue(areAllSnippetOnPageMatchTheFilter(snippets, manufacturersForFilter, minPriceForFilter, maxPriceForFilter),
                "Не все предложения на странице " + lastPage + " соответствуют фильтру.");

        yandexMarketCategoryPage.goToFirstPage();
        Snippet firstSnippet = yandexMarketCategoryPage.getAllSnippetFromPage().stream().findFirst().get();
        yandexMarketCategoryPage.search(firstSnippet.name);
        snippets = yandexMarketCategoryPage.getAllSnippetFromPage();
        Assertions.assertTrue(isProductPresentOnPage(snippets, firstSnippet),"Товар \"" + firstSnippet.name + "\" отсутствует в результатах поиска.");
    }
}
