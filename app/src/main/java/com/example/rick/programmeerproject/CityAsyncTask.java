package com.example.rick.programmeerproject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
/**
 * This is the async task that retrieves the city the user is in.
 */
class CityAsyncTask extends AsyncTask<String, Integer, String> {
    @SuppressLint("StaticFieldLeak")
    private final Context context;

    CityAsyncTask(MainActivity locality) {
        this.context = locality.getApplicationContext();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            return HttpRequestHelper.downloadFromServer("loccity", params);
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
            JSONArray city_array = new JSONArray(result);
            for (int i = 0; i < city_array.length(); i++) {
                JSONObject breweries = city_array.getJSONObject(i);
                String resultString = breweries.getString("id");
                CoordinatesAsyncTask asyncTask = new CoordinatesAsyncTask(this);
                asyncTask.execute(resultString);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Context getApplicationContext() {
        return context;
    }
}
