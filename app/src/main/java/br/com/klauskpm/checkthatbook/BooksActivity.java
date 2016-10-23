package br.com.klauskpm.checkthatbook;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import java.util.ArrayList;

/**
 * The type Books activity.
 */
public class BooksActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<Book>> {

    private static String TAG = "KAZLAUSKAS";

    private static final int BOOK_LOADER_ID = 1;

    private String mQuery = null;
    private BookAdapter mAdapter;
    private ConnectivityManager mConnectivityManager;
    private SearchView mSearchView;

    private OnQueryTextListener mOnQueryTextListener = new OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            if (isConnected()) {
                Log.d(TAG, "onQueryTextSubmit");
                initLoader(query);
            }

            mSearchView.clearFocus();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        mSearchView = (SearchView) findViewById(R.id.search_query);

        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        GridView grid = (GridView) findViewById(R.id.list);

        grid.setAdapter(mAdapter);
        grid.setEmptyView(findViewById(R.id.empty_list));

        mSearchView.setOnQueryTextListener(mOnQueryTextListener);
    }

    /**
     * Verify if it is connected to the internet
     *
     * @return true if it's connected and false if it's not
     */
    private boolean isConnected() {
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Init the loader
     * @param query to search
     */
    private void initLoader(String query) {
        Log.d(TAG, "initLoader");

        mQuery = query;
        LoaderManager loaderManager = getLoaderManager();
        Loader loader = loaderManager.getLoader(BOOK_LOADER_ID);

        Log.d(TAG, "initLoader: verifying loader " + loader);

        if (loader != null) {
            Log.d(TAG, "initLoader: restart");
            loaderManager.restartLoader(BOOK_LOADER_ID, null, this);
        } else {
            Log.d(TAG, "initLoader: init");
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");
        return new BookLoader(this, mQuery);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> data) {
        Log.d(TAG, "onLoadFinished");
        mAdapter.clear();

        Log.d(TAG, "onLoadFinished:are data null? " + (data == null));
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        mAdapter.clear();
    }
}
