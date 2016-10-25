package br.com.klauskpm.checkthatbook.api;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import br.com.klauskpm.checkthatbook.Book;
import br.com.klauskpm.checkthatbook.utils.QueryUtils;

/**
 * Created by Kazlauskas on 22/10/2016.
 */
public class BooksSimpleApi extends QueryUtils {
    private final static String BASE_URL = "https://www.googleapis.com/books/v1/";

    /**
     * Instantiates a new Books simple api.
     */
    public BooksSimpleApi() {
        CONNECT_TIMEOUT = 20000;
        READ_TIMEOUT = 15000;
    }

    /**
     * List volumes array list.
     *
     * @param q the q
     * @return the array list
     */
    public static ArrayList<Book> listVolumes(String q) {
        return BooksVolumes.listVolumes(q);
    }

    private static class BooksVolumes {
        private final static String COLLECTION_URL = BooksSimpleApi.BASE_URL + "volumes";

        private final static String DOWNLOAD = "epub";
        private static int MAX_RESULTS = 10;
        private static String SHOW_PREORDERS = "false";

        private static ArrayList<Book> listVolumes(String query) {
            String url = COLLECTION_URL;

            String urlFinal = null;
            try {
                urlFinal = url + "?q=" + URLEncoder.encode(query, "UTF-8") +
                        "&download=" + DOWNLOAD +
                        "&&maxResults=" + MAX_RESULTS +
                        "&showPreorders=" + SHOW_PREORDERS;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Log.d("KAZLAUSKAS", "listVolumes: " + urlFinal);

            String response = requestGet(urlFinal);

            ArrayList<Book> books = new ArrayList<Book>();

            if (TextUtils.isEmpty(response)) {
                return books;
            }

            try {
                JSONObject responseJSON = new JSONObject(response);
                JSONArray items = responseJSON.optJSONArray("items");

                if (items == null)
                    return books;

                for (int i = 0; i < items.length(); i++) {
                    books.add(Book.extractFromJSON(items.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return books;
        }
    }
}
