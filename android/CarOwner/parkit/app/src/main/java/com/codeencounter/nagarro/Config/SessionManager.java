package com.codeencounter.nagarro.Config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.codeencounter.nagarro.Adapters.ParkingSuggestionList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "ParkIT";

    private static final String KEY_ID = "uid";
    private static final String KEY_NAME = "uname";
    private static final String KEY_PHONE = "uphone";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_LOC = "location";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }



    public void setLogin(boolean isLoggedIn, String id, String name, String phone) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PHONE, phone);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }
    public void setLocations(String loc) {

        editor.putString(KEY_LOC, loc);

        editor.commit();
    }


    public String getLocations(){
        return pref.getString(KEY_LOC, "{}");
    }


    public List<ParkingSuggestionList> getParkingList() {

            Gson gson = new Gson();
            List<ParkingSuggestionList> companyList;

            String string = pref.getString(KEY_LOC, null);
            Type type = new TypeToken<List<ParkingSuggestionList>>() {
            }.getType();
            companyList = gson.fromJson(string, type);
            return companyList;
    }


    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public String getUID(){
        return (String) pref.getString(KEY_ID, "-1");
    }
    public String getName(){
        return (String) pref.getString(KEY_NAME, "Default");
    }
    public String getPhone(){
        return (String) pref.getString(KEY_PHONE, "0");
    }
}