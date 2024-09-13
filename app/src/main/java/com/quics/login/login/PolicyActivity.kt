package com.quics.login.login

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.quics.login.R
import com.google.firebase.firestore.FirebaseFirestore
import com.quics.login.models.DaoPolicy


class PolicyActivity : AppCompatActivity() {
    private lateinit var header: TextView;
    private lateinit var version: TextView;
    private lateinit var point_1: TextView;
    private lateinit var point_1_details: TextView;
    private lateinit var point_2: TextView;
    private lateinit var point_2_details: TextView;
    private lateinit var loader:ProgressBar;

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy)

        // Getting toolbar by its id
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val option=intent.getIntExtra("id",0);
        header=findViewById(R.id.header);
        version=findViewById(R.id.text_last_updated_on);
        point_1=findViewById(R.id.point_1);
        point_1_details=findViewById(R.id.point_1_details);
        point_2=findViewById(R.id.point_2);
        point_2_details=findViewById(R.id.point_2_details);
    loader=findViewById(R.id.progressBar);
        when(option){
            1->terms();
            2->dataPrivacy();
            3->cancelReturns();
            4->helpEmail();
        }
        // Setting the Toolbar to act as the ActionBar
        // setSupportActionBar(toolbar)

        // Setting click listener
        toolbar.setNavigationOnClickListener {
            // Finishing current activity
            finish()
        }
    }
    private fun terms(){
        header.text=this.getString(R.string.terms_conditions);
        getData("terminos_condiciones");
    }
    private fun dataPrivacy(){
        header.text=this.getString(R.string.data_privacy);
        getData("politicas");
    }
    private fun cancelReturns(){
        header.text=this.getString(R.string.cancel_returns);
        getData("cancelaciones");
    }
    private fun helpEmail(){
        header.text=this.getString(R.string.email_help);
        getData("help_email");
    }
    private fun getData(docu:String){
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("terminos").document(docu);
        collectionRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Acceder a los datos del documento
                    val data= document.toObject(DaoPolicy::class.java)

                    //  setData(data as DaoPolicy)
                    if (data != null) {
                        setData(data)
                    }
                    // Mostrar en el logcat (puedes hacer algo con los datos aquí)
                    Log.d("Firestore", "Data: $data")
                } else {
                    Log.d("Firestore", "No se encontró el documento")
                    loader.visibility= View.INVISIBLE;
                    finish()
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error al obtener el documento", exception)
                loader.visibility= View.INVISIBLE;
                finish()
            }

    }
    private fun setData(data:DaoPolicy){
        version.text=data.version;
        point_1.text=data.point_1;
        point_1_details.text=data.text_1;
        point_2.text=data.point_2;
        point_2_details.text=data.text_2.replace("\\n","\n");
        loader.visibility= View.INVISIBLE;

    }
}