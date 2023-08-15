package com.example.loginregistration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textViewName,textViewEmail,textViewFetchResult;
    SharedPreferences sharedPreferences;
    Button buttonLogout,buttonFetchUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewName=findViewById(R.id.name);
        textViewEmail=findViewById(R.id.email);
        textViewFetchResult=findViewById(R.id.fetchResult);
        buttonLogout=findViewById(R.id.logout);
        buttonFetchUser= findViewById(R.id.fetchProfile);

        sharedPreferences = getSharedPreferences("MyAppName",MODE_PRIVATE);
        if(sharedPreferences.getString("logged","false").equals("false")){
            Intent intent= new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
            finish();
        }

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="http://172.20.10.02/LoginRegister/logout.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (response.equals("success")){
                                    SharedPreferences.Editor editor= sharedPreferences.edit();
                                    editor.putString("logged","");
                                    editor.putString("name","");
                                    editor.putString("email","");
                                    editor.putString("apiKey","");
                                    editor.apply();
                                    Intent intent= new Intent(getApplicationContext(),Login.class);
                                    startActivity(intent);
                                    finish();

                                }
                                else
                                    Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                }){
                    @Nullable
                    @Override
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
    }
}