package io.github.timladenov.project_7_book_listing_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {

    /*
    * Populates the fields of the adapter with the corresponding data
    * */

    public ArticleAdapter(Context context, List<Article> booksList) {
        super(context, 0, booksList);
    }

    static class ViewHolder {
        private TextView bookTitle;
        private TextView bookAuthor;
        private TextView published;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.bookTitle = (TextView) convertView.findViewById(R.id.title_text);
            viewHolder.bookAuthor = (TextView) convertView.findViewById(R.id.section_text);
            viewHolder.published = (TextView) convertView.findViewById(R.id.publishedDate_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Article currentBook = getItem(position);

        viewHolder.bookTitle.setText(currentBook.getTitle());
        viewHolder.bookAuthor.setText(currentBook.getSectionName());
        viewHolder.published.setText(currentBook.getPublication());

        return convertView;
    }
}
