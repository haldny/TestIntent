package com.haldny.testintent

import android.app.IntentService
import android.content.Intent
import android.util.Log
import java.lang.Exception

const val TAG = "HelloIntentService"

class HelloIntentService : IntentService(TAG) {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "onHandleIntent")
        try {
            Thread.sleep(5000)
            val name = intent?.getStringExtra("name")
            Log.d(TAG, "Hello ${name}")
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}", e)
        }
    }

}