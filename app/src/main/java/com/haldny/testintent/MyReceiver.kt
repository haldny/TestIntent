package com.haldny.testintent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("HSS", "onReceive foi chamado")

        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("HSS", "O sistema operacional foi inicializado")
        }

        if (intent?.action == Intent.ACTION_LOCKED_BOOT_COMPLETED) {
            Log.d("HSS", "O sistema operacional foi inicializado com locked")
        }

        if (intent?.action == Intent.ACTION_REBOOT) {
            Log.d("HSS", "Foi realizado o Reboot")
        }

    }

}