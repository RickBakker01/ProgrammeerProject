package com.example.rick.programmeerproject;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
/**
 * This is the async task that gets the info from a brewery id
 */
public class InfoAsyncTask extends AsyncTask<String, Integer, String> {
    private Context context;

    InfoAsyncTask(InfoActivity info) {
        this.context = info.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(context, R.string.asking, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            return HttpRequestHelper.downloadFromServer("locquery", params);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            //Get the results
            JSONArray info_array = new JSONArray(result);
            for (int i = 0; i < info_array.length(); i++) {
                JSONObject breweries = info_array.getJSONObject(i);
                String status = breweries.getString("status");
                String street = breweries.getString("street");
                String city = breweries.getString("city");
                String phone = breweries.getString("phone");
                //Use an intent with a broadcast manager to send the data back to InfoActivity
                Intent intent = new Intent("info");
                intent.putExtra("status", status);
                intent.putExtra("street", street);
                intent.putExtra("city", city);
                intent.putExtra("phone", phone);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
