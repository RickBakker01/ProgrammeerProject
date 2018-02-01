package com.example.rick.programmeerproject;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
/**
 * Created by Rick on 23-1-2018.
 */
public class InfoAsyncTask extends AsyncTask<String, Integer, String> {
    private Context context;

    InfoAsyncTask(InfoActivity info) {
        this.context = info.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(context, "Asking brewery for more information", Toast.LENGTH_SHORT).show();
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
            JSONArray Main = new JSONArray(result);
            for (int i = 0; i < Main.length(); i++) {
                JSONObject breweries = Main.getJSONObject(i);
                String status = breweries.getString("status");
                String street = breweries.getString("street");
                String city = breweries.getString("city");
                String phone = breweries.getString("phone");
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
