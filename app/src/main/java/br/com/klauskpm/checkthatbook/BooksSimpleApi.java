package br.com.klauskpm.checkthatbook;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
        private final static String COLLECTION_URL = "volumes";
        private final static String DOWNLOAD = "epub";
        private final static int MAX_RESULTS = 10;
        private final static String SHOW_PREORDERS = "false";

        private static ArrayList<Book> listVolumes(String query) {
            String url = BooksSimpleApi.BASE_URL + COLLECTION_URL;

            String urlFinal = url + "?q=" + query +
                    "&download=" + DOWNLOAD +
                    "&&maxResults=" + MAX_RESULTS +
                    "&showPreorders=" + SHOW_PREORDERS;

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
