package com.codeencounter.nagarro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codeencounter.nagarro.Config.AppConfig;
import com.codeencounter.nagarro.Config.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    Button register,login;
    EditText email,password;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.activity_login_btn_signin);
        register = findViewById(R.id.activity_login_btn_register);
        email = findViewById(R.id.activity_login_email);
        password = findViewById(R.id.activity_login_password);

        login = findViewById(R.id.activity_login_btn_signin);
        register = findViewById(R.id.activity_login_btn_register);

        Log.d("reg splash", "Reg");

        sessionManager = new SessionManager(getApplicationContext());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringRequest strReq = new StringRequest(Request.Method.POST,
                        AppConfig.loginurl, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("reg", "Login Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            int status = jObj.getInt("status");

                            // Check for error node in json
                            if (status==1) {

                                // Launch main activity
                                Intent intent = new Intent(Login.this,HomePage.class);
                                String uid = jObj.getString("user_id");
                                String name = jObj.getString("user_name");
                                String phone = jObj.getString("phone_no");

                                sessionManager.setLogin(true, uid, name, phone);
                                Log.e("login", sessionManager.getName());

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

                        return params;
                    }

                };

                // Adding request to request queue
                Volley.newRequestQueue(Login.this).add(strReq);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
