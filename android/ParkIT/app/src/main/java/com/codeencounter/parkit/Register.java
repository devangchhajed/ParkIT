package com.codeencounter.parkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codeencounter.parkit.Config.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText name,contact,password,email, carnumber;
    Button btn_register;
    TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.activity_register_et_fullname);
        contact = findViewById(R.id.activity_register_et_phone);
        password = findViewById(R.id.activity_register_et_password);
        email = findViewById(R.id.activity_register_et_email);
        carnumber = findViewById(R.id.activity_register_et_carnumber);

        btn_register = findViewById(R.id.acivity_register_btn_register);
        login = findViewById(R.id.activity_register_login_text);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest strReq = new StringRequest(Request.Method.POST,
                        AppConfig.registerurl, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("reg", "Login Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            int status = jObj.getInt("status");

                            // Check for error node in json
                            if (status==1) {

                                // Launch main activity
                                Intent intent = new Intent(Register.this,Login.class);
                                Toast.makeText(getApplicationContext(),
                                        "User Registered", Toast.LENGTH_LONG).show();
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
                        params.put("email", email.getText().toString().trim());
                        params.put("password", password.getText().toString().trim());
                        params.put("name", name.getText().toString().trim());
                        params.put("phone", contact.getText().toString().trim());
                        params.put("carnum", carnumber.getText().toString().trim());

                        return params;
                    }

                };

                // Adding request to request queue
                Volley.newRequestQueue(Register.this).add(strReq);
            }
        });

    }
}
