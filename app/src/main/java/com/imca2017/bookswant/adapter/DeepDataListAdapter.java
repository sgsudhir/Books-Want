package com.imca2017.bookswant.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imca2017.bookswant.R;

import java.util.List;

/**
 * Created by sgsudhir on 4/5/17.
 */

public class DeepDataListAdapter extends BaseAdapter {
    List<String> title, author, year, page, size, type, link;
    private static LayoutInflater inflater=null;
    TextView textViewTitle, textViewAuthor, textViewYear, textViewPage, textViewSize, textViewType;

    public DeepDataListAdapter(Activity activity, List<String> title,
                               List<String> author, List<String> year,
                               List<String> page, List<String> size,
                               List<String> type, List<String> link) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.page = page;
        this.size = size;
        this.type = type;
        this.link = link;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return title.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.list_card_layout, null);
        }
        textViewTitle = (TextView) view.findViewById(R.id.list_card_layout_title);
        textViewAuthor = (TextView) view.findViewById(R.id.list_card_layout_author);
        textViewYear = (TextView) view.findViewById(R.id.list_card_layout_year);
        textViewPage = (TextView) view.findViewById(R.id.list_card_layout_page);
        textViewSize = (TextView) view.findViewById(R.id.list_card_layout_size);
        textViewType = (TextView) view.findViewById(R.id.list_card_layout_type);

        textViewTitle.setText(title.get(position));
        textViewAuthor.setText(author.get(position));
        textViewType.setText(type.get(position));
        textViewPage.setText(page.get(position));
        textViewSize.setText(size.get(position));
        textViewYear.setText(year.get(position));

        return view;
    }

}
