package com.codeencounter.nagarro;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codeencounter.nagarro.Adapters.ParkingSuggestionAdapter;
import com.codeencounter.nagarro.Adapters.ParkingSuggestionList;
import com.codeencounter.nagarro.Config.AppConfig;
import com.codeencounter.nagarro.Config.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePage extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    Button nearbylocation, searchnow;
    FloatingActionButton edit_profile;
    EditText typedlocation;
    ImageView searchimage;
    String search_location;
    LocationManager locationManager;
    List<ParkingSuggestionList> mainParkingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        if (!isNetworkAvailable()) {
            Intent intent = new Intent(HomePage.this, LocationResults.class);
            startActivity(intent);
        }
        getListofLot();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_home_page_fragment_map);
        mapFragment.getMapAsync(this);

        typedlocation = findViewById(R.id.activity_home_page_edittext_searchlocation);
        nearbylocation = findViewById(R.id.activity_home_page_button_searchbylocation);
        searchnow = findViewById(R.id.activity_home_page_btn_search_now);
        edit_profile = findViewById(R.id.activity_home_page_floating_button);
        getLocation();


        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, ProfilePage.class);
                startActivity(intent);
            }
        });

        typedlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchnow.setVisibility(View.VISIBLE);
            }
        });

        searchnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String loc = typedlocation.getText().toString().trim();
                LatLng l = getLocationFromAddress(getApplicationContext(), loc);
                searchnow.setVisibility(View.GONE);

                try {
                    Log.e("home", l.latitude + " " + l.longitude);
                    Intent intent = new Intent(HomePage.this, LocationResults.class);
                    intent.putExtra("lat", String.valueOf(l.latitude));
                    intent.putExtra("lng", String.valueOf(l.longitude));
                    startActivity(intent);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No Location Found", Toast.LENGTH_SHORT).show();
                }

            }
        });

        nearbylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Latitude: " + AppConfig.lat + "\n Longitude: " + AppConfig.lng, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(HomePage.this, LocationResults.class);
                intent.putExtra("lat", String.valueOf(AppConfig.lat));
                intent.putExtra("lng", String.valueOf(AppConfig.lng));
                startActivity(intent);
            }
        });

    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, this);


        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            if (address.size() > 0) {
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
                return p1;
            } else {
                Toast.makeText(getApplicationContext(), "No Location Found", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }


    @Override
    public void onLocationChanged(Location location) {
        AppConfig.lat = location.getLatitude();
        AppConfig.lng = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    List<ParkingSuggestionList> parking;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        parking = new ArrayList<ParkingSuggestionList>();

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);

        parking = new SessionManager(getApplicationContext()).getParkingList();

        if(parking==null){
            parking = new ArrayList<ParkingSuggestionList>();
            AppConfig.savedParkingList = parking;
        }

        for(int i=0;i<parking.size();i++){
            ParkingSuggestionList p = parking.get(i);
            Double lat = Double.valueOf(p.getLat());
            Double lng = Double.valueOf(p.getLng());
            LatLng sydney = new LatLng(lat, lng);
            googleMap.addMarker(new MarkerOptions().position(sydney)
                    .title(i+"-"+p.getName()+"")
                    .snippet(p.getAddress()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(HomePage.this, DetailedParkingLot.class);
                char[] t = marker.getTitle().toCharArray();
                intent.putExtra("pos", Integer.parseInt(String.valueOf(t[0])));
                Log.e("aaa", marker.getId());
                startActivity(intent);

            }
        });



    }



    public void getListofLot(){
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.allplaceslist, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("loclist", "Login Response: " + response.toString());

                mainParkingList = new ArrayList<ParkingSuggestionList>();

                try {
                    JSONArray jArr = new JSONArray(response);
                    for(int i = 0; i<jArr.length();i++){
                        JSONObject jObj = (JSONObject) jArr.get(i);

                        ParkingSuggestionList p = new ParkingSuggestionList();

                        p.setParking_lot_id(jObj.getString("parking_lot_id"));
                        p.setName(jObj.getString("parking_lot_name"));
                        Log.e("AA", p.getName());

                        p.setAddress(jObj.getString("parking_lot_address"));
                        p.setLat(jObj.getString("parking_lot_latitude"));
                        p.setLng(jObj.getString("parking_lot_longitude"));
                        p.setRatings(jObj.getString("ratings"));
                        p.setComments(jObj.getString("comments"));
                        p.setTotal_general(jObj.getString("total_general"));
                        p.setTotal_differently_abled("total_differently_abled");
                        p.setCurrent_general(jObj.getString("current_general"));
                        p.setCurrent_differently_abled(jObj.getString("current_differently_abled"));
                        p.setPrice_per_hour(jObj.getString("price_per_hour"));
                        p.setAvailable(jObj.getString("available"));
                        p.setValet_available(jObj.getString("valet_available"));
                        p.setCctv_available(jObj.getString("cctv_available"));
                        p.setCount(jObj.getString("count"));
                        p.setUser_id(jObj.getString("user_id"));
                        p.setDistance(jObj.getString("distance"));

                        mainParkingList.add(p);
                    }

                    AppConfig.savedParkingList = mainParkingList;
                    Gson gson = new Gson();
                    new SessionManager(getApplicationContext()).setLocations(gson.toJson(mainParkingList));

                    // Check for error node in json
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("reg", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

        };

        // Adding request to request queue
        Volley.newRequestQueue(getApplicationContext()).add(strReq);

    }

}



