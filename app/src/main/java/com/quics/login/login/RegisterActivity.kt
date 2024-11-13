
package com.quics.login.login

import PopupManager
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.login.classes.EmailValidation
import com.login.classes.changeVIsibiltyInput
import com.login.classes.keyboardHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.login.classes.confirm
import com.login.classes.formRegister
import com.login.classes.validateInput
import com.quics.login.R
import com.quics.login.data.User
import com.quics.login.utils.UserActive
import java.util.Date
import kotlin.properties.Delegates


class RegisterActivity : AppCompatActivity() {
    private lateinit var etPassword: EditText
    private lateinit var etPasswordConfirm: EditText
    private lateinit var etEmail: EditText
    private lateinit var popupManager: PopupManager
    private lateinit var hKeyboard: keyboardHelper
    private var email by Delegates.notNull<String>()
    private var password by Delegates.notNull<String>()
    private var password_confirm by Delegates.notNull<String>()
    private lateinit var changeVisibility: changeVIsibiltyInput
    private lateinit var changeVisibilityConfirm: changeVIsibiltyInput
    private lateinit var btnRegister:Button;
    private lateinit var mAuth: FirebaseAuth
    private lateinit var validatePassword: validateInput
    private lateinit var validateEmail: validateInput
    private lateinit var validateConfirm: validateInput
    private lateinit var labelInvalidEmail:TextView;
    private lateinit var labelPassword:TextView;
    private lateinit var labelConfirm:TextView;
    private lateinit var formValidate: formRegister



    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val loginHere = findViewById<TextView>(R.id.text_login_here)
        loginHere.setOnClickListener {
            finish()
        }
        val txtTerminos:TextView=findViewById(R.id.txt_terminos_event);
        val spannableString = SpannableString(txtTerminos.text)
        spannableString.setSpan(UnderlineSpan(), 49, txtTerminos.text.length, 0)
        txtTerminos.text=spannableString
        txtTerminos.setOnClickListener{
            showTerminos();
        }
        hKeyboard=keyboardHelper(this)

        findViewById<View>(R.id.rootActivity).setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hKeyboard.hideKeyboard();
            }
            false
        }
        setBindingInputs()
    }
private fun showTerminos(){
    openActivity(this, "com.quics.login.login.PoliciesActivity")
}
    private fun setBindingInputs(){
        etPassword=findViewById(R.id.edit_text_password_register)
        etPasswordConfirm=findViewById(R.id.edit_text_password_register_confirm)
        etEmail=findViewById(R.id.edit_text_email)
        mAuth=FirebaseAuth.getInstance();
        popupManager = PopupManager(this)
        btnRegister=findViewById(R.id.button_submit_register)
        btnRegister.isEnabled=false;
        btnRegister.setBackgroundColor(ContextCompat.getColor(this, R.color.btn_disabled))
        changeVisibility= changeVIsibiltyInput(etPassword);
        changeVisibility.addEventEye();
        changeVisibilityConfirm= changeVIsibiltyInput(etPasswordConfirm);
        changeVisibilityConfirm.addEventEye();
        labelInvalidEmail=findViewById(R.id.validation_email_register);
        labelPassword=findViewById(R.id.validation_password);
        labelConfirm=findViewById(R.id.validation_confirm);
        val permissionEmail = mapOf("required" to "true","email" to "true");
        val permissionPassword = mapOf("min" to "6", "max" to "10", "required" to "true");
        validateEmail= validateInput(labelInvalidEmail,etEmail,permissionEmail,this);
        validatePassword=validateInput(labelPassword,etPassword,permissionPassword,this);
        validateConfirm= confirm(labelConfirm,etPasswordConfirm,permissionPassword,this,etPassword);
        formValidate= formRegister(arrayOf(validateEmail,validatePassword,validateConfirm),btnRegister,this);

        etPassword.doOnTextChanged { text, start, before, count ->
            validatePassword.validate();
            validateConfirm.validate();
            formValidate.validateForm();
        }
        etPasswordConfirm.doOnTextChanged { text, start, before, count ->
            validateConfirm.validate();
            formValidate.validateForm();
        }
        etEmail.doOnTextChanged { text, start, before, count ->
            validateEmail.validate();
            formValidate.validateForm();
        }
    }



    private fun goHome(){
        popupManager.hidePopup();
        Toast.makeText(this, "Registrado con éxito", Toast.LENGTH_SHORT).show()
        openActivity(this, "com.quics.login.home.HomeActivity")
//      db.collection("business").document(email).set(hashMapInit).addOnSuccessListener {e->
//              popupManager.hidePopup();
//          Toast.makeText(this, "Registrado con éxito", Toast.LENGTH_SHORT).show()
//        //  openActivity(this, "com.example.quics.HomeActivity")
//      }
//          .addOnFailureListener { e -> popupManager.hidePopup(); Toast.makeText(this, "Error writing document: ${e.message}", Toast.LENGTH_SHORT).show() }
    }
    private fun openActivity(context: Context, activityName: String) {
        // Getting a reference to the activity class based on its name
        val activityClass = Class.forName(activityName)
        // Creating an object of the Intent class
        val intent = Intent(context, activityClass)
        // Starting activity
        context.startActivity(intent)
    }
    fun register(v: View){

        popupManager.showPopup(getString(R.string.register_bussines_loader))
            mAuth.createUserWithEmailAndPassword(etEmail.text.toString(),etPassword.text.toString()).addOnCompleteListener(this){task->
                if(task.isSuccessful){
                    val collectionRef = FirebaseFirestore.getInstance().collection("usuarios")

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
                        UserActive().saveUserData(this, user)
                        goHome();
                    }
                }else{
                    val exception = task.exception
                    val msn= exception?.message
                    popupManager.hidePopup();
                    showAlert(msn.toString())

                }
            }


    }
    private fun showAlert(msg:String){
        if(msg==getString(R.string.register_email_duplicated)){
            Toast.makeText(this, getString(R.string.msg_email_duplicated), Toast.LENGTH_SHORT).show()

        }else{
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }
}