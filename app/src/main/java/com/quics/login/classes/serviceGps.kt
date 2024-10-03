package com.quics.login.classes

import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices


class serviceGps(ctx:Context) {
    var locationManager: LocationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager;


    fun isEnabledGps(): Boolean{
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            ||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }



}