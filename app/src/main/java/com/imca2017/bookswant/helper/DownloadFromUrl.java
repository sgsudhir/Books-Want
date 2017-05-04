package com.imca2017.bookswant.helper;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;

/**
 * Created by sgsudhir on 4/5/17.
 */

public class DownloadFromUrl extends AsyncTask<String, String, String>{
    ProgressDialog progressDialog;
    String path;

    public DownloadFromUrl(ProgressDialog progressDialog, String path) {
        this.progressDialog = progressDialog;
        this.path = path;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        int count;
        try {
            URL url = new URL(params[0]);
            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "/abc.pdf");
            if (!file.mkdirs()) {
                Log.e("Print", "Directory not created");
            }


            // Output stream to write file
            OutputStream output = new FileOutputStream(path);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress(""+(int)((total*100)/lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(Integer.parseInt(values[0]));
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.hide();
    }
}
