package com.paraschivescu.tudor.searchbook;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends Activity
        implements LoaderManager.LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = BookActivity.class.getName();

    private String GOOGLE_BOOKS_REQUEST_URL;
    private String GOOGLE_BOOKS_STARTING_QUERY_URL;

    private BookAdapter bookAdapter;

    private static final int BOOK_LOADER_ID = 1;

    // Check for network connectivity
    private boolean hasStableConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GOOGLE_BOOKS_STARTING_QUERY_URL = getString(R.string.google_books_starting_query);

        // Set the appearance of the task (the color was not the right one on my device)
        ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(
                getString(R.string.app_name),
                BitmapFactory.decodeResource(getResources(), R.mipmap.book_icon),
                getColor(R.color.colorPrimary));
        setTaskDescription(taskDesc);

        // Create and set the adapter of the list view
        ListView listView = (ListView) findViewById(R.id.books_list_view);
        bookAdapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(bookAdapter);

        // Set the empty view of the list view
        listView.setEmptyView(findViewById(R.id.empty_view));

        // Set the on click listener for the search button
        findViewById(R.id.round_search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hasStableConnection()) {

                    findViewById(R.id.empty_view).setVisibility(View.INVISIBLE);
                    findViewById(R.id.no_internet_view).setVisibility(View.INVISIBLE);

                    // Build the search query
                    EditText searchEditText = (EditText) findViewById(R.id.search_edit_text);
                    String searchRequest = searchEditText.getEditableText().toString();
                    searchRequest = searchRequest.replaceAll("\\s+", "");
                    GOOGLE_BOOKS_REQUEST_URL = GOOGLE_BOOKS_STARTING_QUERY_URL + searchRequest;

                    // Restart the loader
                    getLoaderManager().restartLoader(BOOK_LOADER_ID, null, BookActivity.this);

                } else {
                    onLoaderReset(null);
                    findViewById(R.id.progress_bar).setVisibility(View.GONE);
                    findViewById(R.id.empty_view).setVisibility(View.GONE);
                    findViewById(R.id.no_internet_view).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, GOOGLE_BOOKS_REQUEST_URL, findViewById(R.id.progress_bar));
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        bookAdapter.clear();

        if (data != null && !data.isEmpty()) {
            bookAdapter.addAll(data);
        }

        findViewById(R.id.progress_bar).setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        bookAdapter.clear();
    }
}
