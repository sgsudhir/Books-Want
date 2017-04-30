package com.imca2017.bookswant;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.imca2017.bookswant.adapter.RecyclerAdapter;
import com.imca2017.bookswant.app.AppController;
import com.imca2017.bookswant.listener.RecyclerTouchListener;
import com.imca2017.bookswant.pojo.RecyclerDataModel;
import com.imca2017.bookswant.pojo.search.Item;
import com.imca2017.bookswant.pojo.search.SearchResults;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {
    LinearLayout layout;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private  RecyclerView recyclerView;
    private static ArrayList<RecyclerDataModel> data;
    public static View.OnClickListener recycleItemOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        layout = (LinearLayout) findViewById(R.id.layout_linear_dynamic);

/*        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Display display = getWindowManager().getDefaultDisplay();
        layout.getLayoutParams().height = (width/10)*3;
        layout.getLayoutParams().width = (width/10)*3;
        layout.requestLayout();
*/
    //    recycleItemOnClickListener = new RecycleItemOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.search_result_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        listGenerator();
        adapter = new RecyclerAdapter(data, this);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getApplicationContext(), position + " clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

 /*   private static class RecycleItemOnClickListener implements View.OnClickListener {

        private RecycleItemOnClickListener(Context context) {
            this.context = context;
        }

        private final Context context;

        @Override
        public void onClick(View v) {

            Toast.makeText(context.getApplicationContext(), v.getId() + " clicked", Toast.LENGTH_SHORT).show();
        }
    }
*/



    private void listGenerator() {

        data = new ArrayList<RecyclerDataModel>();
        SearchResults results = AppController.getInstance().getmSearchResultsObject();
        String title = null;
        String authors = null;
        String publisher = null;
        String imageLink = null;
        for (Item item :  results.getItems()) {
            try {
                title = item.getVolumeInfo().getTitle();
            } catch (Exception e) {
                e.printStackTrace();
            }
            authors = null;
            try {
                for (String author : item.getVolumeInfo().getAuthors()) {
                    if (authors == null) {
                        authors = author;
                    } else {
                        authors = authors + ", " + author;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                publisher = item.getVolumeInfo().getPublisher();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                imageLink = item.getVolumeInfo().getImageLinks().getThumbnail();
            } catch (Exception e) {
                e.printStackTrace();
            }
            data.add(new RecyclerDataModel(title, authors, publisher, imageLink));

        }

    }
}
