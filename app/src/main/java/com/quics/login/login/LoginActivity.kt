package com.quics.login.login
import PopupManager
import kotlinx.coroutines.tasks.await

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.login.classes.EmailValidation
import com.login.classes.changeVIsibiltyInput
import com.login.classes.formRegister
import com.login.classes.keyboardHelper
import com.login.classes.validateInput
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.quics.login.R
import kotlin.properties.Delegates
import java.util.UUID

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.quics.login.data.User
import com.quics.login.home.HomeActivity
import java.util.Date
import com.quics.login.utils.UserActive

class LoginActivity : AppCompatActivity() {
    private lateinit var etPassword:EditText;
    private lateinit var etEmail:EditText;
    private var password by Delegates.notNull<String>();
    private lateinit var btnLogin:Button;
    private lateinit var labelInvalidPassword:TextView;
    private lateinit var labelInvalidEmail:TextView;
    private lateinit var popupManager: PopupManager
    private lateinit var hKeyboard: keyboardHelper
    private lateinit var changeVisibility: changeVIsibiltyInput
    private lateinit var validatePassword: validateInput
    private lateinit var validateEmail: validateInput
    private lateinit var formValidate: formRegister
    private lateinit var Uuid:String;
    private  lateinit var googleSignInClient:GoogleSignInClient;

    //Google Auth

    companion object {
        var userEmail:String="";
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
        val providerOptions = mapOf(
            "0" to "Email_Password",
            "1" to "Google",
            "2" to "NumberPhone",
            "3" to "Facebook"
        )


    }
    private lateinit var mAuth: FirebaseAuth;



   @SuppressLint("ClickableViewAccessibility")
   override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etPassword = findViewById(R.id.edit_text_password)
        etEmail = findViewById(R.id.edit_text_email)
        mAuth=FirebaseAuth.getInstance();
       hKeyboard=keyboardHelper(this)
       hKeyboard.addEvent(findViewById(R.id.rootActivity))
       changeVisibility=changeVIsibiltyInput(etPassword)
       changeVisibility.addEventEye();
       val btnRegister = findViewById<TextView>(R.id.text_create_here)
        btnLogin=findViewById(R.id.button_submit);
        btnLogin.isEnabled=false;
        labelInvalidPassword=findViewById(R.id.validation_password);
        labelInvalidEmail=findViewById(R.id.validation_email);
       val permissionPassword = mapOf("min" to "6", "max" to "10", "required" to "true");
       val permissionEmail = mapOf("required" to "true","email" to "true");

       validatePassword=validateInput(labelInvalidPassword,etPassword,permissionPassword,this);
       validateEmail=validateInput(labelInvalidEmail,etEmail,permissionEmail,this);
       formValidate= formRegister(arrayOf(validateEmail,validatePassword),btnLogin,this);

       etPassword.doOnTextChanged { text, start, before, count ->
           validatePassword.validate();
           formValidate.validateForm();
       }
       etEmail.doOnTextChanged { text, start, before, count ->

           validateEmail.validate();
           formValidate.validateForm();

       }
        btnRegister.setOnClickListener {
            // Calling method to open specified activity
            val intent = Intent(this, RegisterActivity::class.java)
    reset()
            // Starting activity
            startActivity(intent)
        }
       val txtResetPassword:TextView=findViewById(R.id.text_forgot_password)
       txtResetPassword.setOnClickListener{
           resetPassword();
       }


        reset();
       ///pop

       popupManager = PopupManager(this)
       Uuid=UUID.randomUUID().toString();
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
           .requestIdToken(getString(R.string.default_web_client_id))
           .requestEmail()
           .build()
        googleSignInClient= GoogleSignIn.getClient(this, gso)


   }

    private fun reset(){
        etPassword.text.clear();
        etEmail.text.clear();
    }
    fun fnLoginGoogle(v:View){
            loginWithGoogle()
    }
    private fun loginWithGoogle(){
        popupManager.showPopup(getString(R.string.login_loader))
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                popupManager.hidePopup()

            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    verifyUser();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    //updateUI(null)
                    popupManager.hidePopup()

                }
            }
    }
    private fun resetPassword(){
        userEmail=etEmail.text.toString();

        if(userEmail.isEmpty()||!EmailValidation.isEmail(userEmail)){
            showAlert(getString(R.string.msg_error_email_invalid))
        }else{
            popupManager.showPopup(getString(R.string.reset_password_loader))
            mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener { it->
                if(it.isSuccessful){
                        showAlert(getString(R.string.email_send_reset))
                        popupManager.hidePopup()
                    } else{

                        showAlert(getString(R.string.send_email_reset_password_fail))
                        popupManager.hidePopup()

                    }
                }




        }
    }
    public override fun onStart(){
        super.onStart()
        var currentUser=mAuth.currentUser
        reset();
        if(currentUser!=null){
           // mAuth.signOut()
         goHome();
        }

    }
    fun documentExists(collectionName: String, uid: String, callback: (Boolean) -> Unit) {
        // Referencia al documento en la colección especificada y UID
        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection(collectionName).document(uid)

        // Intenta obtener el documento
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    document.data?.let {
                        val userActive = document.toObject(User::class.java)
                        if (userActive != null) {
                            UserActive().saveUserData(this,userActive)
                            callback(true)  // El documento existe
                        }else{
                            callback(false) //no podemos parsear el document
                        }




                    }

                } else {
                    callback(false) // El documento no existe
                }
            }
            .addOnFailureListener { exception ->
                println("Error al verificar el documento: ${exception.message}")
                callback(false) // Maneja el error como no existente si hay un fallo
            }
    }


    private fun verifyUser() {
        val collectionRef = FirebaseFirestore.getInstance().collection("usuarios")
        val uid = mAuth.currentUser?.uid.toString()

        documentExists("usuarios", uid) { exists ->
            if (exists) {
                Log.w(TAG, "Usuario existe")

                goHome();

            } else {
                Log.w(TAG, "Usuario no existe")

                try {
                    mAuth.currentUser?.let {
                        val user = User(
                            "Invitado",
                            emptyList(),
                            emptyList(),
                            it.email.toString(),
                            Date(),
                            "Invitado",
                            it.uid,
                            emptyList()
                        )
                        collectionRef.document(it.uid).set(user);
                        UserActive().saveUserData(this,user)
                        goHome();
                    }
                }catch (e: Exception) {
                    popupManager.hidePopup()
                    Toast.makeText(this, getString(R.string.error_create_user_first), Toast.LENGTH_SHORT).show();
                }
            }
        }



    }


    private fun goHome(){



        reset();

        //openActivity(this, "com.example.quics.HomeActivity");
        ///revisar mi tabla de roles TODO
        //funcion consultar si el usuario esta en la collecion users por medio de su uid



        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        // Starting activity
        startActivity(intent)
        popupManager.hidePopup()

    }

private fun showAlert(msg:String){
    if(msg==getString(R.string.response_firebase_credential_invalid_email_and_password)){
        Toast.makeText(this, getString(R.string.show_user_response_invalid_login), Toast.LENGTH_SHORT).show();
    }else{
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

    fun fnLogin(v: View){

        password=etPassword.text.toString();
        userEmail=etEmail.text.toString();
         popupManager.showPopup(getString(R.string.login_loader))
            btnLogin.isEnabled=false;
            mAuth.signInWithEmailAndPassword(userEmail,password).addOnCompleteListener(this){ task->
                if(task.isSuccessful){
                    verifyUser();

                }else{
                    val exception = task.exception
                    val msn= exception?.message
                   showAlert(msn.toString())
                    popupManager.hidePopup()
                    etPassword.text.clear();


                }
        }



    }

}