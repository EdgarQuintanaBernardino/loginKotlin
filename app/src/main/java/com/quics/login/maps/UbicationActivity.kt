package com.quics.login.maps

import android.annotation.SuppressLint
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.quics.login.R
import com.quics.login.SearchMap
import com.quics.login.home.HomeActivity
import com.quics.login.includes.MyToolbar

class UbicationActivity : AppCompatActivity(){
    private lateinit var btnUbication: CardView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.active_ubication)
        btnUbication=findViewById(R.id.card_action_label);

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        // Setting click listener
        toolbar.setNavigationOnClickListener {
            // Finishing current activity
            val intentHome= Intent(this, HomeActivity::class.java);
            startActivity(intentHome);
        }

        btnUbication.setOnClickListener{val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)}
    }
    @SuppressLint("ServiceCast")
    override fun onResume() {
        super.onResume()

        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (isGpsEnabled) {
            // El GPS está activado
           finish();
        }
    }


    override fun onBackPressed() {
//        super.onBackPressed()
//        val intent = Intent(this, HomeActivity::class.java)
//        intent.putExtra("OPEN_FRAGMENT", "MyProfileFragment")
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//
//        startActivity(intent)
        goHomeClear();
    }
    private fun goHomeClear(){
        var intent =Intent(this,SearchMap::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent);
    }
}