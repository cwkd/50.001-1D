package com.example.km.spendingrec;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener  {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker currentLocationmMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    private double latitude,longitude;
    private double end_latitude, end_longitude;
    private String mode_of_transport;
    private List<Map<String,String>> listOfMaps;
    private boolean markerClicked;
    EditText textfield_location;

    private DatabaseReference mDatabase;
    Map<String, Entry> filteredMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        textfield_location =  findViewById(R.id.TF_location);
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://moneywise-a5fef.firebaseio.com/");
        markerClicked = false;
        filteredMap = new HashMap<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=  PackageManager.PERMISSION_GRANTED)
                    {
                        if(client == null)
                        {
                            bulidGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this,"Permission Denied" , Toast.LENGTH_LONG).show();
                }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            bulidGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);
    }


    protected synchronized void bulidGoogleApiClient() {
        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();

    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lastlocation = location;
        if(currentLocationmMarker != null)
        {
            currentLocationmMarker.remove();

        }
        Log.d("lat = ",""+latitude);
        LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocationmMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if(client != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }
    }

    public void onClick(View v)
    {
        Object dataTransfer[] = new Object[3];
        GetDirectionsData getDirectionsData = new GetDirectionsData();
        GetLocationsData getLocationsData = new GetLocationsData();
        String url = "";

        switch(v.getId()) {

            case R.id.B_search:
                mMap.clear();
                String search = textfield_location.getText().toString();
                getAllEntries();
                Map<String,List<String>> mapOfDetails = queryUsingSearch(search);
                getNearbyPlaces(mapOfDetails);

                //TODO: Indicate under the budget
                Toast.makeText(MapsActivity.this, "Showing Nearby " + search, Toast.LENGTH_SHORT).show();
                break;

            case R.id.B_drive:
                if (markerClicked) {
                    dataTransfer = new Object[3];
                    mode_of_transport = "driving";

                    url = getDirectionsUrl();
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;
                    dataTransfer[2] = new LatLng(end_latitude, end_longitude);
                    getDirectionsData.execute(dataTransfer);
                }
                break;

            case R.id.B_publicTransport:
                if (markerClicked) {
                    dataTransfer = new Object[3];
                    mode_of_transport = "transit";

                    url = getDirectionsUrl();
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;
                    dataTransfer[2] = new LatLng(end_latitude, end_longitude);
                    getDirectionsData.execute(dataTransfer);
                }
                break;

            case R.id.B_foot:
                if (markerClicked) {
                    dataTransfer = new Object[3];
                    mode_of_transport = "walking";

                    url = getDirectionsUrl();
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;
                    dataTransfer[2] = new LatLng(end_latitude, end_longitude);
                    getDirectionsData.execute(dataTransfer);
                }
                break;
        }
    }


    public Map<String, List<String>> queryUsingSearch(String search) {
        listOfMaps = new ArrayList<>();
        List<String> listOfAddresses = new ArrayList<>();
        List<String> listOfMerchants = new ArrayList<>();
        List<String> listOfPrices = new ArrayList<>();
        List<String> listOfProducts = new ArrayList<>();

        for (String product : filteredMap.keySet()) {

//            if (filteredMap.get(product).Price <= budget)
            Map<String, String> filteredResult = new HashMap<>();
            filteredResult.put("Address", filteredMap.get(product).Address);
            filteredResult.put("Price", "$"+String.valueOf(filteredMap.get(product).Price));
            filteredResult.put("Product", product);
            filteredResult.put("Merchant",filteredMap.get(product).Merchant);
            listOfMaps.add(filteredResult);
        }

        for (int i=0; i<listOfMaps.size(); i++) {
            HashMap<String,String> place = (HashMap<String,String>)listOfMaps.get(i);
            String address = place.get("Address");
            String merchant = place.get("Merchant");
            String price = place.get("Price");
            String product = place.get("Product");
            listOfAddresses.add(address);
            listOfMerchants.add(merchant);
            listOfPrices.add(price);
            listOfProducts.add(product);
        }
        Map<String, List<String>> mapOfDetails = new HashMap<>();
        mapOfDetails.put("ListOfAddresses", listOfAddresses);
        mapOfDetails.put("ListOfMerchants", listOfMerchants);
        mapOfDetails.put("ListOfPrices", listOfPrices);
        mapOfDetails.put("ListOfProducts", listOfProducts);

        return mapOfDetails;
    }


    public void getNearbyPlaces(Map<String, List<String>> map) {
        GetLocationsData getLocationsData = new GetLocationsData();
        Object[] dataTransfer = new Object[5];
        List<String> listOfUrls = new ArrayList<>();
        List<String> listOfAddresses = map.get("ListOfAddresses");
        List<String> listOfMerchants = map.get("ListOfMerchants");
        List<String> listOfPrices = map.get("ListOfPrices");
        List<String> listOfProducts = map.get("ListOfProducts");

        for (String address : listOfAddresses) {
            address = address.replaceAll("\\s+", "+");
            Log.d("address", address);
            String url = getGeolocateUrl(address);
            listOfUrls.add(url);
        }

        dataTransfer[0] = mMap;
        dataTransfer[1] = listOfUrls;
        dataTransfer[2] = listOfMerchants;
        dataTransfer[3] = listOfPrices;
        dataTransfer[4] = listOfProducts;
        getLocationsData.execute(dataTransfer);

    }

    public String getGeolocateUrl(String address) {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json?");
        googleDirectionsUrl.append("address="+address);
        googleDirectionsUrl.append("&key="+"AIzaSyCBb77wR1DPrTTMOeJ_Lcbb7xe0DNlpjno");

        return googleDirectionsUrl.toString();
    }

    public String getDirectionsUrl() {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+latitude+","+longitude);
        googleDirectionsUrl.append("&destination="+end_latitude+","+end_longitude);
        googleDirectionsUrl.append("&mode="+mode_of_transport);
        googleDirectionsUrl.append("&key="+"AIzaSyDAiNLdrKkx3T2oX64HMkoELMz5-0zQloU");

        return googleDirectionsUrl.toString();
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }


    public boolean checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            return false;

        } else
            return true;
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        end_latitude = marker.getPosition().latitude;
        end_longitude = marker.getPosition().longitude;
        markerClicked = true;
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        end_latitude = marker.getPosition().latitude;
        end_longitude = marker.getPosition().longitude;
    }


    //this is the method to get all entries from the database
    public void getAllEntries(){

        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //Entry newentry = dataSnapshot.getValue(Entry.class);
                //System.out.println(dataSnapshot.getKey()+" is located at "+newentry.Address);

                //filteredList = new ArrayList();
                String currentQuery = textfield_location.getText().toString();
                Map<String, Integer> tempDistHash = new HashMap<>();

                //to store the distances between words in an array
                //List<Map<String, Integer>> tempDistanceList = new ArrayList();
                //Log.i("yilong",dataSnapshot.getValue(Entry.class).toString());
//                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
//                    Log.i("yilong",snapshot.getValue().toString());
//                }

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Log.i("yilong",String.valueOf(snapshot.getValue(Entry.class)));
                    Entry eachEntry = snapshot.getValue(Entry.class);
                    Integer similarity = new LevenshteinDistance().apply(currentQuery, snapshot.getKey());
                    if (filteredMap.size()<20){


                        filteredMap.put(snapshot.getKey(), eachEntry);
                        tempDistHash.put(snapshot.getKey(), similarity);

                    }
                    else{
                        int maxDistance = 0;
                        String theProduct = "";
                        for (String eachKey:filteredMap.keySet()){
                            int currentDiff = tempDistHash.get(eachKey);
                            if (currentDiff>maxDistance){
                                maxDistance = currentDiff;
                                theProduct = eachKey;
                            }
                        }
                        if (similarity<maxDistance){
                            filteredMap.remove(theProduct);
                            filteredMap.put(snapshot.getKey(), eachEntry);
                        }

                    }


                }
                //filteredMap now contains the filtered result from the database
                //logcat will print out the values of each entry here..
                for (String eachKey:filteredMap.keySet()){
                    Log.i("final result",filteredMap.get(eachKey).Merchant);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}