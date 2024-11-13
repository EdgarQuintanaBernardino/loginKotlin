package com.quics.login.includes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.startActivity
import com.quics.login.R

class MyToolbar {

    companion object{
        fun  show(activity: AppCompatActivity,title:String,backButton:Boolean,back: Intent){
            var toolbar:Toolbar =activity.findViewById(R.id.toolbar)
            activity.setSupportActionBar(toolbar);
            activity.supportActionBar?.setTitle(title);
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(backButton);
            toolbar.setOnClickListener{it-> goActivity(back,activity)}

            toolbar.setNavigationOnClickListener {
                goActivity(back,activity)

            }
        }
        private fun goActivity(back: Intent,activity: AppCompatActivity){
            back.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            activity.startActivity(back)  // Inicia la actividad cuando se presiona la flecha
        }
        fun setTitle(activity: AppCompatActivity,title: String){
            activity.supportActionBar?.setTitle(title);
        }
    }

}