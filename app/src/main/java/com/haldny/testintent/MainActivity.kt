package com.haldny.testintent

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var receiver: MyBroadcastReceiver
    private lateinit var internalReceiver: InternalBroadcastReceiver

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

        button_3.setOnClickListener {
        /*val intent = Intent().apply {
            action = "com.haldny.MY_BROADCAST"
            putExtra("key", "Valor")
            putExtra("key2", "Valor2")
        }

        Log.d("HSS", "Sending broadcast via local broadcast manager")
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)*/

            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        val intent1 = Intent(this, HelloIntentService::class.java)
        val intent2 = Intent(this, HelloIntentService::class.java)
        val intent3 = Intent(this, HelloIntentService::class.java)
        val intent4 = Intent(this, HelloIntentService::class.java)
        val intent5 = Intent(this, HelloIntentService::class.java)

        intent1.putExtra("name", "Renata")
        intent2.putExtra("name", "Jemesson")
        intent3.putExtra("name", "Neto")
        intent4.putExtra("name", "Raquel")
        intent5.putExtra("name", "Leonardo")

        startService(intent1)
        startService(intent2)
        startService(intent3)
        startService(intent4)
        startService(intent5)

        if (!this::receiver.isInitialized) {
            receiver = MyBroadcastReceiver()
        }

        if (!this::internalReceiver.isInitialized) {
            internalReceiver = InternalBroadcastReceiver()
        }

        registerReceiver(receiver, IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION))
        registerReceiver(receiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(internalReceiver, IntentFilter("com.haldny.MY_BROADCAST"))

        val componentName = ComponentName(this, MyJobService::class.java)

        val jobInfo = JobInfo.Builder(102, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build()

        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val result = jobScheduler.schedule(jobInfo)

        if (result == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Conseguiu fazer o schedule do job!")
        } else {
            Log.d(TAG, "Nao conseguiu fazer o schedule do job!")
        }
    }

    override fun onPause() {
        super.onPause()

        if (receiver != null) {
            unregisterReceiver(receiver)
        }

        if (internalReceiver != null) {
            LocalBroadcastManager.getInstance(this)
                    .unregisterReceiver(internalReceiver)
        }
    }

    inner class MyBroadcastReceiver: BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("HSS", "inner onReceiver was called")

            if (intent?.action == WifiManager.WIFI_STATE_CHANGED_ACTION) {
                Log.d("HSS", "O status do wifi mudou!")
            }

            if (intent?.action == "android.net.conn.CONNECTIVITY_CHANGE") {
                val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                val activeNetwork = connectivityManager.activeNetworkInfo

                val isInternetActive = activeNetwork != null && activeNetwork.isConnected

                Log.d("HSS", "Is internet connected: $isInternetActive")
            }
        }

    }

    inner class InternalBroadcastReceiver: BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("HSS", "onReceive from InternalBroadcastReceiver")

            if (intent?.action == "com.haldny.MY_BROADCAST") {
                Log.d("HSS", "Recebemos a action ${intent?.action}")
            }
        }

    }
}