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
 * This async task gets the coordinates from the result of the city async task
 */
public class CoordinatesAsyncTask extends AsyncTask<String, Integer, String> {
    String id;
    private Context context;

    CoordinatesAsyncTask(CityAsyncTask resultString) {
        this.context = resultString.getApplicationContext();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            //Get the ID from the param. Arrays.toString() includes [ and ]. ReplaceAll with
            // regex to get rid of the brackets.
            id = (Arrays.toString(params).replaceAll("\\[|]|,|\\s", ""));
            return HttpRequestHelper.downloadFromServer("locmap", params);
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
            JSONArray coordinates_array = new JSONArray(result);
            for (int i = 0; i < coordinates_array.length(); i++) {
                JSONObject breweries = coordinates_array.getJSONObject(i);
                if (!Objects.equals(breweries.getString("name"), "null")) {
                    String names = breweries.getString("name").replaceAll("[.]", "");
                    String lat = breweries.getString("lat");
                    String lon = breweries.getString("lng");
                    //Use an intent with a broadcast manager to send the data back to MainActivity
                    Intent intent = new Intent("breweries");
                    intent.putExtra("names", names);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lon", lon);
                    intent.putExtra("id", id);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
