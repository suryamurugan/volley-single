package com.example.retfit;
import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREF_NAME = "TokenManager";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";

    private static TokenManager instance;
    private SharedPreferences sharedPreferences;

    private TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized TokenManager getInstance(Context context) {
        if (instance == null) {
            instance = new TokenManager(context);
        }
        return instance;
    }

    public void setAuthToken(String authToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AUTH_TOKEN, authToken);
        editor.apply();
    }

    public String getAuthToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    public void setRefreshToken(String refreshToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.apply();
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null);
    }

    public void clearTokens() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_AUTH_TOKEN);
        editor.remove(KEY_REFRESH_TOKEN);
        editor.apply();
    }

}
