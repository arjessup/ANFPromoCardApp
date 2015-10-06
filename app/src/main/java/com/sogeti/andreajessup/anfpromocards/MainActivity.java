package com.sogeti.andreajessup.anfpromocards;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //you have a connection
            Log.d("DEBUG", "Has connection");
            setContentView(R.layout.activity_main);
            new DownloadJsonDataTask().execute(getResources().getString(R.string.data_url));
        } else {
            Log.d("DEBUG", "No connection");
            // no connection
            //if there is no cached data, show a nice placeholder
            //if there is cached data, load it.
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DownloadJsonDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String...urls) {
            try {
                return downloadURL(urls[0]);
            } catch (IOException e) {
                Log.d("DEBUG", "IOException");
                return "Unable to retrieve JSON data";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //result is the JSON as string
            Log.d("DEBUG", "RESULT: " + result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray namesArray = jsonObject.names();
                Log.d("DEBUG", "These are the names: " + namesArray);
//                JSONArray jsonArray = jsonObject.getJSONArray("button");
//                Log.d("DEBUG", "THe JSON Array: " + jsonArray.toString());
//
//                Gson gson = new GsonBuilder().create();
//                Promotion promotion = gson.fromJson(result, Promotion.class);
//                System.out.println(promotion);

            } catch (JSONException e) {
                Log.d("DEBUG", "json exception");
            }
        }

        private String downloadURL(String urlString) throws IOException {
            InputStream inputStream = null;
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int connectionResponse = connection.getResponseCode();
                inputStream = connection.getInputStream();
                String jsonAsString = readInputStream(inputStream);
                return jsonAsString;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }

        private String readInputStream(InputStream inputStream) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(inputStream, "UTF-8");
            char[] buffer = new char[826];
            reader.read(buffer);
            return new String(buffer);
        }
    }
}
