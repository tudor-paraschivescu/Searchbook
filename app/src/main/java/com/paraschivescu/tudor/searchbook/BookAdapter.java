package com.paraschivescu.tudor.searchbook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(@NonNull Context context, List<Book> books) {
        super(context, 0, books);
    }

    private class ViewHolder {
        Book mBook;

        void setBook(Book book) {
            mBook = book;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
/*        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
*/
        // Get the book placed at that position
        Book book = getItem(position);

        // Create holder and inflater
        final ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) getContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (listItemView == null) {

            listItemView = inflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.setBook(book);
            listItemView.setTag(holder);
        }
        else {
            holder = (ViewHolder) listItemView.getTag();
        }

        book = holder.mBook;

        ImageView cover = (ImageView) listItemView.findViewById(R.id.book_cover);
        new DownloadImageTask(cover).execute(book.getLinkToCoverResource());

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.book_title);
        titleTextView.setText(book.getTitle());

        TextView authorsTextView = (TextView) listItemView.findViewById(R.id.book_authors);
        authorsTextView.setText(book.getAuthors());

        return listItemView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView mImageView;

        private DownloadImageTask(ImageView imageView) {
            mImageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {

            if (urls == null || urls.length < 1) {
                return null;
            }

            String mUrl = urls[0];
            Bitmap mIcon = null;

            try {
                InputStream in = new java.net.URL(mUrl).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e(BookActivity.LOG_TAG, e.getMessage());
                e.printStackTrace();
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            mImageView.setImageBitmap(result);
        }
    }
}
