package com.paraschivescu.tudor.searchbook;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = BookLoader.class.getName();

    private String mUrl;
    private View mProgressBar;

    public BookLoader(Context context, String url, View progressBar) {
        super(context);
        mUrl = url;
        mProgressBar = progressBar;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public List<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        Log.i(LOG_TAG, "Fetching books from " + mUrl);
        return QueryUtils.fetchBooks(mUrl);
    }
}
