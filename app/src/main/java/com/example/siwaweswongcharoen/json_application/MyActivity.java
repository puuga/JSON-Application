package com.example.siwaweswongcharoen.json_application;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MyActivity extends Activity {

    static final String TAG = "json_application";

    private AQuery aQuery;
    private ListView listView;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        bindWidget();

        aQuery = new AQuery(this);

        asyncJson();
    }

    private void bindWidget() {
        listView = (ListView) findViewById(R.id.listView);
        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(), "Position :" + position + "  ListItem : " + itemValue, Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_reload) {
            asyncJson();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void asyncJson() {
        Log.d(TAG, "asyncJson");
        String url = "http://m.sanook.com/feed/gallery/news/?format=json&limit=21";
        aQuery.ajax(url, JSONObject.class, this, "jsonCallback");
    }

    public void jsonCallback(String url, JSONObject json, AjaxStatus status) {
        if (json != null) {
            try {
                Log.d(TAG, "try from: " + url);
                Log.d(TAG, "status: " + status);
                jsonArray = json.getJSONArray("item");
                Log.d(TAG, "get data success, count:" + jsonArray.length());
                displayOnList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void displayOnList() {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                arrayList.add(jsonObj.getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Define a new Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, arrayList);


        // Assign adapter to ListView
        listView.setAdapter(adapter);
    }

}
