package com.sogeti.andreajessup.anfpromocards;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static String FILE_NAME = "json_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadJsonDataTask().execute(getResources().getString(R.string.data_url));
        }

        FileInputStream fileInputStream = null;
        if (fileExists(FILE_NAME)) {
            setContentView(R.layout.activity_main);
            try {
                fileInputStream = openFileInput(FILE_NAME);
                populateTableView(convertStreamToString(fileInputStream));
            } catch (FileNotFoundException fnfe) {
                Log.e("ERROR", "Threw FileNotFoundException", fnfe);
            } catch (Exception e) {
                Log.e("ERROR", "Threw Exception", e);
            } finally {
                try {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                }catch (IOException ioe) {
                    Log.e("ERROR", "Threw IOException", ioe);
                }
            }
        } else {
            setContentView(R.layout.activity_nodata);
        }
    }

    private void populateTableView(String jsonData) {
        Log.d("DEBUG", "This is the json data: " + jsonData);
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("promotions");
            ArrayList<Promotion> promotions = Promotion.promotionsFromJson(jsonArray);
            TableLayout tableLayout = (TableLayout)findViewById(R.id.promotiontable);
            for (final Promotion promotion: promotions) {
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableRow.LayoutParams(1000, 200));
                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundleObject = new Bundle();
                        Intent intent = new Intent(v.getContext(), PromoCardActivity.class);
                        bundleObject.putSerializable("PROMOTION", (Serializable) promotion);
                        intent.putExtras(bundleObject);
                        startActivity(intent);
                    }
                });

                ImageView imageView = new ImageView(this);
                Glide.with(this).load(promotion.getImage()).into(imageView);
                TableRow.LayoutParams imageViewParams = new TableRow.LayoutParams(200, 200);
                imageViewParams.gravity = Gravity.LEFT;
                imageViewParams.bottomMargin = 4;
                imageView.setLayoutParams(imageViewParams);
                tableRow.addView(imageView);

                TextView textView = new TextView(this);
                textView.setText(promotion.getTitle());
                TableRow.LayoutParams textViewParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                textViewParams.gravity = Gravity.LEFT;
                textViewParams.bottomMargin = 4;
                textView.setLayoutParams(textViewParams);
                tableRow.addView(textView);

                tableLayout.addView(tableRow);
            }
        } catch (JSONException je) {
            Log.e("ERROR", "Threw a JSONException", je);
        }
    }

    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        Log.d("DEBUG", "This is the line reader: " + sb.toString());
        return sb.toString();
    }

    private boolean fileExists(String fileName){
        File file = getBaseContext().getFileStreamPath(fileName);
        return file.exists();
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
            } catch (IOException ioe) {
                Log.e("ERROR", "Threw IOException", ioe);
                return "Unable to retrieve JSON data";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //result is the JSON as string
            FileOutputStream fileOutputStream = null;
            Log.d("DEBUG", "THis is the result that is stored on file: " + result);
            try {
                fileOutputStream = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                fileOutputStream.write(result.getBytes());
                fileOutputStream.close();
            } catch (IOException ioe) {
                Log.e("ERROR", "Threw IOException", ioe);
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException ioe) {
                    Log.e("ERROR", "Threw IOException", ioe);
                }
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