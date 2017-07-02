package io.github.timladenov.project_7_book_listing_app;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by tmladenov on 02.07.17.
 */

public class BooksLoader extends AsyncTaskLoader<List<Books>> {

    private String url;

    public BooksLoader(Context context, String URL) {
        super(context);
        this.url = URL;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Books> loadInBackground() {
        if(url == null) {
            return null;
        }

        List<Books> quakes = BooksUtils.fetchEarthquakeData(url);
        return quakes;
    }
}