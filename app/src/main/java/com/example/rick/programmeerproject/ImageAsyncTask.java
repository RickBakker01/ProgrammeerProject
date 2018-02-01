package com.example.rick.programmeerproject;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
/**
 * Created by Rick on 23-1-2018.
 */
public class ImageAsyncTask extends AsyncTask<String, Integer, String> {
    private Context context;

    ImageAsyncTask(InfoActivity image) {
        this.context = image.getApplicationContext();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            return HttpRequestHelper.downloadFromServer("locimage", params);
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
                String imageurl = breweries.getString("imageurl");
                String caption = breweries.getString("caption");
                Intent intent = new Intent("image");
                intent.putExtra("imageurl", imageurl);
                intent.putExtra("caption", caption);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
