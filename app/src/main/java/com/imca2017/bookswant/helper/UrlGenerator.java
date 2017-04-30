package com.imca2017.bookswant.helper;

/**
 * Created by sgsudhir on 29/4/17.
 */

public class UrlGenerator {
    public static String EPUB = "epub";
    public static String ORDERBY_NEWEST = "newest";
    public static String ORDERBY_RELEVANCE = "relevance";
    public static String ORDERBY_ANYTHING = null;
    public static String PRINT_TYPE_ALL = "all";
    public static String PRINT_TYPE_BOOKS = "books";
    public static String PRINT_TYPE_MAGAZINES = "magazines";
    public static String FILTER_BY_NONE = "ebooks";
    public static String FILTER_BY_FREE_EBOOKS = "free-ebooks";
    public static String FILTER_BY_PAID_EBOOKS = "paid-ebooks";
    public static String FILTER_BY_FULL_EBOOKS = "full";
    public static String FILTER_BY_SAMPLE_EBOOKS = "partial";

    /**
     * Example search query -
     * https://www.googleapis.com/books/v1/volumes?q=you+can+win&&orderBy=newest&&filter=full
     */
    public static String BASE_URL = "https://www.googleapis.com/books/v1/volumes";

    private String searchQuery;
    private boolean download;
    private String filter;
    private String orderBy;
    private String printType;
    private String url;

    public UrlGenerator() {
        this.download = false;
        this.filter = FILTER_BY_NONE;
        this.orderBy = ORDERBY_ANYTHING;
        this.printType = PRINT_TYPE_ALL;
        this.url = null;
    }

    public String getURL() {
        this.url = this.BASE_URL + "?q=" + this.searchQuery + "&&filter=" + this.filter + "&&printType=" + this.printType;
        if (download) {
            this.url = this.url + "&&download=epub";
        }
        if (this.orderBy != null && !this.orderBy.isEmpty()) {
            this.url = this.url + "&&orderBy=" + this.orderBy;
        }
        this.url = this.url + "&&maxResults=40";
        return url;
    }

    public void setSearchQuery(String searchQuery) {
        searchQuery = searchQuery.replaceAll("( )+", "+");
        this.searchQuery = searchQuery;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

}
