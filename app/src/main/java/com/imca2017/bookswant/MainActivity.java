package com.imca2017.bookswant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.imca2017.bookswant.app.AppController;
import com.imca2017.bookswant.helper.UrlGenerator;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MaterialSearchView searchView;
    ArrayList<String> sugg = new ArrayList<String>();
    ProgressDialog pDialog;
    SwitchCompat switchCompat;
    UrlGenerator urlGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#FF5E5E5E"));
        urlGenerator = new UrlGenerator();

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
        //Dummy Data for the Demo
        sugg.add("Android");
        sugg.add("Another love");
        sugg.add("Afterglow");
        sugg.add("Ios");
        sugg.add("Iphone");

        String[] suggestions = new String[sugg.size()];
        suggestions = sugg.toArray(suggestions);
        searchView.setSuggestions(suggestions);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("-----SEARCH URL-----", urlGenerator.getURL());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                urlGenerator.setSearchQuery(newText);
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });

        switchCompat = (SwitchCompat) findViewById(R.id.content_main_switch_download);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    urlGenerator.setDownload(true);
                } else if (!isChecked) {
                    urlGenerator.setDownload(false);
                }
            }
        });

    }

    public void orderbyRadioButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.content_main_radio_orderby_newest:
                urlGenerator.setOrderBy(UrlGenerator.ORDERBY_NEWEST);
                break;
            case R.id.content_main_radio_orderby_relevance:
                urlGenerator.setOrderBy(UrlGenerator.ORDERBY_RELEVANCE);
                break;
            case R.id.content_main_radio_orderby_anything:
                urlGenerator.setOrderBy(UrlGenerator.ORDERBY_ANYTHING);
                break;
            default:
                break;

        }
    }

    public void printtypeRadioButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.content_main_radio_printtype_all:
                urlGenerator.setPrintType(UrlGenerator.PRINT_TYPE_ALL);
                break;
            case R.id.content_main_radio_printtype_books:
                urlGenerator.setPrintType(UrlGenerator.PRINT_TYPE_BOOKS);
                break;
            case R.id.content_main_radio_printtype_magazines:
                urlGenerator.setPrintType(UrlGenerator.PRINT_TYPE_MAGAZINES);
                break;
            default:
                break;
        }
    }

    public void filterbyRadioButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.content_main_radio_filterby_all:
                urlGenerator.setFilter(UrlGenerator.FILTER_BY_NONE);
                break;
            case R.id.content_main_radio_filterby_free:
                urlGenerator.setFilter(UrlGenerator.FILTER_BY_FREE_EBOOKS);
                break;
            case R.id.content_main_radio_filterby_paid:
                urlGenerator.setFilter(UrlGenerator.FILTER_BY_PAID_EBOOKS);
                break;
            case R.id.content_main_radio_filterby_full:
                urlGenerator.setFilter(UrlGenerator.FILTER_BY_FULL_EBOOKS);
                break;
            case R.id.content_main_radio_filterby_sample:
                urlGenerator.setFilter(UrlGenerator.FILTER_BY_SAMPLE_EBOOKS);
                break;
            default:
                break;
        }
    }

    //An Example of fatching JSON using Volly network Library
    private void loadData() {
        String url= "https://www.googleapis.com/books/v1/volumes?q=you+can+win&&maxResults=10";

        pDialog = new ProgressDialog(MainActivity.this);

        String tag_json_obj = "json_obj_req";

        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("--Results--", response.toString());
                        pDialog.hide();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("--Results--", error.getMessage());
                pDialog.hide();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, tag_json_obj);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }
}
