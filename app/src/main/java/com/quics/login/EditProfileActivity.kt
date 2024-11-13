package com.quics.login

import PopupManager
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.login.classes.keyboardHelper
import com.quics.login.adapters.ShippingAddressesAdapter
import com.quics.login.classes.DateFormat
import com.quics.login.data.DirectionData
import com.quics.login.data.User
import com.quics.login.models.AddressItemModel
import com.quics.login.utils.UserActive
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import kotlin.properties.Delegates
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlin.math.log
import com.bumptech.glide.Glide

class EditProfileActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
   private lateinit var text_customer_name: TextView;
    private lateinit var text_customer_email: TextView;
    private lateinit var monthPicker: NumberPicker;
    private lateinit var yearPicker: NumberPicker;
    private lateinit var dayPicker: NumberPicker;
    private lateinit var userActive: UserActive;
    private lateinit var submit:Button;
    private var SelectedDay by Delegates.notNull<Int>();
    private var SelectedMonth by Delegates.notNull<Int>();
    private var SelectectYear by Delegates.notNull<Int>();
    private lateinit var hKeyboard: keyboardHelper
    private lateinit var edit_text_name:TextView;
    private lateinit var edit_text_phone:TextView;
    private lateinit var image_profile:ImageView;
    private lateinit var image_camera:ImageView;
    private val IMAGE_REQUEST_CODE = 1000
