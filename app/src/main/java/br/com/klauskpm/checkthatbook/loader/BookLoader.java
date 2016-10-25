package br.com.klauskpm.checkthatbook.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import br.com.klauskpm.checkthatbook.Book;
import br.com.klauskpm.checkthatbook.api.BooksSimpleApi;

/**
 * Created by Kazlauskas on 22/10/2016.
 */
public class BookLoader extends AsyncTaskLoader<ArrayList<Book>> {
    private String mQuery;

    /**
     * Instantiates a new Book loader.
     *
     * @param context the context
     * @param query   the query
     */
    public BookLoader(Context context, String query) {
        super(context);

        mQuery = query;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        Log.d("KAZLAUSKAS", "loadInBackground: bookloader " + mQuery);
        if (mQuery == null) {
            return null;
        }

        return BooksSimpleApi.listVolumes(mQuery);
    }
}
