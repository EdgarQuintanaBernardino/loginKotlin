package com.quics.login.includes

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.quics.login.R

class MyToolbar {
    companion object{
        fun  show(activity: AppCompatActivity,title:String,backButton:Boolean){
            var toolbar:Toolbar =activity.findViewById(R.id.toolbar)
            activity.setSupportActionBar(toolbar);
            activity.supportActionBar?.setTitle(title);
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(backButton);
        }
    }

}