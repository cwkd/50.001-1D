package com.example.km.spendingrec;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by KM on 8/12/2017.
 */

public class GetLocationsData extends AsyncTask<Object, String, String> {

    private String googlePlacesData;
    private GoogleMap mMap;
    String url;

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];

        DownloadURL downloadURL = new DownloadURL();
        try {
            googlePlacesData = downloadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("geolocate data", googlePlacesData);

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("json data", s);
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(s);
            jsonArray = jsonObject.getJSONArray("results");
            double lat = Double.parseDouble(jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat"));
            double lng = Double.parseDouble(jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng"));
            String address = jsonArray.getJSONObject(0).getString("formatted_address");
            LatLng latLng = new LatLng(lat,lng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(address);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void showPlace(String s) {

    }
}
