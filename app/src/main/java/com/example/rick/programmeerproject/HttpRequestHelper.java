package com.example.rick.programmeerproject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * Created by Rick on 17-1-2018.
 */
class HttpRequestHelper {
    static synchronized String downloadFromServer(String... params) throws MalformedURLException {
        StringBuilder result = new StringBuilder();
        String city = params[0];
        URL url = new URL("http://beermapping" + "" + "" +
                ".com/webservice/loccity/ebd4907cef61e7544b290b02c3b0d28b/" + city + "&s=json");
        HttpURLConnection connect;
        try {
            connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("GET");
            Integer responseCode = connect.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(connect
                        .getInputStream()));
                String line;
                while ((line = bReader.readLine()) != null) {
                    result.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    static synchronized String downloadFromServer2(String... params) throws MalformedURLException {
        String result2 = "";
        String id = params[0];
        URL url = new URL("http://beermapping" + "" + "" +
                ".com/webservice/locmap/ebd4907cef61e7544b290b02c3b0d28b/" + id + "&s=json");
        HttpURLConnection connect;
        try {
            connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("GET");
            Integer responseCode = connect.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(connect
                        .getInputStream()));
                String line;
                while ((line = bReader.readLine()) != null) {
                    result2 += line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result2;
    }

    static synchronized String downloadFromServer3(String... params) throws MalformedURLException {
        String result3 = "";
        String id = params[0];
        URL url = new URL("http://beermapping" + "" + "" +
                ".com/webservice/locquery/ebd4907cef61e7544b290b02c3b0d28b/" + id + "&s=json");
        HttpURLConnection connect;
        try {
            connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("GET");
            Integer responseCode = connect.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(connect
                        .getInputStream()));
                String line;
                while ((line = bReader.readLine()) != null) {
                    result3 += line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result3;
    }

    static synchronized String downloadFromServer4(String... params) throws MalformedURLException {
        String result4 = "";
        String id = params[0];
        URL url = new URL("http://beermapping" + "" + "" +
                ".com/webservice/locimage/ebd4907cef61e7544b290b02c3b0d28b/" + id + "&s=json");
        HttpURLConnection connect;
        try {
            connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("GET");
            Integer responseCode = connect.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(connect
                        .getInputStream()));
                String line;
                while ((line = bReader.readLine()) != null) {
                    result4 += line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result4;
    }
}
