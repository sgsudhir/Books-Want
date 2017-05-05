package com.imca2017.bookswant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.imca2017.bookswant.adapter.DeepDataListAdapter;
import com.imca2017.bookswant.app.AppController;
import com.imca2017.bookswant.helper.DownloadFromUrl;
import com.imca2017.bookswant.pojo.deepdata.DeepDataContainer;

import java.util.ArrayList;
import java.util.List;

public class DeepWebResultActivity extends AppCompatActivity {
    ListView listView;
    DeepDataListAdapter adapter;
    List<String> title = new ArrayList<String>();
    List<String> author = new ArrayList<String>();
    List<String> year = new ArrayList<String>();
    List<String> page = new ArrayList<String>();
    List<String> size = new ArrayList<String>();
    List<String> type = new ArrayList<String>();
    List<String> link = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_web_result);
        try {
            for (DeepDataContainer object :AppController.getInstance().getDeepSearchObjectses().getNodes()) {
                title.add(object.getTitle());
                author.add(object.getAuthors());
                year.add(object.getYear());
                page.add(object.getPages());
                size.add(object.getSize());
                type.add(object.getType());
                link.add(object.getDownloadLink());
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                this.finish();
                Toast.makeText(getApplicationContext(), "Error: Please try again", Toast.LENGTH_SHORT).show();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }

        listView = (ListView) findViewById(R.id.list_deep_results);
        adapter = new DeepDataListAdapter(this, title, author, year, page, size, type, link);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeepDataContainer container = AppController.getInstance().getDeepSearchObjectses().getNodes().get(position);
                String url = container.getDownloadLink();
                Toast.makeText(getApplicationContext(), url + "", Toast.LENGTH_SHORT).show();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
/*                ProgressDialog progressDialog = new ProgressDialog(DeepWebResultActivity.this);
                String path =  (Math.random() * 9) + "." + container.getType();
                path = path.replaceAll("( )+", "");
                try {
                    new DownloadFromUrl(progressDialog, path)
                            .execute(container.getDownloadLink());
                } catch (Exception e) {
                    e.printStackTrace();
                }
*/            }
        });
    }

}
