package steps;

import com.browserup.bup.BrowserUpProxyServer;
import com.browserup.harreader.model.Har;
import com.browserup.harreader.model.HarEntry;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Step;
import models.SearchResultsInfo;
import models.Snippet;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static helpers.WaitUtils.waitForState;

public class StepsAll {
    /**
     * Метод, который определяет, есть ли искомый товар на странице.
     * @author Кирилл Желтышев
     * @param snippets Список карточек товаров страницы
     * @param snippetForSearch Искомая карточка
     * @return true - если удалось найти товар, false - если товар не удалось найти
     */
    @Step("Определяем, есть ли в предложенных карточках товар искомый товар.")
    public static boolean isProductPresentOnPage(List<Snippet> snippets, Snippet snippetForSearch) {
        List<Snippet> filtredSnippet = snippets.stream()
                .filter(snippet -> snippet.name.equalsIgnoreCase(snippetForSearch.name))
                .collect(Collectors.toList());

        return filtredSnippet.stream().anyMatch(snippet -> snippet.price == snippetForSearch.price);
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
    public static boolean areAllSnippetOnPageMatchTheFilter(List<Snippet> snippets, List<String> manufacturers, int minPrice, int maxPrice) {
        return areAllSnippetsContainsAnyStringInName(snippets, manufacturers)
                && areAllSnippetsHaveCurrentPrice(snippets, minPrice, maxPrice);
    }

    /**
     * Метод, возвращающий дополнительную информацию поискового запроса.
     * @author Кирилл Желтышев
     * @return Объект, который содержит в себе дополнительную информацию.
     */
    @Step("Берём дополнительную информацию из результата запроса.")
    public static SearchResultsInfo getSearchResultsInfo(BrowserUpProxyServer proxy) {
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
    @Step("Проверяем все карточки товаров на странице на соответствие цене (от {minPrice} до {maxPrice}).")
    public static boolean areAllSnippetsHaveCurrentPrice(List<Snippet> snippets, int minPrice, int maxPrice) {
        return snippets.stream().allMatch(snippet -> snippet.price >= minPrice && snippet.price <= maxPrice);
    }

    /**
     * Метод проверки товаров на наличие в названиях товаров наименования производителя.
     * @author Кирилл Желтышев
     * @param snippets Список карточки товаров
     * @param strings Список строк
     * @return true - в случае, если все товары на странице соответствуют фильтру, false - если не соответствуют
     */
    @Step("Проверяем все карточки товаров на странице на соответствие производителю.")
    public static boolean areAllSnippetsContainsAnyStringInName(List<Snippet> snippets, List<String> strings) {
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
    @Step("Берём из Har определённые значения.")
    public static SearchResultsInfo extractLatestSearchResultsResponse(Har har) {
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
    @Step("Берём из Har определённые записи.")
    public static HarEntry getMostRecentHarEntryForSearchRequest(Har har) {
        Optional<HarEntry> mostRecentEntry = har.getLog().findMostRecentEntry(Pattern.compile(".*search/resolveRemoteSearch.*"));
        return mostRecentEntry
                .orElseThrow(() -> new RuntimeException("Не удалось найти требуемый ответ Яндекс Маркета"));
    }
}
