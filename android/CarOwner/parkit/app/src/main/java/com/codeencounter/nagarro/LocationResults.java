package com.codeencounter.nagarro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationResults extends AppCompatActivity {

    List<ParkingSuggestionList> mainParkingList;
    SessionManager sessionManager;

     ListView recyclerView;
     ParkingSuggestionAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_results);
        sessionManager = new SessionManager(getApplicationContext());


        recyclerView = findViewById(R.id.activity_location_results_listView);

        sessionManager = new SessionManager(getApplicationContext());

        mainParkingList = new ArrayList<ParkingSuggestionList>();
        adapter=new ParkingSuggestionAdapter(mainParkingList, LocationResults.this);
        recyclerView.setAdapter(adapter);

        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(isNetworkAvailable()) {
                    Intent intent = new Intent(LocationResults.this, DetailedParkingLot.class);
                    intent.putExtra("pos", i);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(LocationResults.this, DetailedParkingLot.class);
                    intent.putExtra("pos", i);
                    startActivity(intent);

                }
            }
        });


        if(isNetworkAvailable()){
            Bundle extras = getIntent().getExtras();
            String lat  = extras.getString("lat");
            String lng  = extras.getString("lng");
            Log.e("locres", lat+" - "+lng);
            getListofLot(lat, lng);

        }else{

            mainParkingList = new ArrayList<ParkingSuggestionList>();

            Log.e("obj", sessionManager.getLocations());
            mainParkingList = sessionManager.getParkingList();
            Log.e("obj", mainParkingList.get(0).getName());
            AppConfig.savedParkingList = mainParkingList;
            ParkingSuggestionAdapter mAdapter = new ParkingSuggestionAdapter(mainParkingList,getApplicationContext());
            recyclerView.setAdapter(mAdapter);

        }

    }



    public void getListofLot(final String lat, final String lng){
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.placeslist, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("loclist", "Login Response: " + response.toString());

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
                    sessionManager.setLocations(gson.toJson(mainParkingList));
                    Log.e("obj", sessionManager.getLocations());
                    ParkingSuggestionAdapter mAdapter = new ParkingSuggestionAdapter(mainParkingList,getApplicationContext());
                    recyclerView.setAdapter(mAdapter);

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
                params.put("lat", lat);
                params.put("lng", lng);

                return params;
            }

        };

        // Adding request to request queue
        Volley.newRequestQueue(getApplicationContext()).add(strReq);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
