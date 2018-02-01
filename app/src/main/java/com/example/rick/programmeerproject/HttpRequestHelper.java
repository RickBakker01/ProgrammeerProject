package com.example.rick.programmeerproject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * This is the helper for the different async tasks
 */
class HttpRequestHelper {
    static synchronized String downloadFromServer(String query, String... params) throws
            MalformedURLException {

        StringBuilder result = new StringBuilder();
        String city = params[0];
        URL url = new URL("http://beermapping.com/webservice/" + query +
                "/ebd4907cef61e7544b290b02c3b0d28b/" + city + "&s=json");

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
}