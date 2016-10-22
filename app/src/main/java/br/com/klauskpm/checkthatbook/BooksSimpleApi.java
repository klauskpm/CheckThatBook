package br.com.klauskpm.checkthatbook;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kazlauskas on 22/10/2016.
 */

public class BooksSimpleApi extends QueryUtils {
    private final static String BASE_URL = "https://www.googleapis.com/books/v1/";

    public BooksSimpleApi() {
        connectTimeout = 20000;
        readTimeout = 15000;
    }

    public static ArrayList<Book> listVolumes(String q) {
        return BooksVolumes.listVolumes(q);
    }

    private static class BooksVolumes {
        private final static String COLLECTION_URL = "volumes";
        private final static String DOWNLOAD = "epub";
        private final static int MAX_RESULTS = 10;
        private final static String SHOW_PREORDERS = "false";

        public static ArrayList<Book> listVolumes(String query) {
            String url = BooksSimpleApi.BASE_URL + COLLECTION_URL;

            String response = requestGet(url + "?q=" + query +
                    "&download=" + DOWNLOAD +
                    "&&maxResults=" + MAX_RESULTS +
                    "&showPreorders=" + SHOW_PREORDERS);

            ArrayList<Book> books = new ArrayList<Book>();

            try {
                JSONObject responseJSON = new JSONObject(response);
                JSONArray items = responseJSON.getJSONArray("items");

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
