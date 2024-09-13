package com.quics.login.home


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment

//import com.example.fragments.CartFragment
import com.quics.login.fragments.HomeFragment
//import com.example.fragments.MyProfileFragment
//import com.example.fragments.NotificationsFragment

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.quics.login.R
import com.quics.login.login.LoginActivity


class HomeActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth;

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth=FirebaseAuth.getInstance();

        setContentView(R.layout.activity_home)


        // Calling method to replace FrameLayout with the HomeFragment by default
        replaceFragment(HomeFragment())

        // Getting drawer by its id
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout_home)

        // Setting lock mode for the drawer
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        // Getting NavigationView by its id
        val navigationView = findViewById<NavigationView>(R.id.navigation_view_drawer_layout_home)

        // Setting on navigation item selected listener for the NavigationView
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {

                }
                R.id.logOut->{

                    mAuth.signOut()
                    goLogin();
                }
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
                R.id.logOut->{

                    mAuth.signOut()
                    goLogin();
                }
            }

            true
        }
    }
    public override fun onStart(){
        super.onStart()
        var currentUser=mAuth.currentUser
        if(currentUser==null){
            // mAuth.signOut()
            goLogin();
        }

    }
    private fun goLogin(){
        val intent = Intent(this, LoginActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        // Iniciar la LoginActivity
        startActivity(intent)

        // Opcionalmente, finalizar la actividad actual si es necesario
        finish()
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
