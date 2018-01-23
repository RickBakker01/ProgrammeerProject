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
    String status;
    String street;
    String city;
    String phone;
    private Context context;

    public InfoAsyncTask(InfoActivity info) {
        this.context = info.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(context, "Asking brewery for more information", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            return HttpRequestHelper.downloadFromServer3(params);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result3) {
        super.onPostExecute(result3);
        Log.d("testinfo", result3);
        try {
            //Get the results
            JSONArray Main = new JSONArray(result3);
            for (int i = 0; i < Main.length(); i++) {
                JSONObject breweries = Main.getJSONObject(i);
                status = breweries.getString("status");
                street = breweries.getString("street");
                city = breweries.getString("city");
                phone = breweries.getString("phone");
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
