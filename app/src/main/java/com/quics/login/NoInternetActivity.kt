package com.quics.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.airbnb.lottie.LottieAnimationView
import com.quics.login.home.HomeActivity

class NoInternetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_internet)

        // Getting toolbar by its id
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // Setting click listener
        toolbar.setNavigationOnClickListener {
            // Finishing current activity
            val intentHome= Intent(this, HomeActivity::class.java);
            startActivity(intentHome);
        }
    }
}