package com.codeencounter.parkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codeencounter.parkit.Adapters.ParkingSuggestionList;
import com.codeencounter.parkit.Config.AppConfig;
import com.codeencounter.parkit.Config.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailedParkingLot extends AppCompatActivity {


    TextView name,cost,totalavailability,currentavailability,abledavailability,ratings,address,update_current_status,showonmaps;
    Button booknow;
    final String server="+918976446718";
    SessionManager sessionManager;
    int pos;
    ParkingSuggestionList pl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_parking_lot);
        sessionManager = new SessionManager(getApplicationContext());



        Bundle extras = getIntent().getExtras();
        pos  = extras.getInt("pos");


        pl = AppConfig.savedParkingList.get(pos);

        name = findViewById(R.id.activity_detailed_parking_lot_name);
        showonmaps = findViewById(R.id.activity_detailed_parking_lot_show_on_maps);
        ratings = findViewById(R.id.activity_detailed_parking_lot_ratings_value);
        totalavailability = findViewById(R.id.activity_detailed_parking_lot_total_availability_value);
        address = findViewById(R.id.activity_detailed_parking_lot_address);
        currentavailability = findViewById(R.id.activity_detailed_parking_lot_current_availability_value);
        abledavailability = findViewById(R.id.activity_detailed_parking_lot_differently_abled_value);
        cost = findViewById(R.id.activity_detailed_parking_lot_cost_value);
        update_current_status = findViewById(R.id.activity_detailed_parking_lot_update_parking_status);

        showonmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:"+pl.getLat()+","+pl.getLng());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        if(!isNetworkAvailable()){
            update_current_status.setVisibility(View.VISIBLE);


            SmsReciever.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String messageText, String sender) {
                    Log.e("mssg", messageText+sender);
                    if(sender.equals(server)){
                        try {
                            JSONObject jObj = new JSONObject(messageText);
                            int status = jObj.getInt("status");
                            if (status==11) {
                                updateData(jObj);
                            }else
                            if (status==1) {
                                book(jObj);
                            } else {
                                Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(),"Message: "+messageText+" Sender: "+sender,Toast.LENGTH_LONG).show();
                    }
                }
            });

        }else{
            name.setText(pl.getName());
            ratings.setText(pl.getRatings());
            totalavailability.setText(pl.getTotal_general());
            address.setText(pl.getAddress());
            currentavailability.setText(pl.getCurrent_general());
            abledavailability.setText(pl.getCurrent_differently_abled());
            cost.setText(pl.getPrice_per_hour());

        }

        update_current_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager smgr = SmsManager.getDefault();
                String msg = "{\"code\":\"9\",\"pid\":\""+pl.getParking_lot_id()+"\"}";
                Log.e("offline",msg);
                smgr.sendTextMessage(server,null,msg,null,null);

            }
        });

        booknow =findViewById(R.id.activity_detailed_parking_lot_btn_booknow);
        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!isNetworkAvailable()){
                    SmsManager smgr = SmsManager.getDefault();
                    String msg = "{\"code\":\"2\",\"pid\":\""+pl.getParking_lot_id()+"\",\"uid\":\""+sessionManager.getUID()+"\"}";
                    Log.e("offline",msg);
                    smgr.sendTextMessage(server,null,msg,null,null);

                }else {

                    StringRequest strReq = new StringRequest(Request.Method.POST,
                            AppConfig.booking, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d("reg", "Login Response: " + response.toString());
                            JSONObject jObj = null;
                            try {
                                jObj = new JSONObject(response);
                                int status = jObj.getInt("status");
                                if (status == 1) {

                                    String booking = jObj.getString("bookingid");
                                    // Launch main activity
                                    Intent intent = new Intent(DetailedParkingLot.this, BookedStatusPage.class);
                                    intent.putExtra("pos", pos);
                                    intent.putExtra("bookid", booking);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Please Try Again", Toast.LENGTH_LONG).show();
                                }
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
                            params.put("uid", new SessionManager(getApplicationContext()).getUID());
                            params.put("pid", pl.getParking_lot_id());


                            return params;
                        }

                    };

                    // Adding request to request queue
                    Volley.newRequestQueue(DetailedParkingLot.this).add(strReq);

                }
            }
        });



    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void book(JSONObject jObj){

        String booking = null;
        try {
            booking = jObj.getString("bookingid");
            Intent intent = new Intent(DetailedParkingLot.this, BookedStatusPage.class);
            intent.putExtra("pos", pos);
            intent.putExtra("bookid", booking);
            startActivity(intent);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Launch main activity


    }

    private void updateData(JSONObject jObj){
        try {
            ParkingSuggestionList p = new ParkingSuggestionList();

            p.setTotal_general(jObj.getString("tg"));
            p.setCurrent_general(jObj.getString("cg"));
            p.setCurrent_differently_abled(jObj.getString("cd"));
            p.setPrice_per_hour(jObj.getString("pr"));


            totalavailability.setText(p.getTotal_general());
            currentavailability.setText(p.getCurrent_general());
            abledavailability.setText(p.getCurrent_differently_abled());
            cost.setText(p.getPrice_per_hour());




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
