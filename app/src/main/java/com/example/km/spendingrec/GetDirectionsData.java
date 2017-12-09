package com.example.km.spendingrec;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KM on 6/12/2017.
 */
public class GetDirectionsData extends AsyncTask<Object , String, String> {

    GoogleMap mMap;
    String url;
    String googleDirectionsData;
    String duration,distance,end_address;
    LatLng latLng;
    Map<String,String> directionsList = new HashMap<>();


    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        latLng = (LatLng)objects[2];

        DownloadURL downloadURL = new DownloadURL();
        try {
            googleDirectionsData = downloadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String s) {
        mMap.clear();
        DataParser parser = new DataParser();
        String[] directionsPolyList = parser.parseDirectionsPolyline(s);
        directionsList = parser.parseDirections(s);
        List<Polyline> singlePolyline = new ArrayList<>();


        int count = directionsPolyList.length;
        for (int i=0; i<count; i++) {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.RED);
            options.width(5);
            options.addAll(PolyUtil.decode(directionsPolyList[i]));
            singlePolyline.add(mMap.addPolyline(options));
        }

        duration = directionsList.get("duration");
        distance = directionsList.get("distance");
        end_address = directionsList.get("end_address");

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(end_address);
        markerOptions.snippet("Duration = " + duration + ", Distance = " + distance);

        Marker destination = mMap.addMarker(markerOptions);
        destination.showInfoWindow();

    }

}
