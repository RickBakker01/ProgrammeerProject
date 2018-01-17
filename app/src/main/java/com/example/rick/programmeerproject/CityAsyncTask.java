package com.example.rick.programmeerproject;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Rick on 17-1-2018.
 */
class CityAsyncTask extends AsyncTask<String, Integer, String> {
    private Context context;

    //
    CityAsyncTask(MainActivity locality) {
        this.context = locality.getApplicationContext();
    }

    //    @Override
    //    protected void onPreExecute() {
    //        Toast.makeText(context, "testing", Toast.LENGTH_SHORT).show();
    //    }

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
        List adresses = new ArrayList();
        List names = new ArrayList();
        try {
            //Get the results
            JSONArray Main = new JSONArray(result);
            for (int i = 0; i < Main.length(); i++) {
                JSONObject breweries = Main.getJSONObject(i);
                adresses.add(breweries.getString("street"));
                names.add(breweries.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("street", adresses.toString());
        Log.d("street", names.toString());
    }
}
