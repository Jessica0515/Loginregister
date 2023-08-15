package com.example.loginregistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    TextView textViewRegisterNow;
    TextInputEditText textInputEditTextEmail,textInputEditTextPassword;
    Button buttonSubmit;
    String name,email,password,apiKey;
    TextView textViewError;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textViewRegisterNow = findViewById(R.id.registerNow);
        textInputEditTextEmail= findViewById(R.id.email);
        textInputEditTextPassword= findViewById(R.id.password);
        buttonSubmit= findViewById(R.id.submit);
        textViewError = findViewById(R.id.error);
        progressBar = findViewById(R.id.loading);
        sharedPreferences = getSharedPreferences("MyAppName",MODE_PRIVATE);

        if(sharedPreferences.getString("logged","false").equals("true")){
            Intent intent= new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewError.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                email= String.valueOf(textInputEditTextEmail.getText());
                password= String.valueOf(textInputEditTextPassword.getText());
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="http://172.20.10.02/LoginRegister/login.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");
                                    if(status.equals("success")){
                                        name= jsonObject.getString("name");
                                        email= jsonObject.getString("email");
                                        apiKey=jsonObject.getString("apiKey");
                                        SharedPreferences.Editor editor= sharedPreferences.edit();
                                        editor.putString("logged","true");
                                        editor.putString("name",name);
                                        editor.putString("email",email);
                                        editor.putString("apiKey",apiKey);
                                        editor.apply();
                                        Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        textViewError.setText(error.getLocalizedMessage());
                        textViewError.setVisibility(View.VISIBLE);
                    }
                }){
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("email", sharedPreferences.getString("email",""));
                        paramV.put("apiKey", sharedPreferences.getString("apiKey",""));
                        return super.getParams();
                    }
                };
                queue.add(stringRequest);
            }
        });
        textViewRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),registration.class);
                startActivity(intent);
                finish();
            }
        });
    }
}