private val IMAGE_PERMISSION_CODE = 1001
    private lateinit var popManager:PopupManager;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        // Getting toolbar by its id
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        // Setting the Toolbar to act as the ActionBar
         //setSupportActionBar(toolbar)
        // Setting click listener
        text_customer_name =findViewById(R.id.text_customer_name)
        text_customer_email=findViewById(R.id.text_customer_email)
        edit_text_phone=findViewById(R.id.edit_text_phone)
        edit_text_name=findViewById(R.id.edit_text_name)
        userActive=UserActive()


        toolbar.setNavigationOnClickListener {
        // Finishing current activity

            finish()
        }
        popManager=PopupManager(this);
        hKeyboard= keyboardHelper(this)
        hKeyboard.addEvent(findViewById(R.id.allDisplay))
        monthPicker = findViewById(R.id.spinner_months)
        dayPicker = findViewById(R.id.spinner_days)
        yearPicker = findViewById(R.id.spinner_years)

        // Configura el rango de valores para los meses (0-11)
        val months = arrayOf(
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        )
        val day= arrayOf(
            "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "10", "11", "12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"
        )
        val years= arrayOf(
            "1940", "1941", "1942", "1943", "1944", "1945",
            "1946", "1947", "1948", "1949",
            "1950", "1951", "1952", "1953", "1954", "1955",
            "1956", "1957", "1958", "1959",
            "1960", "1961", "1962", "1963", "1964", "1965",
            "1966", "1967", "1968", "1969", "1970", "1971","1972","1973","1974","1975","1976","1977","1978","1979","1980", "1981", "1982", "1983", "1984", "1985",
            "1986", "1987", "1988", "1989",
            "1990", "1991", "1992", "1993", "1994", "1995",
            "1996", "1997", "1998", "1999", "2000", "2001","2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013","2014","2015","2016","2017","2018","2019","2020"
        )


        monthPicker.minValue = 0
        monthPicker.maxValue = months.size - 1
        monthPicker.displayedValues = months

        monthPicker.wrapSelectorWheel = true // Habilita el desplazamiento circular

        // Configura un listener para capturar el mes seleccionado
        monthPicker.setOnValueChangedListener { _, _, newVal ->
            SelectedMonth = newVal

        }
        dayPicker.minValue = 0
        dayPicker.maxValue = day.size - 1
        dayPicker.displayedValues = day
       dayPicker.value=27;
        dayPicker.wrapSelectorWheel = true // Habilita el desplazamiento circular

        // Configura un listener para capturar el día seleccionado
        dayPicker.setOnValueChangedListener { _, _, newVal ->
             SelectedDay = day[newVal].toInt()


        }
        yearPicker.minValue = 0
        yearPicker.maxValue = years.size - 1
        yearPicker.displayedValues = years
        yearPicker.value=49;
        yearPicker.wrapSelectorWheel = true // Habilita el desplazamiento circular

        // Configura un listener para capturar el año seleccionado
        yearPicker.setOnValueChangedListener { _, _, newVal ->
             SelectectYear = years[newVal].toInt()


    }
        submit=findViewById(R.id.button_submit);
        submit.setOnClickListener {
                savePerfil();
        }
        image_profile=findViewById(R.id.image_customer);
        image_profile.setOnClickListener {
            requestPermission();
        }
        userActive.getUserData(this).let {
            text_customer_name.text=it?.nombre
            text_customer_email.text=it?.email
            if((it?.telefonos?.size ?: 0) > 0){
                edit_text_phone.text= it?.telefonos?.get(0).toString();
            }
            if(it?.foto != "Invitado"){
                Glide.with(this) // Usa `this` o el contexto adecuado, como `requireContext()` en un Fragment
                    .load(it?.foto) // Carga la imagen
                    .placeholder(R.drawable.ic_person) // Imagen mientras se carga (opcional)
                    .error(R.drawable.ic_person) // Imagen en caso de error (opcional)
                    .into(image_profile) // Coloca la imagen en el ImageView

            }
            val fecha=DateFormat(it?.fnacimiento as Date);
            SelectedDay=fecha.getDay();
            SelectedMonth=fecha.getMonth();
            SelectectYear=fecha.getYear();

            ///visualmente

            monthPicker.value=months.indexOf(months[SelectedMonth]);
            dayPicker.value=SelectedDay-1;
            yearPicker.value=years.indexOf(SelectectYear.toString());

            edit_text_name.text=it?.nombre


        }
        image_camera=findViewById(R.id.image_camera);

        image_camera.setOnClickListener {
            requestPermission();

        }

    }
    fun openGallery(){
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            if (imageUri != null) {

                deleteImagesAndExecuteCallback({ uploadImageToFirebase(imageUri) })
            }
        }
    }



    private fun uploadImageToFirebase(imageUri: Uri) {
        // Obtén una referencia al almacenamiento de Firebase
Log.d("Firebase", "Subiendo imagen")
        popManager.showPopup("Subiendo imagen")

        userActive.getUserData(this).let {
            // Elimina el espacio después de "/${it?.uid}/"
            Log.d("Firebase", "Subiendo imagen ${it?.uid}")
            Log.d("Firebase",  "profiles/${it?.uid}/${System.currentTimeMillis()}.jpg")


            val storageRef = FirebaseStorage.getInstance().reference.child("profiles/${it?.uid}/${System.currentTimeMillis()}.jpg")

            // Sube el archivo a Firebase
            storageRef.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    // Obtén la URL de descarga de la imagen subida
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        // Aquí puedes almacenar la URL en Firestore o en Realtime Database si deseas
                        Log.d("Firebase", "Imagen subida correctamente: $downloadUrl")
                        it?.foto = downloadUrl;
                        image_profile.setImageURI(imageUri);
                        lifecycleScope.launch {
                            userActive.UpdateUserBd(this@EditProfileActivity, it as User)
                            Toast.makeText(
                                this@EditProfileActivity,
                                "Imagen guardada",
                                Toast.LENGTH_LONG
                            ).show();
                            popManager.hidePopup()
                        }
                    }
                        .addOnFailureListener { exception ->
                            // Manejo de errores
                            Log.e("Firebase", "Error al subir imagen", exception)
                            Toast.makeText(
                                this@EditProfileActivity,
                                "Error al subir imagen",
                                Toast.LENGTH_LONG
                            ).show();
                            popManager.hidePopup()
                        }
                }

        }
    }

    private fun deleteImagesAndExecuteCallback(onComplete: () -> Unit) {

        popManager.showPopup("Eliminando imagen pasada")
        val folderRef = FirebaseStorage.getInstance().reference.child("profiles/${userActive.getUserData(this)?.uid}")

        folderRef.listAll()
            .addOnSuccessListener { listResult ->
                // Verifica si hay archivos en la carpeta
                if (listResult.items.isEmpty()) {
                    Log.d("Firebase", "No hay archivos en la carpeta para eliminar.")
                    onComplete() // Ejecuta la función de callback inmediatamente si no hay archivos
                    return@addOnSuccessListener
                }

                // Contador para rastrear eliminaciones completadas
                var deletionCount = 0
                val totalFiles = listResult.items.size

                // Itera sobre cada archivo en la carpeta
                listResult.items.forEach { fileRef ->
                    fileRef.delete()
                        .addOnSuccessListener {
                            Log.d("Firebase", "Archivo eliminado: ${fileRef.name}")
                            deletionCount++

                            // Verifica si todos los archivos se han eliminado
                            if (deletionCount == totalFiles) {
                                onComplete() // Llama a la función de callback después de eliminar todos los archivos
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Firebase", "Error al eliminar archivo: ${fileRef.name}", exception)
                            popManager.hidePopup()
                            Toast.makeText(this, "Error al eliminar imagen particular", Toast.LENGTH_LONG).show()
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error al listar archivos de la carpeta", exception)
                popManager.hidePopup()
                Toast.makeText(this, "Error al eliminar imagen general", Toast.LENGTH_LONG).show()
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == IMAGE_PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else {
            // Maneja el caso de permiso denegado
            Toast.makeText(this, "Permiso denegado para acceder a imágenes", Toast.LENGTH_SHORT).show()
        }
    }
    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Para Android 13 y superiores
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), IMAGE_PERMISSION_CODE)
            } else {
                // Abre la galería
                openGallery()
            }
        } else {
            // Para versiones anteriores a Android 13
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), IMAGE_PERMISSION_CODE)
            } else {
                // Abre la galería
                openGallery()
            }
        }
    }
    fun savePerfil(){
        if(edit_text_name.text.isEmpty()||edit_text_name.text.length<5){
            if (edit_text_name.text.length<5){
                Toast.makeText(this,"El nombre debe tener al menos 5 caracteres",Toast.LENGTH_LONG).show();
            }else
            {
                Toast.makeText(this,"El nombre es requerido",Toast.LENGTH_LONG).show();
            }

            return;
        }
        if(edit_text_phone.text.isEmpty()||edit_text_phone.text.length<10){
            if (edit_text_phone.text.length<10){
                Toast.makeText(this,"El teléfono debe tener 10 dígitos",Toast.LENGTH_LONG).show();
            }else
            {
                Toast.makeText(this,"El teléfono es requerido",Toast.LENGTH_LONG).show();

            }
            return;
        }

        var nUser= UserActive();
        nUser.getUserData(this).let {
            var user= User();
            user.uid=it?.uid.toString();
            user.email=it?.email.toString();
            user.nombre=edit_text_name.text.toString();
            val calendar = Calendar.getInstance()
            calendar.set(SelectectYear,SelectedMonth,SelectedDay)
            user.fnacimiento=calendar.time;
            user.foto=it?.foto.toString();
            user.direcciones=it?.direcciones as List<DirectionData>;
            user.telefonos= listOf(edit_text_phone.text.toString());
            user.metodos_pago= it.metodos_pago as List<Any>;

            if (it != null) {
                lifecycleScope.launch {
                    nUser.UpdateUserBd(this@EditProfileActivity, user)
                    Toast.makeText(this@EditProfileActivity,"Perfil guardado",Toast.LENGTH_LONG).show();
                    finish()
                }

            };
        }




    }

        // Calling method to initialize days spinner
