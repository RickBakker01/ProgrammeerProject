package com.example.rick.programmeerproject;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
/**
 * Created by Rick on 17-1-2018.
 * test comment git
 */
public class CityAsyncTask extends AsyncTask<String, Integer, String> {
    private String resultaat;
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

        Log.d("resultaat", result);
        //        ArrayList adresses = new ArrayList();
        //        ArrayList names = new ArrayList();
        try {
            //Get the results
            JSONArray Main = new JSONArray(result);
            for (int i = 0; i < Main.length(); i++) {
                JSONObject breweries = Main.getJSONObject(i);
                //                adresses.add(breweries.getString("street"));
                //                names.add(breweries.getString("id"));
                resultaat = breweries.getString("id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CoordinatesAsyncTask asyncTask = new CoordinatesAsyncTask(this);
        asyncTask.execute(resultaat);
    }

    public Context getApplicationContext() {
        return context;
    }
}
