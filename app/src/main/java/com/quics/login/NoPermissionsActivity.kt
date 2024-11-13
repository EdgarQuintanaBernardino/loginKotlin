package com.quics.login

import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.quics.login.home.HomeActivity
import android.Manifest;
class NoPermissionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions_gps)
        val btnUbication = findViewById<androidx.cardview.widget.CardView>(R.id.card_action_label)
        // Getting toolbar by its id
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // Setting click listener
        toolbar.setNavigationOnClickListener {
            // Finishing current activity
            val intentHome= Intent(this, HomeActivity::class.java);
            startActivity(intentHome);
        }
        btnUbication.setOnClickListener{
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }



    }
    override fun onResume() {
        super.onResume()
        if(checkLocationPermissions()){
            finish();
        }

    }
    // Verificar si existe permiso de ubicación
    private fun checkLocationPermissions(): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED && coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }
    override fun onBackPressed() {

        goHomeClear();
    }
    private fun goHomeClear(){
        var intent =Intent(this,SearchMap::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent);
    }
}