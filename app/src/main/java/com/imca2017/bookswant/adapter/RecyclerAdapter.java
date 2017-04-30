package com.imca2017.bookswant.adapter;
/**
 * Created by sgsudhir on 30/4/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imca2017.bookswant.MainActivity;
import com.imca2017.bookswant.R;
import com.imca2017.bookswant.pojo.RecyclerDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<RecyclerDataModel> dataSet;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewAuthor;
        TextView textViewPublisher;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.recycler_card_title);
            this.textViewAuthor = (TextView) itemView.findViewById(R.id.recycler_card_author);
            this.textViewPublisher = (TextView) itemView.findViewById(R.id.recycler_card_publisher);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.recycler_card_image);
        }
    }

    public RecyclerAdapter(ArrayList<RecyclerDataModel> data, Context context) {
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_card_layout, parent, false);

      //  view.setOnClickListener(MainActivity.recycleItemOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.MyViewHolder holder, int position) {
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewAuthor = holder.textViewAuthor;
        TextView textViewPublisher = holder.textViewPublisher;
        ImageView imageView = holder.imageViewIcon;

        textViewTitle.setText(dataSet.get(position).getTitle());
        textViewAuthor.setText(dataSet.get(position).getAuthor());
        textViewPublisher.setText(dataSet.get(position).getPublisher());
        Picasso.with(context).load(dataSet.get(position).getImageUrl()).into(imageView);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}