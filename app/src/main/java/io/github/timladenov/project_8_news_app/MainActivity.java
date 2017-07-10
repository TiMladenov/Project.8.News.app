package io.github.timladenov.project_8_news_app;

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

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private final String URL_SEARCH = "https://content.guardianapis.com/search?api-key=acb710a9-cece-466f-b264-6805aa52b74a&q=";

    private String mUserQuery;
    private String Guardian_API_URL = URL_SEARCH;

    private ListView listView;
    private ArticleAdapter mAdapter;
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
                networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    progressBar.setVisibility(View.VISIBLE);
                    mEmptyState.setVisibility(View.GONE);
                    mUserQuery = MainActivity.clearQuery(userEntry.getText().toString());
                    Guardian_API_URL += mUserQuery;
                    getLoaderManager().restartLoader(1, null, MainActivity.this);
                    mUserQuery = "";
                    Guardian_API_URL = URL_SEARCH;
                } else {
                    showWelcome = false;
                    mAdapter.clear();
                }
            }
        });


        /*
        * Initiates ListView and Adapter population with data
        * */

        listView = (ListView) findViewById(R.id.list);
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());
        listView.setAdapter(mAdapter);

        /*
        * Gets the URL to the article in The Guardian if the user selects a particular article in the app
        * */

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article selectedBook = mAdapter.getItem(position);
                if (selectedBook.getUrl() != null) {
                    Intent visit = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedBook.getUrl()));
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
            mAdapter.clear();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("showWelcome", showWelcome);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(this, Guardian_API_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (showWelcome) {
            mEmptyState.setText(getResources().getString(R.string.welcome_txt));
            showWelcome = false;
        }
        else if(networkInfo == null) {
            mEmptyState.setText(getResources().getString(R.string.no_internet));
        }
        else {
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
    public void onLoaderReset(Loader<List<Article>> loader) {
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