package core;

public class MarketVisibleSearchResults {
    public final int total;
    public final int itemsPerPage;
    public final int page;
    private final int pagesCount;

    public MarketVisibleSearchResults(int total, int itemsPerPage, int page) {
        this.total = total;
        this.itemsPerPage = itemsPerPage;
        this.page = page;
        // это поле пригодится сразу посчитать
        //this.pagesCount = (int) Math.ceil((double)this.total / this.itemsPerPage);
        this.pagesCount = (this.total / this.itemsPerPage) + 1;
    }
}
