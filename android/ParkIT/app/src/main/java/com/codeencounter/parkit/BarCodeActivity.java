package com.codeencounter.parkit;

import androidx.appcompat.app.AppCompatActivity;
import info.androidhive.barcode.BarcodeReader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codeencounter.parkit.Config.AppConfig;
import com.codeencounter.parkit.Config.SessionManager;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarCodeActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    BarcodeReader barcodeReader;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);

        sessionManager = new SessionManager(getApplicationContext());
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);

    }

    @Override
    public void onScanned(Barcode barcode) {
        // playing barcode reader beep sound
        barcodeReader.playBeep();
        Log.e("BarCOde", barcode.displayValue);
        // ticket details activity by passing barcode
        /*
        Intent intent = new Intent(BarCodeActivity.this, BookedStatusPage.class);
        intent.putExtra("code", barcode.displayValue);
        startActivity(intent);

         */
        callN();
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Toast.makeText(getApplicationContext(), "Error occurred while scanning " + errorMessage, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCameraPermissionDenied() {

    }

    public void callN(){
        Log.d("reg", "Login Response: " + sessionManager.getCar()+AppConfig.checkIN);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.checkIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("reg", "Login Response: " + response.toString());

                        Toast.makeText(getApplicationContext(),
                                "CheckedIN", Toast.LENGTH_LONG).show();
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
                params.put("carnum", sessionManager.getCar().toString().trim());

                return params;
            }

        };

        // Adding request to request queue
        Volley.newRequestQueue(BarCodeActivity.this).add(strReq);

    }
}
