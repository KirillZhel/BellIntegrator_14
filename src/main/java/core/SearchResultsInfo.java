package core;

public class SearchResultsInfo {
    public final int total;
    public final int itemsPerPage;
    public final int page;
    private final int pagesCount;

    public SearchResultsInfo(int total, int itemsPerPage, int page) {
        this.total = total;
        this.itemsPerPage = itemsPerPage;
        this.page = page;
        this.pagesCount = (int) Math.ceil((double)this.total / this.itemsPerPage);
    }
}
