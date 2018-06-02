package com.mrkostua.mathalarm.Tools

import android.util.Log



object ShowLogs {
    private var isLogForProduction = false

    fun log(TAG: String, logMessage: String) {
        if (!isLogForProduction)
            Log.i("KOKO " + TAG, " " + logMessage)
    }

}