package com.haldny.testintent

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.util.*
import kotlin.concurrent.timerTask

class SyncBoundedService: Service(), MyActions {

    companion object {
        const val TAG = "SyncBoundedService"
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

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        timer.cancel()
        super.onDestroy()
    }

    override fun onCount(): Int {
        return count
    }

    override fun onBind(intent: Intent?): IBinder? {
        timer.schedule(timerTask, 1000, 1000)
        return MyBinder(this)
    }

}

interface MyActions {
    fun onCount(): Int
}

class MyBinder(val actions: MyActions): Binder()