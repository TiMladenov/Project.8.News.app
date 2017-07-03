package io.github.timladenov.project_7_book_listing_app;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>> {

    private final String URL_SEARCH = "https://www.googleapis.com/books/v1/volumes?startIndex=0&language=en&maxResults=30&q=";

    //TODO Check for memory leaks
    private String mUserQuery;
    private String GOOGLE_BOOKS_API_URL = URL_SEARCH;

    private ListView listView;
    private BooksAdapter mAdapter;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private ProgressBar progressBar;
    private TextView mEmptyState;
    private boolean showWelcome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * Gets the state of the network connection
        * */
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();


        /*
        * Used to display welcome message once per app start
        * */
        if (savedInstanceState != null) {
            showWelcome = savedInstanceState.getBoolean("showWelcome");
        } else {
            showWelcome = true;
        }

        /*
        * Initiates search after the user enters his query
        * */
        final EditText userEntry = (EditText) findViewById(R.id.search_field);
        userEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkInfo != null && networkInfo.isConnected()) {
                    progressBar.setVisibility(View.VISIBLE);
                    mEmptyState.setVisibility(View.GONE);
                    mUserQuery = MainActivity.clearQuery(userEntry.getText().toString());
                    GOOGLE_BOOKS_API_URL += mUserQuery;
                    getLoaderManager().restartLoader(1, null, MainActivity.this);
                    mUserQuery = "";
                    GOOGLE_BOOKS_API_URL = URL_SEARCH;
                }
            }
        });


        /*
        * Initiates ListView and Adapter population with data
        * */

        listView = (ListView) findViewById(R.id.list);
        mAdapter = new BooksAdapter(this, new ArrayList<Books>());
        listView.setAdapter(mAdapter);

        /*
        * Gets the URL to the book in Google Books if the user selects a particular book
        * */

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Books selectedBook = mAdapter.getItem(position);
                if (selectedBook.getPreviewLink() != null) {
                    Intent visit = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedBook.getPreviewLink()));
                    startActivity(visit);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_no_url), Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
        * Progress bar to display during loading and empty state to display in there's no connectivity
        * */
        progressBar = (ProgressBar) findViewById(R.id.load_progress);
        progressBar.setVisibility(View.VISIBLE);
        mEmptyState = (TextView) findViewById(R.id.empty_state);
        listView.setEmptyView(mEmptyState);

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager manager = getLoaderManager();
            manager.initLoader(1, null, this);

        } else {
            progressBar.setVisibility(View.GONE);
            mEmptyState.setText(getResources().getString(R.string.no_internet));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("showWelcome", showWelcome);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<List<Books>> onCreateLoader(int id, Bundle args) {
        return new BooksLoader(this, GOOGLE_BOOKS_API_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> data) {
        if (showWelcome) {
            mEmptyState.setText(getResources().getString(R.string.welcome_txt));
            showWelcome = false;
        } else {
            mEmptyState.setText(getResources().getString(R.string.no_result));
        }
        mAdapter.clear();
        progressBar.setVisibility(View.GONE);
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    /*
    * Clears and formats the user entry for search
    * */

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        mAdapter.clear();
    }

    private static String clearQuery(String userSearch) {
        String[] tempArr = userSearch.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < tempArr.length; i++) {
            stringBuilder.append(tempArr[i].toLowerCase());
            if (i < tempArr.length - 1) {
                stringBuilder.append("+");
            }
        }
        return stringBuilder.toString();
    }
}