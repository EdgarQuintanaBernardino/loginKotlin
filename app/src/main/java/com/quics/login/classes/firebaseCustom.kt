package com.quics.login.classes


import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.quics.login.R

class firebaseCustom(ctx:Context) {
    private val TAG = "GoogleActivity"
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance();
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(ctx.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    var  googleSignInClient: GoogleSignInClient= GoogleSignIn.getClient(ctx, gso)


    fun checkIfEmailExists(email: String):Boolean {
        println(email)
        var response=false;
        mAuth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods

                    response=signInMethods.isNullOrEmpty();
                } else {
                    // Manejo de errores
                    println("Error al verificar el correo electrónico: ${task.exception?.message}")
                    response=false
                }
            }
        return response;
    }
}