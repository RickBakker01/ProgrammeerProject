package com.example.rick.programmeerproject;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
/**
 * Created by Rick on 17-1-2018.
 * test comment git
 */
public class CityAsyncTask extends AsyncTask<String, Integer, String> {
    private Context context;

    CityAsyncTask(MainActivity locality) {
        this.context = locality.getApplicationContext();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            return HttpRequestHelper.downloadFromServer(params);
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
                String resultaat = breweries.getString("id");
                CoordinatesAsyncTask asyncTask = new CoordinatesAsyncTask(this);
                asyncTask.execute(resultaat);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Context getApplicationContext() {
        return context;
    }
}
