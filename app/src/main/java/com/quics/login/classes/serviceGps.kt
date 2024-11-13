package com.quics.login.classes

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.location.LocationServices
import com.quics.login.R
import com.quics.login.SearchMap.Companion.LOCATION_REQUEST_CODE


class serviceGps(ctx:Context) {
    var locationManager: LocationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager;
    var _ctx=ctx;

    fun isEnabledGps(): Boolean{
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            ||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    fun isInternetAvailable(): Boolean {
        val connectivityManager =
            _ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }

    }
    fun checkLocationpermissions(){
        if(ContextCompat.checkSelfPermission(_ctx,Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.shouldShowRequestPermissionRationale
                (_ctx as Activity,Manifest.permission.ACCESS_FINE_LOCATION)&&gpsActive()) {
            Log.d("PermissionsGPS", "NO TIENE PERMISO")

            ActivityCompat.requestPermissions(
                _ctx as Activity,  // Aquí va tu Activity o Fragment
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),  // Array con los permisos
                LOCATION_REQUEST_CODE  // El código que manejará la respuesta
            )
//            AlertDialog.Builder(_ctx)
//                .setTitle(_ctx.getString(R.string.header_maps))
//                .setMessage(_ctx.getString(R.string.message_permission_gps))
//                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
//
//                })
//                .create()
//                .show()

        }else{
            Log.d("PermissionsGPS", "ELSE DE PERMISSOS")

            ActivityCompat.requestPermissions(
                _ctx as Activity,  // Aquí va tu Activity o Fragment
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),  // Array con los permisos
                LOCATION_REQUEST_CODE  // El código que manejará la respuesta
            )
        }
    }
     fun gpsActive(): Boolean {
        val locationManager = _ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }




}