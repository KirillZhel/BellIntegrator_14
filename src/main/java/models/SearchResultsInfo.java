package models;

/**
 * Класс, содержающий дополнительную информацию о странице с товарами.
 * @author Кирилл Желтышев
 */
public class SearchResultsInfo {
    /**
     * Общее количество товаров, которое удалосб найти.
     * @author Кирилл Желтышев
     */
    public final int total;
    /**
     * Количество товаров, представленных на одной странице.
     * @author Кирилл Желтышев
     */
    public final int itemsPerPage;
    /**
     * Страница, открытая на данный момент.
     * @author Кирилл Желтышев
     */
    public final int page;
    /**
     * Количество страниц.
     * @author Кирилл Желтышев
     */
    public final int pagesCount;

    /**
     * Конструктор класса
     * @author Кирилл Желтышев
     * @param total Общее количество товаров, которое удалосб найти.
     * @param itemsPerPage Количество товаров, представленных на одной странице.
     * @param page Страница, открытая на данный момент.
     */
    public SearchResultsInfo(int total, int itemsPerPage, int page) {
        this.total = total;
        this.itemsPerPage = itemsPerPage;
        this.page = page;
        this.pagesCount = (int) Math.ceil((double)this.total / this.itemsPerPage);
    }
}
