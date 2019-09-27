package com.codeencounter.nagarro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codeencounter.nagarro.Adapters.BookingAdapter;
import com.codeencounter.nagarro.Adapters.BookingHistoryRecords;
import com.codeencounter.nagarro.Adapters.ParkingSuggestionAdapter;
import com.codeencounter.nagarro.Adapters.ParkingSuggestionList;
import com.codeencounter.nagarro.Config.AppConfig;
import com.codeencounter.nagarro.Config.SessionManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingHistory extends AppCompatActivity {

    ListView recyclerView;

    List<BookingHistoryRecords> bookingHistoryRecordsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        recyclerView = findViewById(R.id.activity_bookinghistory_results_listView);


    getBHistory();


    }




    public void getBHistory(){
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.bookinghiatory+"?uid="+new SessionManager(getApplicationContext()).getUID().toString(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("loclist", "Login Response: " + response.toString()+AppConfig.placeslist+"?uid="+new SessionManager(getApplicationContext()).getUID().toString());

                bookingHistoryRecordsList = new ArrayList<BookingHistoryRecords>();

                try {
                    JSONArray jArr = new JSONArray(response);
                    for(int i = 0; i<jArr.length();i++){
                        JSONObject jObj = (JSONObject) jArr.get(i);

                        BookingHistoryRecords p = new BookingHistoryRecords();
                        p.setCost(jObj.getString("cost"));
                        p.setCheckin(jObj.getString("check_in_time"));
                        p.setCheckout(jObj.getString("check_out_time"));
                        p.setParkinglotid(jObj.getString("parking_lot_id"));

                        bookingHistoryRecordsList.add(p);
                    }

                    BookingAdapter mAdapter = new BookingAdapter(bookingHistoryRecordsList,getApplicationContext());
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
                params.put("uid", new SessionManager(getApplicationContext()).getUID().toString());

                return params;
            }

        };

        // Adding request to request queue
        Volley.newRequestQueue(getApplicationContext()).add(strReq);

    }

}
