package com.example.rick.programmeerproject;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * Created by Rick on 18-1-2018.
 */
public class CoordinatesAsyncTask extends AsyncTask<String, Integer, String> {
    private Context context;

    ArrayList<String> ids = new ArrayList<>();

    CoordinatesAsyncTask(CityAsyncTask resultaat) {
        this.context = resultaat.getApplicationContext();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            ids.add(Arrays.toString(params));
            return HttpRequestHelper.downloadFromServer2(params);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result2) {
        super.onPostExecute(result2);

        Log.d("resultaat2", result2);
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> lat = new ArrayList<>();
        ArrayList<String> lon = new ArrayList<>();

        try {
            //Get the results
            JSONArray Main = new JSONArray(result2);
            for (int i = 0; i < Main.length(); i++) {
                JSONObject breweries = Main.getJSONObject(i);
                names.add(breweries.getString("name"));
                lat.add(breweries.getString("lat"));
                lon.add(breweries.getString("lng"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent("breweries").putExtra("name", names);
        intent.putExtra("lat", lat);
        intent.putExtra("lon", lon);
        intent.putExtra("ids", ids);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
