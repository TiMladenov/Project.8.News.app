package io.github.timladenov.project_7_book_listing_app;

/**
 * Created by tmladenov on 02.07.17.
 */

public class Books {
    private String bookTitle;
    private String author;
    private String publishedDate;
    private int pageCount;
    private String thumbnail;
    private String language;
    private String previewLink;

    public Books (String bookTitle, String author, String publishedDate, int pageCount, String thumbnail, String language, String previewLink) {
        this.bookTitle = bookTitle;
        this.author = author;
        this.publishedDate = publishedDate;
        this.pageCount = pageCount;
        this.thumbnail = thumbnail;
        this.language = language;
        this.previewLink = previewLink;
    }

    public String getBookTitle() {
        return this.bookTitle;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getPublishedDate() {
        return this.publishedDate;
    }

    public int getPageCount() {
        return this.pageCount;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public String getBookLanguage() {
        return this.language;
    }

    public String getPreviewLink() {
        return this.previewLink;
    }
}
