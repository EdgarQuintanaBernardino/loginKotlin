package com.quics.login.classes

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.common.api.GoogleApi.Settings
import com.quics.login.R

class ConfirmDialog(ctx:Context) {
    val dialog = Dialog(ctx)
    val inflater = LayoutInflater.from(ctx)
    val view = inflater.inflate(R.layout.dialog_custom, null)
    val title = view.findViewById<TextView>(R.id.dialog_title)
    val message = view.findViewById<TextView>(R.id.dialog_message)
    // Configurar los botones
    val confirmButton = view.findViewById<Button>(R.id.dialog_confirm)
    val cancelButton = view.findViewById<Button>(R.id.dialog_cancel)

 fun setParams(title:String,message:String){
     dialog.setContentView(view)
     dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
     this.title.text = title;
     this.message.text = message;
     cancelButton.setOnClickListener {
         dialog.dismiss()
     }
 }
    fun setFunctions(confirm:()->Unit){
        confirmButton.setOnClickListener {
            confirm();
            dialog.dismiss()
        }

    }
fun show(){

    // Mostrar el diálogo
    dialog.show()
}




}
