package com.quics.login.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.quics.login.R
import com.quics.login.classes.MyPreferences

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Calling method to check theme mode(light, dark or system default)
        checkTheme()

        Handler(Looper.getMainLooper()).postDelayed({
            // Creating an object of the Intent class
             val intent = Intent(this, LoginActivity::class.java)

            // Starting activity
            startActivity(intent)

            // Finishing current activity
            finish()
        }, 1500)
    }

    // Method to check theme mode(light, dark or system default)
    private fun checkTheme() {
        // Comparing
        when (MyPreferences(this).darkMode) {
            0 -> {
                // Setting light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                // Applying light mode
                delegate.applyDayNight()
            }
            1 -> {
                // Setting dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                // Applying dark mode
                delegate.applyDayNight()
            }
            2 -> {
                // Setting system default mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

                // Applying system default mode
                delegate.applyDayNight()
            }
        }
    }
}