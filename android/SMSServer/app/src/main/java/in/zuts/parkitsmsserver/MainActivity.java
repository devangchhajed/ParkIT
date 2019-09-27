package in.zuts.parkitsmsserver;

import androidx.appcompat.app.AppCompatActivity;
import in.zuts.parkitsmsserver.Adapters.ParkingSuggestionList;
import in.zuts.parkitsmsserver.Config.AppConfig;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SmsReciever.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText, String sender) {
                processMessage(messageText,sender);
                Log.d("Text",messageText);
                Toast.makeText(MainActivity.this,"Message: "+messageText+" Sender: "+sender,Toast.LENGTH_LONG).show();
            }
        });

    }


    public void processMessage(String messageText, final String sender){
        Log.e("mssg", messageText+sender);
        try{
            JSONObject jObj = new JSONObject(messageText);
            String code = jObj.getString("code");
            Log.e("reg", code+"-----");
            if(code.equals("9")){
                Log.e("reg", "1 called");

                final String pid=jObj.getString("pid");
                Log.e("reg", "Login Response: " + pid);

                String url  = AppConfig.oneplace+"?pid="+pid;
                Log.e("reg", "Login Response: " + url);
                StringRequest strReq12 = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("reg", "Login Response: " + response);

                        SmsManager smgr = SmsManager.getDefault();
                        smgr.sendTextMessage(sender,null,response ,null,null);

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
                        params.put("pid", pid);
                        return params;
                    }
                };

                // Adding request to request queue
                Volley.newRequestQueue(MainActivity.this).add(strReq12);

            }
            if(code.equals("2")){

                final String uid=jObj.getString("uid"),pid=jObj.getString("pid");
                Log.d("reg", "Login Response: " + uid+"-"+pid);

                StringRequest strReq = new StringRequest(Request.Method.POST,
                        AppConfig.booking, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("reg", "Login Response: " + response.toString());
                        Log.d("reg", "Login Response: " + response.toString());

                        SmsManager smgr = SmsManager.getDefault();
                        smgr.sendTextMessage(sender,null,response,null,null);


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
                        params.put("uid", uid);
                        params.put("pid", pid);
                        return params;
                    }
                };

                // Adding request to request queue
                Volley.newRequestQueue(MainActivity.this).add(strReq);

            }


        } catch (JSONException e) {

            Log.e("offf", messageText);
            LatLng location = getLocationFromAddress(getApplicationContext(), messageText);
            String lat=String.valueOf(location.latitude),lng=String.valueOf(location.longitude);
            getListofLot(lat,lng,sender);
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
            if (address.size()>0){
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude() );
                return p1;
            }
            else{
                Toast.makeText(getApplicationContext(),"No Location Found", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return p1;
    }


    public void getListofLot(final String lat, final String lng, final String sender){
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.placeslist, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("loclist", "Login Response: " + response.toString());


                try {
                    String Message = "ParkIT\n";
                    JSONArray jArr = new JSONArray(response);
                    for(int i = 0; i<jArr.length();i++){
                        if(i>=5)
                            break;

                        Message+=(i+1)+". ";

                        JSONObject jObj = (JSONObject) jArr.get(i);

                        ParkingSuggestionList p = new ParkingSuggestionList();

                        p.setParking_lot_id(jObj.getString("parking_lot_id"));
                        p.setName(jObj.getString("parking_lot_name"));
                        Message+=p.getName()+"\n";
                        Log.e("AA", p.getName());

                        p.setAddress(jObj.getString("parking_lot_address"));
                        Message+="Address : "+p.getAddress()+"\n";
                        p.setLat(jObj.getString("parking_lot_latitude"));
                        p.setLng(jObj.getString("parking_lot_longitude"));
                        p.setRatings(jObj.getString("ratings"));
                        p.setComments(jObj.getString("comments"));
                        p.setTotal_general(jObj.getString("total_general"));
                        p.setTotal_differently_abled("total_differently_abled");
                        p.setCurrent_general(jObj.getString("current_general"));
                        p.setCurrent_differently_abled(jObj.getString("current_differently_abled"));
                        Message+="Status : "+p.getCurrent_general()+"\\"+p.getTotal_general()+"\n";
                        p.setPrice_per_hour(jObj.getString("price_per_hour"));
                        p.setAvailable(jObj.getString("available"));
                        p.setValet_available(jObj.getString("valet_available"));
                        p.setCctv_available(jObj.getString("cctv_available"));
                        p.setCount(jObj.getString("count"));
                        p.setUser_id(jObj.getString("user_id"));
                        p.setDistance(jObj.getString("distance"));

                    }

                    SmsManager smgr = SmsManager.getDefault();
                    smgr.sendTextMessage(sender,null,Message,null,null);

                    // Check for error node in json
                } catch (JSONException e) {
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





}
