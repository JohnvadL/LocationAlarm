package com.john.v.toot.location

import android.content.Context
import com.google.android.gms.location.LocationServices

class LocationExtended {


    companion object{


        fun getCurrentLocation(context: Context): Pair<Double, Double>{


            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(context)



            fusedLocationClient.lastLocation.addOnSuccessListener {

            }

            return Pair(1.0,2.0)
        }
    }
}