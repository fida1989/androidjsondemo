package com.hungrydroid.jsonapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.list);
        if (Connectivity.isNetworkAvailable(MainActivity.this)) {
            loadData();
        } else {
            Toast.makeText(MainActivity.this, "Network not available!", Toast.LENGTH_SHORT).show();

        }
    }

    public void loadData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://10.0.2.2/json/getjson.php", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Loading JSON...");
                pDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray testV = new JSONArray(new String(responseBody));
                    String[] values = new String[testV.length()];


                    for (int i = 0; i < testV.length(); i++) {
                        JSONObject c = testV.getJSONObject(i);
                        values[i] = c.getString("id") + ". " + c.getString("name") + " - " + c.getString("age");
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_list_item_1, values);
                    lv.setAdapter(adapter);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    lv.setAdapter(null);
                }
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                lv.setAdapter(null);
            }


            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }


        });
    }
}
