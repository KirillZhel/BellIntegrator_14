package ru.yandex.market;

import com.browserup.harreader.model.Har;
import com.browserup.harreader.model.HarEntry;
import com.jayway.jsonpath.JsonPath;
import core.*;
import helpers.UriUtils;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.YandexMainPage;
import pages.YandexMarketMainPage;
import pages.YandexMarketCategoryPage;
import pages.elements.Snippet;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static helpers.WaitUtils.*;

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
    @Feature("Yandex Market")
    @DisplayName("Проверка выдачи товаров")
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
        yandexMarketCategoryPage.manufacturerFilter.clickShowAllButton();
        yandexMarketCategoryPage.manufacturerFilter.chooseManufacturers(manufacturersForFilter);
        yandexMarketCategoryPage.priceFilter.setMinPrice(minPriceForFilter);
        yandexMarketCategoryPage.priceFilter.setMaxPrice(maxPriceForFilter);

        List<Snippet> snippets = yandexMarketCategoryPage.getAllSnippetFromPage();
        Assertions.assertTrue(snippets.size() > snippetsNumber, "На странице отображается меньше " + snippetsNumber + "элементов.");

        Assertions.assertTrue(areAllSnippetOnPageMatchTheFilter(snippets, manufacturersForFilter, minPriceForFilter, maxPriceForFilter),
                "Не все предложения на 1 странице соответствуют фильтру.");

        SearchResultsInfo info = getSearchResultsInfo();

        int randomPage = goToRandomPage(info);
        snippets = yandexMarketCategoryPage.getAllSnippetFromPage();
        Assertions.assertTrue(areAllSnippetOnPageMatchTheFilter(snippets, manufacturersForFilter, minPriceForFilter, maxPriceForFilter),
                "Не все предложения на странице " + randomPage + " соответствуют фильтру.");

        int lastPage = goToLastPage(info);
        snippets = yandexMarketCategoryPage.getAllSnippetFromPage();
        Assertions.assertTrue(areAllSnippetOnPageMatchTheFilter(snippets, manufacturersForFilter, minPriceForFilter, maxPriceForFilter),
                "Не все предложения на странице " + lastPage + " соответствуют фильтру.");

        goToFirstPage();
        Snippet firstSnippet = yandexMarketCategoryPage.getAllSnippetFromPage().stream().findFirst().get();
        yandexMarketCategoryPage.search(firstSnippet.name);
        snippets = yandexMarketCategoryPage.getAllSnippetFromPage();
        Assertions.assertTrue(isProductPresentOnPage(snippets, firstSnippet),"Товар \"" + firstSnippet.name + "\" отсутствует в результатах поиска.");
    }

    /**
     * Метод, который определяет, есть ли искомый товар на странице.
     * @author Кирилл Желтышев
     * @param snippets Список карточек товаров страницы
     * @param snippetForSearch Искомая карточка
     * @return true - если удалось найти товар, false - если товар не удалось найти
     */
    @Step("Определяем, есть ли в предложенных карточках товар искомый товар.")
    private boolean isProductPresentOnPage(List<Snippet> snippets, Snippet snippetForSearch) {
        List<Snippet> filtredSnippet = snippets.stream()
                .filter(snippet -> snippet.name.equalsIgnoreCase(snippetForSearch.name))
                .collect(Collectors.toList());

        return filtredSnippet.stream().anyMatch(snippet -> snippet.price == snippetForSearch.price);
    }

    /**
     * Метод перехода на последнюю страницу выдачи товаров.
     * @author Кирилл Желтышев
     * @param info Объект дополнительной информации поиска
     * @return Номер последней страницы
     */
    @Step("Переходим на последнюю страницу.")
    private int goToLastPage(SearchResultsInfo info) {
        int lastPageNumber = getLastPageNumber(info);
        goToPage(lastPageNumber);
        return lastPageNumber;
    }

    /**
     * Метод перехода на случайную страницу выдачи товаров.
     * @author Кирилл Желтышев
     * @param info Объект дополнительной информации поиска
     * @return Номер случайной страницы
     */
    @Step("Переходим на случайную страницу.")
    private int goToRandomPage(SearchResultsInfo info) {
        int randomPageNumber = getRandomPageNumber(info);
        goToPage(randomPageNumber);
        return randomPageNumber;
    }

    /**
     * Метод перехода на первую страницу выдачи товаров.
     * @author Кирилл Желтышев
     * @return Номер первой страницы
     */
    @Step("Переходим на первую страницу")
    private int goToFirstPage() {
        goToPage(1);
        return 1;
    }

    /**
     * Метод перехода на страницу выдачи товаров.
     * @author Кирилл Желтышев
     * @param pageNumber Номер страницы
     */
    @Step("Переходим на {pageNumber} страницу.")
    private void goToPage(int pageNumber) {
        String randomPageUrl = UriUtils.addQueryParameters(driver.getCurrentUrl(), Map.of("page", String.valueOf(pageNumber)));
        driver.get(randomPageUrl);
    }

    /**
     * Метод, возвращающий случайный номер страницы.
     * @author Кирилл Желтышев
     * @param info Объект дополнительной информации поиска
     * @return Случайный номер страницы
     */
    private int getRandomPageNumber(SearchResultsInfo info) {
        int lastPageNumber = getLastPageNumber(info);
        int firstPageNumber = 1;
        return RandomUtils.nextInt(firstPageNumber + 1, lastPageNumber);
    }

    /**
     * Метод, возвращающий номер последней страницы выдачи.
     * @author Кирилл Желтышев
     * @param info Объект дополнительной информации поиска
     * @return Номер последней страницы
     */
    private int getLastPageNumber(SearchResultsInfo info) {
        return Math.min(info.pagesCount, 30);
    }

    /**
     * Метод проверки карточек товаров на странице на соответствие фильтрам.
     * @author Кирилл Желтышев
     * @param snippets Список карточки товаров
     * @param manufacturers Список производителей, отмеченных в фильтре
     * @param minPrice Минимальная цена
     * @param maxPrice Максимальная цена
     * @return true - в случае, если все товары на странице соответствуют фильтрам, false - если не соответствуют
     */
    @Step("Проверяем на соответствие карточки предложенных товаров фильтру.")
    private boolean areAllSnippetOnPageMatchTheFilter(List<Snippet> snippets, List<String> manufacturers, int minPrice, int maxPrice) {
        return areAllSnippetsContainsAnyStringInName(snippets, manufacturers)
                && areAllSnippetsHaveCurrentPrice(snippets, minPrice, maxPrice);
    }

    /**
     * Метод, возвращающий дополнительную информацию поискового запроса.
     * @author Кирилл Желтышев
     * @return Объект, который содержит в себе дополнительную информацию.
     */
    private SearchResultsInfo getSearchResultsInfo() {
        waitForState(() -> getMostRecentHarEntryForSearchRequest(proxy.getHar()),
                harEntry -> harEntry
                        .getResponse()
                        .getContent()
                        .getSize() > 0);

        return extractLatestSearchResultsResponse(proxy.endHar());
    }

    /**
     * Метод проверки товаров на соответствие цене.
     * @author Кирилл Желтышев
     * @param snippets Список карточки товаров
     * @param minPrice Минимальная цена
     * @param maxPrice Максимальная цена
     * @return true - в случае, если все товары на странице соответствуют фильтру, false - если не соответствуют
     */
    private boolean areAllSnippetsHaveCurrentPrice(List<Snippet> snippets, int minPrice, int maxPrice) {
        return snippets.stream().allMatch(snippet -> snippet.price >= minPrice && snippet.price <= maxPrice);
    }

    /**
     * Метод проверки товаров на наличие в названиях товаров наименования производителя.
     * @author Кирилл Желтышев
     * @param snippets Список карточки товаров
     * @param strings Список строк
     * @return true - в случае, если все товары на странице соответствуют фильтру, false - если не соответствуют
     */
    private boolean areAllSnippetsContainsAnyStringInName(List<Snippet> snippets, List<String> strings) {
        return snippets.stream()
                .map(snippet -> snippet.name.toLowerCase())
                .allMatch(snippetName -> strings.stream()
                        .anyMatch(s -> snippetName.contains(s.toLowerCase())));
    }

    /**
     * Метод, возвращающий дополнительную информацию из файла Har.
     * @author Кирилл Желтышев
     * @param har Файл har
     * @return Дополнительная информация запроса
     */
    private SearchResultsInfo extractLatestSearchResultsResponse(Har har) {
        HarEntry mostRecentEntry = getMostRecentHarEntryForSearchRequest(har);
        String rawJson = mostRecentEntry.getResponse().getContent().getText();

        Map<String, Integer> rawData = (Map<String, Integer>) JsonPath
                .parse(rawJson)
                .read("$.results..visibleSearchResult.*['total', 'itemsPerPage', 'page']", List.class)
                .get(0);

        return new SearchResultsInfo(
                rawData.get("total"),
                rawData.get("itemsPerPage"),
                rawData.get("page")
        );
    }

    /**
     * Получение последней записи Har, содержащей требуемый ответ Яндекс Маркета.
     * @author Кирилл Желтышев
     * @param har Файл har
     * @return запись Har
     */
    private HarEntry getMostRecentHarEntryForSearchRequest(Har har) {
        Optional<HarEntry> mostRecentEntry = har.getLog().findMostRecentEntry(Pattern.compile(".*search/resolveRemoteSearch.*"));
        return mostRecentEntry
                .orElseThrow(() -> new RuntimeException("Не удалось найти требуемый ответ Яндекс Маркета"));
    }
}
