package com.haldny.testintent

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main2.*
import java.util.*
import java.util.concurrent.TimeUnit
import androidx.lifecycle.Observer
import androidx.work.*

class MainActivity2 : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity2"
    }

    private lateinit var textView: TextView

    private var connection: ServiceConnection? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        textView = findViewById(R.id.textview_time)

        connection = object : ServiceConnection {

            override fun onServiceConnected(componentName: ComponentName?, binder: IBinder?) {
                Log.d(TAG, "Serviço foi conectado")

                val myBinder = binder as MyBinder

                val timer = Timer()
                timer.schedule(UpdateTimeTask(textview_time, myBinder), 1000, 1000)
            }

            override fun onServiceDisconnected(componentName: ComponentName?) {
                Log.d(TAG, "Serviço desconectado")
                unbindService(this)
            }

        }

        button_start_service.setOnClickListener {
            Log.d(TAG, "Clicou no botao start service")
            val intent = Intent(this, SyncService::class.java)
            startService(intent)
        }

        button_stop_service.setOnClickListener {
            Log.d(TAG, "Clicou no botao stop service")
            val intent = Intent(this, SyncService::class.java)
            stopService(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "bind service")
        connection?.let {
            bindService(
                Intent(this, SyncBoundedService::class.java),
                it, Context.BIND_AUTO_CREATE
            )
        }
    }

    override fun onPause() {
        super.onPause()

        Log.d(TAG, "onPause")
        if (connection != null) {
            Log.d(TAG, "unbindService")
            unbindService(connection!!)
            connection = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy")
    }

    inner class UpdateTimeTask(val textView: TextView, val myBinder: MyBinder) : TimerTask() {

        override fun run() {
            runOnUiThread {
                textView.text = myBinder.actions.onCount().toString()
            }
        }
    }
}