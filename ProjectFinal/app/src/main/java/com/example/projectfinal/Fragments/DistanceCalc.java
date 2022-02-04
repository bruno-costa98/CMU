package com.example.projectfinal.Fragments;

import android.location.Location;
import android.util.Log;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DistanceCalc {
    public DistanceCalc() {
    }
    public final Double getDistance(Location from, Location to) {

        final Double[] d = new Double[1];
        Thread thread = new Thread((Runnable) new Runnable() {
            public final void run() {
                try {

                    URL url = new URL("https://api.mapbox.com/directions/v5/mapbox/walking" + '/' + from.getLongitude()+ ',' + from.getLatitude()
                            + ';' + to.getLongitude() + ',' + to.getLatitude() + "?geometries=geojson&access_token=pk.eyJ1IjoibWFyb3RvMTIzNDUiLCJhIjoiY2t4bmE1dm4wMjZpNDJya2p0dWRrangwbCJ9.hPFUdTzeFL2PHhBhKjvvzg");
                    Log.v("urldirection", url.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    if(conn.getResponseCode() == 200) {
                        BufferedInputStream inp = new BufferedInputStream(conn.getInputStream());
                        JSONObject jsonObject = new JSONObject(IOUtils.toString((InputStream) inp, "UTF-8"));
                        JSONArray array = jsonObject.getJSONArray("routes");
                        JSONObject routes = array.getJSONObject(0);
                        JSONArray legs = routes.getJSONArray("legs");
                        JSONObject steps = legs.getJSONObject(0);
                        d[0] = steps.getDouble("distance");
                    }
                    Log.e("THREAD", String.valueOf(d[0]));
                } catch (JSONException var9) {
                    Log.e("ContentValues", var9.toString());
                } catch (IOException var10) {
                    Log.e("ContentValues", var10.toString());
                }

            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return d[0];
    }
}