//        initDaysSpinner()
//
//        // Calling method to initialize months spinner
//        initMonthsSpinner()
//
//        // Calling method to initialize years spinner
//        initYearsSpinner()
       // initShippingAddressesRecyclerView();
    }


    // Method to initialize days spinner
//    private fun initDaysSpinner(){
//        // Getting spinner by id
//        val daysSpinner: Spinner = findViewById(R.id.spinner_days)
//
//        // Creating an ArrayAdapter using the string array and a custom spinner layout
//        ArrayAdapter.createFromResource(
//            this,
//            R.array.edit_profile_days_array,
//            R.layout.custom_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            daysSpinner.adapter = adapter
//        }
//    }
//
//    // Method to initialize months spinner
//    private fun initMonthsSpinner(){
//        // Getting spinner by id
//        val monthsSpinner: Spinner = findViewById(R.id.spinner_months)
//
//        // Creating an ArrayAdapter using the string array and a custom spinner layout
//        ArrayAdapter.createFromResource(
//            this,
//            R.array.edit_profile_months_array,
//            R.layout.custom_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            monthsSpinner.adapter = adapter
//        }
//    }

    // Method to initialize years spinner
//    private fun initYearsSpinner(){
//        // Getting spinner by id
//        val yearsSpinner: Spinner = findViewById(R.id.spinner_years)
//
//        // Creating an ArrayAdapter using the string array and a custom spinner layout
//        ArrayAdapter.createFromResource(
//            this,
//            R.array.edit_profile_years_array,
//            R.layout.custom_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            yearsSpinner.adapter = adapter
//        }
//    }
