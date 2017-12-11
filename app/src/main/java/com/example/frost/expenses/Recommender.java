package com.example.frost.expenses;

import android.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import linanalysistools.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Recommender.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Recommender#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Recommender extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    // Fields for map
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
    private EditText textfield_location;

    private DatabaseReference mDatabase;
    Map<String, Entry> filteredMap;

    private SupportMapFragment mMapFragment;


    public Recommender() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Recommender newInstance() {
        return new Recommender();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//
//        mapFragment.getMapAsync(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recommender, container, false);
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);


        textfield_location =  v.findViewById(R.id.TF_location);
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://moneywise-a5fef.firebaseio.com/");
        markerClicked = false;
        filteredMap = new HashMap<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();

        }

        GetLocationsData getLocationsData = new GetLocationsData();

        Button search = (Button) v.findViewById(R.id.B_search);
        Button drive = (Button) v.findViewById(R.id.B_drive);
        Button publicTransport = (Button) v.findViewById(R.id.B_publicTransport);
        Button foot = (Button) v.findViewById(R.id.B_foot);



        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mMap.clear();
                String search = textfield_location.getText().toString();
                getAllEntries();
                Map<String,List<String>> mapOfDetails = queryUsingSearch(search);
                getNearbyPlaces(mapOfDetails);


                Toast.makeText(Recommender.this.getActivity(), "Showing Nearby " + search, Toast.LENGTH_SHORT).show();
            }
        });

        drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object dataTransfer[] = new Object[3];
                GetDirectionsData getDirectionsData = new GetDirectionsData();
                String url = "";
                if (markerClicked) {
                    dataTransfer = new Object[3];
                    mode_of_transport = "driving";

                    url = getDirectionsUrl();
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;
                    dataTransfer[2] = new LatLng(end_latitude, end_longitude);
                    getDirectionsData.execute(dataTransfer);
                }
            }
        });

        publicTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object dataTransfer[];
                GetDirectionsData getDirectionsData = new GetDirectionsData();
                String url = "";
                if (markerClicked) {
                    dataTransfer = new Object[3];
                    mode_of_transport = "transit";

                    url = getDirectionsUrl();
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;
                    dataTransfer[2] = new LatLng(end_latitude, end_longitude);
                    getDirectionsData.execute(dataTransfer);
                }
            }
        });

        foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object dataTransfer[];
                GetDirectionsData getDirectionsData = new GetDirectionsData();
                String url = "";
                if (markerClicked) {
                    dataTransfer = new Object[3];
                    mode_of_transport = "walking";

                    url = getDirectionsUrl();
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;
                    dataTransfer[2] = new LatLng(end_latitude, end_longitude);
                    getDirectionsData.execute(dataTransfer);
                }
            }
        });


        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }





//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//        textfield_location =  findViewById(R.id.TF_location);
//        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://moneywise-a5fef.firebaseio.com/");
//        markerClicked = false;
//        filteredMap = new HashMap<>();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//        {
//            checkLocationPermission();
//
//        }
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=  PackageManager.PERMISSION_GRANTED)
                    {
                        if(client == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this.getActivity(),"Permission Denied" , Toast.LENGTH_LONG).show();
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

        if (ContextCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);
    }


    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this.getActivity()).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
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


                Toast.makeText(Recommender.this.getActivity(), "Showing Nearby " + search, Toast.LENGTH_SHORT).show();
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

            // TODO: Get instance budget, if price > budget don't add to list
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


        if(ContextCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }


    public boolean checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this.getActivity(),new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this.getActivity(),new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
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
//                for (String eachKey:filteredMap.keySet()){
//                    Log.i("final result",filteredMap.get(eachKey).Merchant);
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
