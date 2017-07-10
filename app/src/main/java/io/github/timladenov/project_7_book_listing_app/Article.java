package io.github.timladenov.project_7_book_listing_app;

public class Article {
    private String sectionName;
    private String publication;
    private String title;
    private String url;

    public Article(String sectionName, String publication, String title, String url) {
        this.sectionName = sectionName;
        this.publication = publication;
        this.title = title;
        this.url = url;
    }

    public String getSectionName() {
        return this.sectionName;
    }

    public String getPublication() {
        return this.publication;
    }

    public String getTitle() {
        return this.title;
    }

    public String getUrl() {
        return this.url;
    }
}
