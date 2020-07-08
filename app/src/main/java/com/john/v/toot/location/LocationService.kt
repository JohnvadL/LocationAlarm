package com.john.v.toot.location

import android.app.Service
import android.content.Intent
import android.os.IBinder

class LocationService:Service (){


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_NOT_STICKY
    }


    companion object
    {

    }
}