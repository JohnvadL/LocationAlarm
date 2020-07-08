package com.john.v.toot.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log


class AutoStart : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("AutoStrart", "OnReceive")

        val intent = Intent(context, NotificationsService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.startForegroundService(intent)
        } else {
            context?.startService(intent)
        }
    }
}