package br.com.klauskpm.checkthatbook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kazlauskas on 22/10/2016.
 */

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(Context context, List<Book> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root = convertView;
        ViewHolder holder;
        Book book = getItem(position);

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

        assert book != null;
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

        String currencySymbol;
        Double price = book.getPrice();
        Double previousPrice = book.getPreviousPrice();
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMinimumFractionDigits(2);

        if (price != null) {
            currencySymbol = book.getCurrencySymbol();
            holder.price.setText(String.format("%s%s", currencySymbol,
                    decimalFormat.format(price)));

            if (previousPrice != null) {
                holder.previousPrice.setText(String.format("%s%s", currencySymbol,
                        decimalFormat.format(previousPrice)));
            }
        }

        return root;
    }

    private class ViewHolder {
        ImageView thumbnail;
        TextView title;
        TextView authors;
        TextView price;
        TextView previousPrice;
    }
}
