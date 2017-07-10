package io.github.timladenov.project_7_book_listing_app;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private String url;

    /*
    * Loads the URL and initiates the fetch of JSON data
    * */

    public ArticleLoader(Context context, String URL) {
        super(context);
        this.url = URL;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        if (url == null) {
            return null;
        }

        List<Article> books = ArticleUtils.fetchArticleData(url);
        return books;
    }
}