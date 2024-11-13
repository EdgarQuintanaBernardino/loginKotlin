package com.quics.login.utils

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.quics.login.data.User
import PopupManager
import android.content.Intent
import com.quics.login.data.DirectionData
import com.quics.login.data.toMap
import kotlinx.coroutines.tasks.await

class UserActive {


    fun saveUserData(context: Context, user: User) {

        val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(user) // Convierte el objeto `User` a JSON
        editor.putString("user", json)
        editor.apply()
    }
    fun getUserData(context: Context): User? {
        val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("user", null)
        return gson.fromJson(json, User::class.java) // Convierte de JSON a un objeto `User`
    }
    fun signOut(context: Context) {
        PopupManager(context).showPopup("Cerrando Sesión")
        var mAuth: FirebaseAuth = FirebaseAuth.getInstance();
        val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        mAuth.signOut()
        PopupManager(context).hidePopup()
        var intent=Intent(context,com.quics.login.login.LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)

    }

    suspend fun UpdateUserBd(context: Context, user: User) {
        val popupManager=PopupManager(context);
        popupManager.showPopup("Actualizando Información")
        val db = FirebaseFirestore.getInstance()
        val mAuth= FirebaseAuth.getInstance();
        val uid=mAuth.currentUser?.uid.toString()


        try {
           db.collection("usuarios").document(uid)
                .update(user.toMap())
                .await()
            saveUserData(context,user)
            popupManager.hidePopup()


        } catch (exception: Exception) {
            println("Error al actualizar el documento: ${exception.message}")
            popupManager.hidePopup()
            signOut(context)
        }
    }
    suspend fun updateUser(uid: String, context: Context) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("usuarios").document(uid)
        val popupManager=PopupManager(context);
        popupManager.showPopup("Actualizando Información")
        try {
            val document = docRef.get().await() // Usamos await para esperar el resultado

            if (document.exists()) {
                document.data?.let {
                    val userActive = document.toObject(User::class.java)
                    if (userActive != null) {
                        popupManager.hidePopup()
                        saveUserData(context, userActive)
                    } else {
                        popupManager.hidePopup()
                        signOut(context)
                    }
                }
            } else {
                popupManager.hidePopup()
                signOut(context)
            }
        } catch (exception: Exception) {
            println("Error al verificar el documento: ${exception.message}")
            popupManager.hidePopup()
            signOut(context)
        }
    }

    fun UpdateDirection(context: Context, direction: DirectionData, position: Int) {
        val user = getUserData(context)
       val updateList= user?.direcciones?.toMutableList().apply {
            this?.set(position, direction)
        }

       //updateDirectionBd(context,updateList!!)
    }
    suspend fun updateDirectionBd(context: Context,direcciones:List<DirectionData>){
        val db = FirebaseFirestore.getInstance()
        val mAuth= FirebaseAuth.getInstance();
        val uid=mAuth.currentUser?.uid.toString()

        val docRef = db.collection("usuarios").document(uid);
        val popupManager=PopupManager(context);
        popupManager.showPopup("Actualizando dirección")
        try {
            docRef.update("direcciones", direcciones).await()
            popupManager.hidePopup()
        } catch (e: Exception) {
            popupManager.hidePopup()
            popupManager.showPopup("Error al actualizar dirección")
            popupManager.hidePopup()
        }

//        popupManager.showPopup("Actualizando dirección")
//        docRef.update("direcciones",direcciones).addOnSuccessListener {
//
//            popupManager.hidePopup()
//        }.addOnFailureListener {
//            popupManager.hidePopup()
//            popupManager.showPopup("Error al actualizar dirección")
//            popupManager.hidePopup()
//
//        }
    }


}