package com.john.v.toot

import android.app.Application
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.john.v.toot.notifications.NotificationsService


class BuddySystemApplication : Application() {
    override fun onCreate() {
        Log.e("BuddySystemApplication", "onCreate")
        updateLocation(baseContext)

        val intent = Intent(baseContext, NotificationsService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            baseContext?.startForegroundService(intent)
        } else {
            baseContext?.startService(intent)
        }

        super.onCreate()

    }

    companion object{
        var latitude: Double?  = 0.0
        var longitude: Double? = 0.0

        fun updateLocation (context:Context){

            val locationManager: LocationManager =
                context.getSystemService(Context.LOCATION_SERVICE)
                        as LocationManager
            val testListener = TestListener()
            try {
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, testListener, null)
            } catch (e:SecurityException){
                Log.e("Buddy System Application", "Security Exception: Location Permission not granted")
            }
        }

    }

    class TestListener : LocationListener {
        override fun onProviderEnabled(provider: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(provider: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            Log.e("NotificationsReceiver", "status changed")
        }

        override fun onLocationChanged(p0: Location?) {


            latitude =  p0?.latitude
            longitude = p0?.longitude


            Log.e(
                "BuddySystemApplication",
                "location changed" + p0?.latitude.toString() + ":" + p0?.longitude.toString()
            )
        }
    }
}