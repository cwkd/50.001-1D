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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KM on 8/12/2017.
 */

public class GetLocationsData extends AsyncTask<Object, String, List<String>> {

    private List<String> listGooglePlacesData = new ArrayList<>();
    private GoogleMap mMap;
    private List<String> listofUrl = new ArrayList<>();
    private List<String> listofMerchants = new ArrayList<>();
    private List<String> listofPrices = new ArrayList<>();
    private List<String> listofProducts = new ArrayList<>();

    @Override
    protected List<String> doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        listofUrl = (List<String>)objects[1];
        listofMerchants = (List<String>)objects[2];
        listofPrices = (List<String>)objects[3];
        listofProducts = (List<String>)objects[4];

        DownloadURL downloadURL = new DownloadURL();
        try {
            for (String url : listofUrl) {
                String googlePlacesData = downloadURL.readUrl(url);
                listGooglePlacesData.add(googlePlacesData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listGooglePlacesData;
    }

    @Override
    protected void onPostExecute(List<String> listOfS) {
        Log.d("json data", listOfS.toString());
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        try {
            for (int i=0; i<listOfS.size(); i++) {

                jsonObject = new JSONObject(listOfS.get(i));
                jsonArray = jsonObject.getJSONArray("results");
                double lat = jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                double lng = jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                String address = jsonArray.getJSONObject(0).getString("formatted_address");
                Log.d("formatted address", address);
                LatLng latLng = new LatLng(lat, lng);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(listofMerchants.get(i));
                markerOptions.snippet(listofProducts.get(i) + ": " + listofPrices.get(i));
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void showPlace(String s) {

    }
}
