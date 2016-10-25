package br.com.klauskpm.checkthatbook.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import br.com.klauskpm.checkthatbook.Book;
import br.com.klauskpm.checkthatbook.R;

/**
 * Created by Kazlauskas on 22/10/2016.
 */
public class BookAdapter extends ArrayAdapter<Book> {

    /**
     * Instantiates a new Book adapter.
     *
     * @param context the context
     * @param objects the objects
     */
    public BookAdapter(Context context, List<Book> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Book book = getItem(position);
        assert book != null;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_item, parent, false
            );

            holder = new ViewHolder();
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.book_image);
            holder.title = (TextView) convertView.findViewById(R.id.book_title);
            holder.authors = (TextView) convertView.findViewById(R.id.book_author);
            holder.price = (TextView) convertView.findViewById(R.id.book_price);
            holder.previousPrice = (TextView) convertView.findViewById(R.id.book_previous_price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide.with(getContext()).load(book.getThumbnailLink()).into(holder.thumbnail);

        holder.title.setText(book.getTitle());

        StringBuilder authorsBuilder = new StringBuilder();
        ArrayList<String> authors = book.getAuthors();
        for (int i = 0; i < authors.size(); i++) {
            if (authorsBuilder.length() > 0)
                authorsBuilder.append(", ").append(authors.get(i));
            else
                authorsBuilder.append(authors.get(i));
        }
        holder.authors.setText(authorsBuilder.toString());

        String formattedPrice = book.getFormattedPrice();
        String formattedPreviousPrice = book.getFormattedPreviousPrice();

        if (formattedPrice != null) {
            holder.price.setText(formattedPrice);

            if (formattedPreviousPrice != null)
                holder.previousPrice.setText(formattedPreviousPrice);
        }

        holder.thumbnail.setOnClickListener(mInfoLinkOnClickListener(book));
        holder.title.setOnClickListener(mInfoLinkOnClickListener(book));
        holder.authors.setOnClickListener(mInfoLinkOnClickListener(book));

        holder.price.setOnClickListener(mBuyLinkOnClickListener(book));
        holder.previousPrice.setOnClickListener(mBuyLinkOnClickListener(book));

        return convertView;
    }

    private class ViewHolder {
        /**
         * The Thumbnail.
         */
        ImageView thumbnail;
        /**
         * The Title.
         */
        TextView title;
        /**
         * The Authors.
         */
        TextView authors;
        /**
         * The Price.
         */
        TextView price;
        /**
         * The Previous price.
         */
        TextView previousPrice;
    }

    /**
     * Create the click listener to send to the info link
     *
     * @param book with the info link
     * @return the click listener to access the info link
     */
    private View.OnClickListener mInfoLinkOnClickListener(final Book book) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL(book.getInfoLink());
            }
        };
    }

    /**
     * Create the click listener to send to the buy link
     *
     * @param book with the buy link
     * @return the click listener to access the buy link
     */
    private View.OnClickListener mBuyLinkOnClickListener(final Book book) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL(book.getBuyLink());
            }
        };
    }

    /**
     * Open an external URL
     *
     * @param url to open
     */
    private void openURL(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        getContext().startActivity(intent);
    }
}
