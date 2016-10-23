package br.com.klauskpm.checkthatbook;

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
        View root = convertView;
        ViewHolder holder;
        Book book = getItem(position);
        assert book != null;

        if (root == null) {
            root = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_item, parent, false
            );

            holder = new ViewHolder();
            holder.thumbnail = (ImageView) root.findViewById(R.id.book_image);
            holder.title = (TextView) root.findViewById(R.id.book_title);
            holder.authors = (TextView) root.findViewById(R.id.book_author);
            holder.price = (TextView) root.findViewById(R.id.book_price);
            holder.previousPrice = (TextView) root.findViewById(R.id.book_previous_price);

            root.setTag(holder);
        } else {
            holder = (ViewHolder) root.getTag();
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

        return root;
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

    private View.OnClickListener mInfoLinkOnClickListener(final Book book) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL(book.getInfoLink());
            }
        };
    }

    private View.OnClickListener mBuyLinkOnClickListener(final Book book) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL(book.getBuyLink());
            }
        };
    }

    private void openURL(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        getContext().startActivity(intent);
    }
}
