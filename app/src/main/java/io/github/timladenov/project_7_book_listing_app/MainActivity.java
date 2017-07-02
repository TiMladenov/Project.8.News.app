package io.github.timladenov.project_7_book_listing_app;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>> {

    //TODO create link as per user entry from EditText field
    //TODO create check for correct spelling and appending of search criteria words - replace " " with "+", do it with split()
    //TODO add ListView onItemClickListener to open website link to the book
    //TODO add state save on change rotation on entered search word and search results. results must remain the same
    //TODO add method to load book thumbnail programmatically
    //TODO add empty state message for missing results and online connection
    //TODO Check for memory leaks
    private String GOOGLE_BOOKS_API_URL =
            "https://www.googleapis.com/books/v1/volumes?startIndex=0&projection=full&printType=books&showPreorders=false&maxResults=40&orderBy=relevance&filter=ebooks&q=android";

    private ListView listView;
    private BooksAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        mAdapter = new BooksAdapter(this, new ArrayList<Books>());
        listView.setAdapter(mAdapter);

        LoaderManager manager = getLoaderManager();
        manager.initLoader(1, null, this);
    }

    @Override
    public Loader<List<Books>> onCreateLoader(int id, Bundle args) {
        return new BooksLoader(this, GOOGLE_BOOKS_API_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> data) {
        mAdapter.clear();
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        mAdapter.clear();
    }
}