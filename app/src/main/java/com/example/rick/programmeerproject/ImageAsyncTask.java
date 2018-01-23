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
/**
 * Created by Rick on 23-1-2018.
 */
public class ImageAsyncTask extends AsyncTask<String, Integer, String> {
    String imageurl;
    String caption;
    private Context context;

    public ImageAsyncTask(InfoActivity image) {
        this.context = image.getApplicationContext();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            return HttpRequestHelper.downloadFromServer4(params);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result4) {
        super.onPostExecute(result4);
        try {
            //Get the results
            JSONArray Main = new JSONArray(result4);
            for (int i = 0; i < Main.length(); i++) {
                JSONObject breweries = Main.getJSONObject(i);
                imageurl = breweries.getString("imageurl");
                caption = breweries.getString("caption");
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
