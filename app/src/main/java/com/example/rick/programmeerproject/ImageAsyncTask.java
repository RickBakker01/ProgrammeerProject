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
 * This is the async task that gets the image and caption from a brewery id
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
            JSONArray image_array = new JSONArray(result);
            for (int i = 0; i < image_array.length(); i++) {
                JSONObject breweries = image_array.getJSONObject(i);
                String imageurl = breweries.getString("imageurl");
                String caption = breweries.getString("caption");
                //Use an intent with a broadcast manager to send the data back to InfoActivity
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
