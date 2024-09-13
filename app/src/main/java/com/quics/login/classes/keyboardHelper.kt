package com.login.classes
import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity


class keyboardHelper(private val context: Context,) {
    private val imm: InputMethodManager
        get() = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
     fun hideKeyboard() {
         val view = (context as? AppCompatActivity)?.currentFocus ?: View(context)
         imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    @SuppressLint("ClickableViewAccessibility")
    fun addEvent(v:View){
        v.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard();
            }
            false
        }
    }


    }
