package com.quics.login.home


import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY

//import com.example.fragments.CartFragment
import com.quics.login.fragments.HomeFragment
//import com.example.fragments.MyProfileFragment
//import com.example.fragments.NotificationsFragment

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.quics.login.R
import com.quics.login.SearchMap
import com.quics.login.classes.ConfirmDialog
import com.quics.login.classes.DialogLogout
import com.quics.login.classes.serviceGps
import com.quics.login.login.LoginActivity


class HomeActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth;
    private var flagSavedLocation = false

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var init_lt: Double = 0.0
    private var init_ln: Double = 0.0

    private var distance: Double = 0.0
    private var maxSpeed: Double = 0.0
    private var avgSpeed: Double = 0.0
    private var speed: Double = 0.0

    companion object {
        val REQUIRED_PERMISSIONS_GPS= arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)
        lateinit var fusedLocation:FusedLocationProviderClient;

    }
    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permiso concedido
            Toast.makeText(this, "Permiso de ubicación concedido", Toast.LENGTH_SHORT).show()
            if(allPermissionsGrantedGPS()){
                fusedLocation=LocationServices.getFusedLocationProviderClient(this);
                fusedLocation.lastLocation.addOnSuccessListener { it-> setDataLocation()}
            }else{
                permissionGpsNative()
            }

        } else {
            // Permiso denegado
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                // El usuario no marcó "No volver a preguntar", mostrar un diálogo explicativo
                permissionGpsNative()
            } else {
                // El usuario marcó "No volver a preguntar", redirigir a configuración
                showSettingsRedirectDialog()
            }
            Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }
private lateinit var drawerLayout:DrawerLayout;
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth=FirebaseAuth.getInstance();

        setContentView(R.layout.activity_home)


        // Calling method to replace FrameLayout with the HomeFragment by default
        replaceFragment(HomeFragment())

        // Getting drawer by its id
        drawerLayout = findViewById(R.id.drawer_layout_home)

        // Setting lock mode for the drawer
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        // Getting NavigationView by its id
        val navigationView = findViewById<NavigationView>(R.id.navigation_view_drawer_layout_home)

        // Setting on navigation item selected listener for the NavigationView
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {

                }
                R.id.logOut->showCustomDialog()
//                R.id.support -> {
//                    // Calling method to open specified activity
//                    openActivity(this, "com.example.quics.SupportActivity")
//                }
//
//                R.id.chats -> {
//                    // Calling method to open specified activity
//                    openActivity(this, "com.example.quics.ChatsActivity")
//                }
//                R.id.not_found -> {
//                    // Calling method to open specified activity
//                    openActivity(this, "com.example.quics.PageNotFoundActivity")
//                }
//                R.id.no_internet -> {
//                    // Calling method to open specified activity
//                    openActivity(this, "com.example.quics.NoInternetActivity")
//                }
//                R.id.faqs -> {
//                    // Calling method to open specified activity
//                    openActivity(this, "com.example.quics.FaqsActivity")
//                }
//                R.id.login -> {
//                    // Calling method to open specified activity
//                    openActivity(this, "com.example.quics.LoginActivity")

               }


        // CLosing drawer
        drawerLayout.closeDrawer(GravityCompat.START)
        true

         }


        // Getting BottomNavigationView by its id
        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.home_bottom_navigation_view)

        // Setting Home selected in BottomNavigationView by default
         bottomNavigationView.selectedItemId = R.id.home

        // Setting on item selected listener for the BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    // Calling method to replace FrameLayout with the HomeFragment
                    replaceFragment(HomeFragment())

                }
                R.id.cart->{
                    var intent=Intent(this,SearchMap::class.java)
                    startActivity(intent);
                }
//                R.id.cart -> {
//                    // Calling method to replace FrameLayout with the CartFragment
//                    replaceFragment(CartFragment())
//                }
//                R.id.profile -> {
//                    // Calling method to replace FrameLayout with the MyProfileFragment
//                    replaceFragment(MyProfileFragment())
//                }
//                R.id.notifications -> {
//                    // Calling method to replace FrameLayout with the NotificationsFragment
//                    replaceFragment(NotificationsFragment())
//                }
                R.id.logOut-> showCustomDialog();

            }

            true
        }
    }
    private fun logOut(){
        mAuth.signOut()
        goLogin();
    }
    public override fun onStart(){
        super.onStart()
        var currentUser=mAuth.currentUser
        if(currentUser==null){
            // mAuth.signOut()
            goLogin();
        }
        ///activar gps
        var sGps=serviceGps(this);
//        if(!sGps.isEnabledGps()){
//            ///No tenemos gps
//            permissionGps()
//        }
    }
    override fun onBackPressed(){
        //super.onBackPressed()
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
        drawerLayout.closeDrawer(GravityCompat.START)

    }else{
          //  DialogLogout().show(supportFragmentManager, "GAME_DIALOG")

            showCustomDialog()
    }

    }
    private fun permissionGps(){
        var dialog=ConfirmDialog(this);
        dialog.setParams(this.getString(R.string.title_permission_gps),this.getString(R.string.message_permission_gps))
        dialog.setFunctions(::permissionGpsNative);
        dialog.show();
    }
    fun permissionGpsNative(){
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
    private fun showSettingsRedirectDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permiso requerido")
            .setMessage("Para continuar usando la aplicación, habilita los permisos de ubicación desde la configuración de la app.")
            .setPositiveButton("Ir a configuración") { _, _ ->
                // Redirigir al usuario a la configuración de la aplicación
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun allPermissionsGrantedGPS()= REQUIRED_PERMISSIONS_GPS.all{
        ContextCompat.checkSelfPermission(baseContext,it)==PackageManager.PERMISSION_GRANTED
    }
    private fun showCustomDialog() {
        var dialog=ConfirmDialog(this);
        dialog.setParams(this.getString(R.string.my_profile_exit),this.getString(R.string.confirm_close_session))
        dialog.setFunctions(::logOut);
        dialog.show();
    }
    private fun goLogin(){
        val intent = Intent(this, LoginActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        // Iniciar la LoginActivity
        startActivity(intent)

        // Opcionalmente, finalizar la actividad actual si es necesario
        finish()
    }

    private fun setDataLocation(){
        var mLocationRequest = LocationRequest.create().apply {
            priority = PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1};
        fusedLocation=LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocation.requestLocationUpdates(mLocationRequest,mLocationCallback,Looper.myLooper());


    }
    private var mLocationCallback=object:LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
           // super.onLocationResult(p0)
            val mLocation: Location? =p0.lastLocation;
            Log.d("GPSSSSSSSSSSSSSSSSSSSSS", mLocation?.latitude.toString())


        };


    }
    // Method to toggle replace fragment
    private fun replaceFragment(fragment: Fragment) {
        // Getting supportFragmentManager
        val fragmentManager = supportFragmentManager

        // Beginning transaction
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Replacing FrameLayout with a desired Fragment
        fragmentTransaction.replace(R.id.frame_layout, fragment)

        // Committing Fragment transaction
        fragmentTransaction.commit()
    }

    // Method to toggle navigation drawer menu. NOTE: this method will be called from the Home fragment
    fun openCloseNavigationDrawer() {
        // Getting drawer layout by its id
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout_home)

        // Comparing
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // Closing drawer
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // Opening drawer
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    // Method to open specified activity
    fun openActivity(context: Context, activityName: String) {
        // Getting a reference to the activity class based on its name
        val activityClass = Class.forName(activityName)

        // Creating an object of the Intent class
        val intent = Intent(context, activityClass)

        // Starting activity
        context.startActivity(intent)
    }
}
