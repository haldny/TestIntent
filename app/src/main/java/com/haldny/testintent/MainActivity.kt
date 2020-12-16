package com.haldny.testintent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var receiver: MyBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_1.setOnClickListener {
            val intent = Intent().apply {
                setAction(Intent.ACTION_VIEW)
                setData(Uri.parse("https://www.google.com"))
            }

            if (intent.resolveActivity(packageManager) != null) {
                Log.d("HSS", "Start activity action VIEW")
                startActivity(intent)
            } else {
                Log.d("HSS", "Nao tem activities para resolver essa action HALDNY")
            }
        }

        button_2.setOnClickListener {
            val intent = Intent().apply {
                setAction("HALDNY3")
            }

            if (intent.resolveActivity(packageManager) != null) {
                Log.d("HSS", "Start activity action HALDNY3")
                startActivity(intent)
            } else {
                Log.d("HSS", "Nao tem activities para resolver essa action HALDNY3")
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (!this::receiver.isInitialized) {
            receiver = MyBroadcastReceiver()
        }

        registerReceiver(receiver, IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION))
        registerReceiver(receiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
    }

    override fun onPause() {
        super.onPause()

        if (receiver != null) {
            unregisterReceiver(receiver)
        }
    }

    inner class MyBroadcastReceiver: BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("HSS", "inner onReceiver was called")

            if (intent?.action == WifiManager.WIFI_STATE_CHANGED_ACTION) {
                Log.d("HSS", "O status do wifi mudou!")

                Log.d("HSS", "Wifi Status: ${intent.getStringExtra(WifiManager.EXTRA_WIFI_STATE)}")
            }

            if (intent?.action == "android.net.conn.CONNECTIVITY_CHANGE") {
                val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                val activeNetwork = connectivityManager.activeNetworkInfo

                val isInternetActive = activeNetwork != null && activeNetwork.isConnected

                Log.d("HSS", "Is internet connected: $isInternetActive")
            }
        }

    }
}