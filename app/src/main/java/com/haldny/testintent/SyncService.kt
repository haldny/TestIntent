package com.haldny.testintent

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.*
import kotlin.concurrent.timerTask

class SyncService: Service() {

    companion object {
        const val TAG = "SyncService"
    }

    private lateinit var timerTask: TimerTask
    private val timer = Timer()
    private var count = 0

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")

        timerTask = timerTask {
            count++
            Log.d(TAG, "Count value: $count")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        timer.schedule(timerTask, 1000, 1000)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        timer.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}