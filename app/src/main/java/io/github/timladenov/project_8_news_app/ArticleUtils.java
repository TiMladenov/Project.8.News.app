package io.github.timladenov.project_8_news_app;

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

/*
* Receives a string, converts it to URL, then makes an HTTP call to get the raw JSON data, then extracts the data from the JSON and returns it as a List
* */

public class ArticleUtils extends AppCompatActivity {
    private static final String LOG_TAG = ArticleUtils.class.getSimpleName();

    private ArticleUtils() {
    }

    public static List<Article> fetchArticleData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPcall(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making HTTP request\\n");
            e.printStackTrace();
        }

        List<Article> articlesInfo = extractDataFromJSON(jsonResponse);

        return articlesInfo;
    }

    private static URL createUrl(String url) {
        URL formUrl = null;
        try {
            formUrl = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error while creating URL\\n");
            e.printStackTrace();
        }
        return formUrl;
    }

    private static String makeHTTPcall(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
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

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code:\\t" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Can't retrieve the books JSON", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
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

    private static List<Article> extractDataFromJSON(String booksJson) {
        if (TextUtils.isEmpty(booksJson)) {
            return null;
        }
        List<Article> articleList = new ArrayList<>();
        try {
            JSONObject baseJsonResp = new JSONObject(booksJson);

            if (baseJsonResp.has("response")) {
                JSONObject responseObj = baseJsonResp.getJSONObject("response");
                JSONArray arrayOfObjects = responseObj.getJSONArray("results");

                for (int i = 0; i < arrayOfObjects.length(); i++) {

                    JSONObject jsonArrObject = arrayOfObjects.getJSONObject(i);

                    String sectionName = jsonArrObject.optString("sectionName");

                    //Formats the time to a readable format. Guardian API uses ZULU / UTC time.
                    String publication = jsonArrObject.optString("webPublicationDate");
                    publication = publication.split("T")[0] + ", " + publication.split("T")[1].split("Z")[0] + " UTC";

                    String title = jsonArrObject.optString("webTitle");
                    String url = jsonArrObject.optString("webUrl");

                    Article article = new Article(sectionName, publication, title, url);
                    articleList.add(article);
                }

            }
            return articleList;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return null;
    }
}
