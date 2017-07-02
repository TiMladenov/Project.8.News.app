package io.github.timladenov.project_7_book_listing_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

    public BooksAdapter(Context context, List<Books> booksList) {
        super(context, 0, booksList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);
        }

        Books currentBook = getItem(position);

        //TODO Add check for empty values -> hide views if empty
        //TODO Add horizontal scroll for book title - may be too large even for horizontal
        bookTitle = (TextView) convertView.findViewById(R.id.bookTitle_text);
        bookTitle.setText(currentBook.getBookTitle());
        bookAuthor = (TextView) convertView.findViewById(R.id.bookAuthor_text);
        bookAuthor.setText(currentBook.getAuthor());
        published = (TextView) convertView.findViewById(R.id.publishedDate_text);
        published.setText(currentBook.getPublishedDate());
        pageCount = (TextView) convertView.findViewById(R.id.pageCount_text);
        pageCount.setText(Integer.toString(currentBook.getPageCount()));
        language = (TextView) convertView.findViewById(R.id.language_text);
        language.setText(currentBook.getBookLanguage());

        return convertView;
    }
}
