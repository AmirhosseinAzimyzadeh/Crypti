package com.cryptogeraphyapp.azimyzadeh.amirhossein.crypti;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
/**
 * this class made for file and data controller (NOT USED IN APP)*/

public class AppDataController {
    private Activity activity ;
    private SharedPreferences appData;

    private static final String APP_DATA_NAME = "app_data_crypti";
    private static final String LAST_PASSWORD_KEY = "lk";
    private static final String NUMBER_OF_KEY_GENERATED = "nkg";
    private static final String LAST_DATE_KEY = "ldk";



    public AppDataController(Activity activity) {
        this.activity = activity;
        appData=activity.getSharedPreferences(APP_DATA_NAME,Context.MODE_PRIVATE);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
