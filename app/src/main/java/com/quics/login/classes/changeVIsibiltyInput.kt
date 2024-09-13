package com.login.classes

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.text.InputType
import android.view.MotionEvent
import android.widget.EditText

class changeVIsibiltyInput(editText: EditText) {
    private var input=editText;

    @SuppressLint("ClickableViewAccessibility")
     fun addEventEye(){
        input.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                // Obtener el drawableEnd (drawableRight en API nivel < 17)
                val drawableEnd: Drawable? = input.compoundDrawablesRelative[2]
                drawableEnd?.let {
                    if (event.rawX >= (input.right - input.compoundPaddingEnd)) {
                        // Se ha hecho clic en el drawableEnd
                        onDrawableEndClicked()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }
    }
    private fun onDrawableEndClicked() {
        val currentInputType = input.inputType
        if (currentInputType == InputType.TYPE_CLASS_TEXT) {
            // Cambiar a tipo contraseña
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        } else {
            // Cambiar a tipo texto

            input.inputType = InputType.TYPE_CLASS_TEXT
        }

        // Mover el cursor al final del texto
        input.setSelection(input.text.length)


    }

}