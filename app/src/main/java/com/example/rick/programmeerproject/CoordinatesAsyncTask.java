package com.example.rick.programmeerproject;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Objects;
/**
 * Created by Rick on 18-1-2018.
 */
public class CoordinatesAsyncTask extends AsyncTask<String, Integer, String> {
    String id;
    private Context context;

    CoordinatesAsyncTask(CityAsyncTask resultaat) {
        this.context = resultaat.getApplicationContext();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            //Get the ID from the param. Arrays.toString() includes [ and ]. ReplaceAll with
            // regex to get rid of the brackets.
            id = (Arrays.toString(params).replaceAll("\\[|]|,|\\s", ""));
            return HttpRequestHelper.downloadFromServer2(params);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result2) {
        super.onPostExecute(result2);
        String names;
        String lat;
        String lon;
        //        String status;
        try {
            //Get the results
            JSONArray Main = new JSONArray(result2);
            for (int i = 0; i < Main.length(); i++) {
                JSONObject breweries = Main.getJSONObject(i);
                if (!Objects.equals(breweries.getString("name"), "null")) {
                    names = breweries.getString("name");
                    lat = breweries.getString("lat");
                    lon = breweries.getString("lng");
                    //Use an intent with a broadcast manager to send the data back to MainActivity
                    Intent intent = new Intent("breweries");
                    intent.putExtra("names", names);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lon", lon);
                    //                    intent.putExtra("status", status);
                    intent.putExtra("id", id);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
