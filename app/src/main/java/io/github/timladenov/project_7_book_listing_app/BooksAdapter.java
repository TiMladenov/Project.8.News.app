package io.github.timladenov.project_7_book_listing_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tmladenov on 02.07.17.
 */

public class BooksAdapter extends ArrayAdapter<Books> {

    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView published;
    private TextView pageCount;
    private TextView language;
    private ImageView image;
    private Context context;
    private String unknown;


    /*
    * Populates the fields of the adapter with the corresponding data
    * */

    public BooksAdapter(Context context, List<Books> booksList) {
        super(context, 0, booksList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        unknown = context.getResources().getString(R.string.unknown);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);
        }

        Books currentBook = getItem(position);

        image = (ImageView) convertView.findViewById(R.id.bookImage);
        Picasso.with(getContext()).load(currentBook.getThumbnail()).into(image);

        bookTitle = (TextView) convertView.findViewById(R.id.bookTitle_text);
        if (currentBook.getBookTitle() != null && !currentBook.getBookTitle().equals("")) {
            bookTitle.setText(currentBook.getBookTitle());
        } else {
            bookTitle.setText(unknown);
        }
        bookAuthor = (TextView) convertView.findViewById(R.id.bookAuthor_text);
        if (currentBook.getAuthor() != null && !currentBook.getAuthor().equals("")) {
            bookAuthor.setText(currentBook.getAuthor());
        } else {
            bookAuthor.setText(unknown);
        }

        published = (TextView) convertView.findViewById(R.id.publishedDate_text);
        if (currentBook.getPublishedDate() != null && !currentBook.getPublishedDate().equals("")) {
            published.setText(currentBook.getPublishedDate());
        } else {
            published.setText(unknown);
        }

        pageCount = (TextView) convertView.findViewById(R.id.pageCount_text);
        if (currentBook.getPageCount() != 0) {
            pageCount.setText(Integer.toString(currentBook.getPageCount()));
        } else {
            pageCount.setText(unknown);
        }

        language = (TextView) convertView.findViewById(R.id.language_text);
        if (currentBook.getBookLanguage() != null && !currentBook.getBookLanguage().equals("")) {
            language.setText(currentBook.getBookLanguage());
        } else {
            language.setText(unknown);
        }

        return convertView;
    }
}
