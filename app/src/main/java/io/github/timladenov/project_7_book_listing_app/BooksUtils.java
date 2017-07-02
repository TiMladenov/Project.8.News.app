package io.github.timladenov.project_7_book_listing_app;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tmladenov on 02.07.17.
 */

public class BooksUtils extends AppCompatActivity {
    private static final String LOG_TAG = BooksUtils.class.getSimpleName();

    private BooksUtils() {}

    public static List<Books> fetchEarthquakeData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPcall(url);
        } catch (IOException e) {
            //TODO Add context to constructor, access string from resource
            Log.e(LOG_TAG, "Problem making HTTP request\\n");
            e.printStackTrace();
        }

        List<Books> bookInfo = extractDataFromJSON(jsonResponse);

        return bookInfo;
    }

    private static URL createUrl(String url) {
        URL formUrl = null;
        try {
            formUrl = new URL(url);
        } catch (MalformedURLException e) {
            //TODO Add context to constructor, access string from resource
            Log.e(LOG_TAG, "Error while creating URL\\n");
            e.printStackTrace();
        }
        return formUrl;
    }

    private static String makeHTTPcall(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                //TODO Add context to constructor, access string from resource
                Log.e(LOG_TAG, "Error response code:\\t" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            //TODO Add context to constructor, access string from resource
            Log.e(LOG_TAG, "Can't retrieve the books JSON", e);
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }

            if(inputStream != null) {
                inputStream.close();
            }
            return jsonResponse;
        }
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Books> extractDataFromJSON(String booksJson) {
        if (TextUtils.isEmpty(booksJson)) {
            return null;
        }
        List<Books> booksList = new ArrayList<>();
        try {
            JSONObject baseBookJsonResp = new JSONObject(booksJson);

            //TODO pass books count value to Int variable in MainActivity
            int totalBooksFound = baseBookJsonResp.optInt("totalItems");

            JSONArray arrayItems = baseBookJsonResp.getJSONArray("items");

            for (int i = 0; i < arrayItems.length(); i++) {

                JSONObject jsonArrObject = arrayItems.getJSONObject(i);

                JSONObject titleRoot = jsonArrObject.getJSONObject("volumeInfo");
                String title = titleRoot.optString("title");
                String authors = titleRoot.optString("authors");

                //TODO if authors are more than 0 go to array and add all authors to a String
//                JSONArray authors_array = titleRoot.getJSONArray("authors");
//
//                StringBuilder author_list = new StringBuilder();
//                for(int j = 0; j < authors_array.length(); j++) {
//                    String author = authors_array.optString(i);
//                    author_list.append(author);
//                }
//
//                String authors = author_list.toString();

                String publishedDate = titleRoot.optString("publishedDate");
                int pageCount = titleRoot.optInt("pageCount");

                JSONObject imageRes = titleRoot.getJSONObject("imageLinks");
                String thumbnail = imageRes.optString("thumbnail");

                String language = titleRoot.optString("language");
                String previewLink = titleRoot.optString("previewLink");

                Books books = new Books(title, authors, publishedDate, pageCount, thumbnail, language, previewLink);
                booksList.add(books);
            }

        } catch (JSONException e) {
            //TODO Add context to constructor, access string from resource
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return booksList;
    }
}
