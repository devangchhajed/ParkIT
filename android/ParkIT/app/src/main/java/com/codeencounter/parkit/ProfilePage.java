package com.codeencounter.parkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codeencounter.parkit.Adapters.ParkingSuggestionList;
import com.codeencounter.parkit.Config.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProfilePage extends AppCompatActivity {

    TextView booking, barcode;
    ImageView imageView;
    private TextToSpeech myTTS;
    private SpeechRecognizer mySpeechRecognizer;
    ParkingSuggestionList p;
    String totaln="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        getOne();

        booking = findViewById(R.id.activity_profile_page_txt_booking_history);
        barcode = findViewById(R.id.activity_profile_page_txt_bar_code);
        imageView = findViewById(R.id.activity_profile_page_img_mic);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ProfilePage.this, "Speak Now", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                mySpeechRecognizer.startListening(intent);

            }
        });

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilePage.this,BookingHistory.class);
                startActivity(intent);
            }
        });

        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilePage.this,BarCodeActivity.class);
                startActivity(intent);
            }
        });


        initializeTextToSpeech();
        initializeSpeechRecognizer();

    }

    private void initializeSpeechRecognizer() {

        if(SpeechRecognizer.isRecognitionAvailable(this)){
            mySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int i) {

                }

                @Override
                public void onResults(Bundle bundle) {

                    List <String> results = bundle.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                    );
                    processResult(results.get(0));
                }

                @Override
                public void onPartialResults(Bundle bundle) {

                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });
        }

    }

    private void processResult(String command) {

        getOne();
        command = command.toLowerCase();

        //find the nearest parking spot
        //directions for the nearest spot

        Toast.makeText(this, "Till this function", Toast.LENGTH_SHORT).show();

        if (command.indexOf("find") != -1){
            getOne();

        }
        if (command.indexOf("navigate") != -1){
            speak("Navigating to the parking spot Opening Google Maps");

            Uri gmmIntentUri = Uri.parse("google.navigation:q="+p.getLat()+","+p.getLng());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }

    }

    public void getOne(){
        final String lat = String.valueOf(AppConfig.lat);
        final String lng = String.valueOf(AppConfig.lng);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.oneplaceslist+"?lat="+lat+"&lng="+lng, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("oneplace", response+AppConfig.oneplaceslist+"?lat="+lat+"&lng="+lng);
                try {
                    JSONObject jObj = new JSONObject(response);
                    p = new ParkingSuggestionList();

                    p.setParking_lot_id(jObj.getString("parking_lot_id"));
                    p.setName(jObj.getString("parking_lot_name"));
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
                    totaln= String.valueOf(jObj.getInt("totaln"));
                    speak("I have Found "+totaln+" Location near you. Recommended location is "+p.getName());

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



    private void initializeTextToSpeech() {

        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(myTTS.getEngines().size() == 0){
                    Toast.makeText(ProfilePage.this, "There is no TTS Engine installed on your device", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    myTTS.setLanguage(Locale.US);
                    speak("Hello! I am your Parking Assistant");
                }

            }
        });

    }

    private void speak(String message) {

        if(Build.VERSION.SDK_INT >= 21){
            myTTS.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
            Toast.makeText(this, "Toast Recieved successful", Toast.LENGTH_SHORT).show();
        }else{
            myTTS.speak(message,TextToSpeech.QUEUE_FLUSH,null);
            Toast.makeText(this, "This shoudny display", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        myTTS.shutdown();
    }
}
