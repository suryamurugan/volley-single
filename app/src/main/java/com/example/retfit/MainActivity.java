package com.example.retfit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button myButton = findViewById(R.id.myButton);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleySingleton volleySingleton = VolleySingleton.getInstance(MainActivity.this);

                String authToken = "your_auth_token";
                String refreshToken = "your_refresh_token";
                volleySingleton.setAuthTokens(authToken, refreshToken);
                // Define the API endpoint URL
                String url = "https://reqres.in/api/users?page=2";

// Make the API request using VolleySingleton
                volleySingleton.makeAPIRequest(url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Handle successful response
                                Log.d("API Response", response);

                                // Update UI with the response data
                                // Example: textView.setText(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle request error
                                error.printStackTrace();
                            }
                        });




            }
        });
    }

    private void makeAPIRequest2() {
        String url = "https://reqres.in/api/users?page=2";
        Log.d("DEBUG API", "calling API");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful response
                        Log.d("API Response", response);

                        // Update UI with the response data
                        // Example: textView.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle request error
                        error.printStackTrace();
                    }
                });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    private void makeAPIRequest() {
        String url = "https://reqres.in/api/users?page=2";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful response
                        Log.d("API Response", response);

                        // Update UI with the response data
                        // Example: textView.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle request error
                        error.printStackTrace();
                    }
                });

        requestQueue.add(stringRequest);
    }


}
