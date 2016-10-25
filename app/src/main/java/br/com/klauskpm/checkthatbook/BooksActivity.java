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
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.klauskpm.checkthatbook.adapter.BookAdapter;
import br.com.klauskpm.checkthatbook.loader.BookLoader;

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
    private ProgressBar mSpinnerLoader;
    private TextView mEmptyList;

    private OnQueryTextListener mOnQueryTextListener = new OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            mAdapter.clear();
            Log.d(TAG, "onQueryTextSubmit");
            initLoader(query);

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

        mSpinnerLoader = (ProgressBar) findViewById(R.id.spinner_loader);

        grid.setAdapter(mAdapter);
        mEmptyList = (TextView) findViewById(R.id.empty_list);
        grid.setEmptyView(mEmptyList);

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
        mEmptyList.setVisibility(View.GONE);

        if (!isConnected()) {
            mEmptyList.setVisibility(View.VISIBLE);
            mEmptyList.setText(getString(R.string.no_connection));
            return;
        }

        mSpinnerLoader.setVisibility(View.VISIBLE);

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

        if (!isConnected()) {
            mEmptyList.setVisibility(View.VISIBLE);
            mEmptyList.setText(R.string.no_connection);
            return;
        }

        mEmptyList.setVisibility(View.GONE);
        mSpinnerLoader.setVisibility(View.GONE);

        Log.d(TAG, "onLoadFinished:are data null? " + (data == null));
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        } else {
            mEmptyList.setVisibility(View.VISIBLE);
            mEmptyList.setText(getString(R.string.no_books, mQuery));
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        mAdapter.clear();
    }
}
