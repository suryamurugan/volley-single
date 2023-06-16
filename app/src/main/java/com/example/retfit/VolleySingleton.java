package com.example.retfit;

import android.content.Context;
import android.util.Log;

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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class VolleySingleton {
    private static VolleySingleton instance;
    private RequestQueue requestQueue;
    private static Context context;
    private String authToken;
    private String refreshToken;

    private VolleySingleton(Context ctx) {
        context = ctx;
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        Log.d("DE:",req.toString());
        getRequestQueue().add(req);
    }

    public void setAuthTokens(String authToken, String refreshToken) {
        this.authToken = authToken;
        this.refreshToken = refreshToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }



    public void makeAPIRequest(String url, final Response.Listener<String> successListener, final Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                successListener,
                errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authToken);
                return headers;
            }
        };
        Log.d("DE:",stringRequest.toString());
        addToRequestQueue(stringRequest);
    }



    public void makeAPIRequestTk1(String url, final Response.Listener<String> successListener, final Response.ErrorListener errorListener) {
        if (isTokenExpired()) {
            // Token has expired, handle token refresh
            refreshAuthToken(new AuthTokenRefreshListener() {
                @Override
                public void onTokenRefreshed() {
                    // Token refreshed, retry the API request
                    makeAPIRequest(url, successListener, errorListener);
                }

                @Override
                public void onTokenRefreshFailed() {
                    // Token refresh failed, handle the error
                    // You can invoke the error listener or handle it according to your requirements
                    errorListener.onErrorResponse(new VolleyError("Token refresh failed"));
                }
            });
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    successListener,
                    errorListener) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + authToken);
                    return headers;
                }
            };
            Log.d("DE:", stringRequest.toString());
            addToRequestQueue(stringRequest);
        }
    }

    public interface AuthTokenRefreshListener {
        void onTokenRefreshed();
        void onTokenRefreshFailed();
    }

    public void refreshAuthToken(final AuthTokenRefreshListener listener) {
        // Make an API request to refresh the authentication token
        String refreshUrl = "YOUR_REFRESH_URL";

        StringRequest refreshRequest = new StringRequest(Request.Method.POST, refreshUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Parse the response and update the authToken and refreshToken variables
                        // Assuming the response contains the new tokens in JSON format
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String newAuthToken = jsonResponse.getString("authToken");
                            String newRefreshToken = jsonResponse.getString("refreshToken");

                            // Update the authToken and refreshToken variables
                            setAuthTokens(newAuthToken, newRefreshToken);

                            // Notify the listener that token refresh was successful
                            listener.onTokenRefreshed();
                        } catch (JSONException e) {
                            // JSON parsing error, token refresh failed
                            listener.onTokenRefreshFailed();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error and notify the listener that token refresh failed
                        listener.onTokenRefreshFailed();
                    }
                }
        );

        // Add the refresh request to the request queue
        addToRequestQueue(refreshRequest);
    }

    public boolean isTokenExpired() {
        try {
            // Assuming you have the JWT token stored in a variable called "authToken"
            Jws<Claims> jws = Jwts.parser().parseClaimsJws(authToken);
            Claims claims = jws.getBody();
            long expirationTimeMillis = claims.getExpiration().getTime();
            long currentTimeMillis = System.currentTimeMillis();

            return expirationTimeMillis <= currentTimeMillis;
        } catch (ExpiredJwtException e) {
            // The token has expired
            return true;
        } catch (Exception e) {
            // An error occurred while parsing the token or checking the expiration
            return true; // or handle the error accordingly
        }
    }

}
