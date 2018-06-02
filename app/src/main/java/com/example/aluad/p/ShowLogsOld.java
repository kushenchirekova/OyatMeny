package com.example.aluad.p;

import android.util.Log;


public  class ShowLogsOld {
    private static final String TAG = "MathAlarm";

    public static boolean LOG_STATUS= true;

    public static void i(String logMessage) {
        Log.i(TAG," "+logMessage);
    }
}
