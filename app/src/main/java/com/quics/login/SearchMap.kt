package com.quics.login

import PopupManager
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.quics.login.classes.serviceGps
import com.quics.login.data.DirectionData
import com.quics.login.databinding.ActivitySearchMapBinding
import com.quics.login.home.HomeActivity
import com.quics.login.home.HomeActivity.Companion.REQUIRED_PERMISSIONS_GPS
import com.quics.login.home.HomeActivity.Companion.fusedLocation
import com.quics.login.includes.MyToolbar;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.quics.login.maps.UbicationActivity
import java.util.Locale
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken

import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.quics.login.data.User
import com.quics.login.maps.MyDirections
import com.quics.login.utils.UserActive
import kotlinx.coroutines.launch

class SearchMap : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivitySearchMapBinding
    companion object{
        val LOCATION_REQUEST_CODE=1;
        val SETTING_REQUEST_CODE=2;
        private const val GoogleDireccion = "GoogleMapsDireccion"
          val  AUTOCOMPLETE_REQUEST_CODE=3;
    }
private lateinit var title:TextView
    private lateinit var toolbar:androidx.appcompat.widget.Toolbar;
private lateinit var serviceGPS:serviceGps;
    private lateinit var locationRequest:LocationRequest;
    private lateinit var fusedLocationClient:FusedLocationProviderClient;
    private lateinit var previosLocation:LatLng;
    private var userLocation: LatLng? = null
    private lateinit var text_direccion:TextView;
private lateinit var btn_save_direction:Button;
    private lateinit var popupManager: PopupManager
    private var saveDirectionFlag:Boolean=false; //esta bandera es para saber si se guardo la direccion
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {


                Log.d("LocationCallback", "*****************************")

                locationResult.lastLocation?.let { location ->
                    // Aquí recibes la nueva ubicación
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d("LocationCallback", "Latitud: $latitude, Longitud: $longitude")
                    setMyLocation(latitude,longitude);
                    // Usar la ubicación, por ejemplo, mostrarla en un mapa o actualizar la UI


                }
            }

    }
    @SuppressLint("MissingPermission")
    private fun setMyLocation(lat:Double, long:Double){
        val quics = LatLng(lat, long)
        mMap.isMyLocationEnabled = true
        userLocation = LatLng(lat,long)
        mMap.clear()
        obtenerDireccionDesdeLatLng(quics);
        mMap.addMarker(MarkerOptions().position(quics).title("Home").draggable(true).icon(
            BitmapDescriptorFactory.fromResource(R.drawable.home)))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(quics))
        val cameraPosition = CameraPosition.Builder()
            .target(quics)      // La ubicación deseada
            .zoom(18f)             // Nivel de zoom
            .bearing(180f)          // Orientación de la cámara hacia el este
            .tilt(30f)             // Ángulo de la cámara hacia abajo
            .build()

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        popupManager = PopupManager(this)
        binding = ActivitySearchMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        serviceGPS=serviceGps(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val intentHome=Intent(this, MyDirections::class.java);
        text_direccion=findViewById(R.id.text_direccion);
        toolbar=findViewById(R.id.toolbar);
        title=findViewById(R.id.toolbar_title);
        title.text="Mi Dirección"
        toolbar.setNavigationOnClickListener {
            // Finishing current activity
            startActivity(intentHome);
        }

        btn_save_direction=findViewById(R.id.saveDirection);
        btn_save_direction.setOnClickListener {
            showTextInputDialog(this);

        }
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_api_key_maps));
        }

        // Configura el intent para el Autocomplete
//        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
//
//
//        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
//            .setCountry("MX") // Establecer el código de país para México
//            .build(this)
//
//        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        val autocompleteFragment = AutocompleteSupportFragment()
        autocompleteFragment.setCountry("MX");
        supportFragmentManager.beginTransaction()
            .replace(R.id.autocomplete_fragment_container, autocompleteFragment)
            .commit()


        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // Manejar selección de lugar
                val name = place.name
                val latLng = place.latLng
                Log.i("Places", "Lugar seleccionado: $name, $latLng")
                setMyLocation(latLng!!.latitude, latLng.longitude)
            }

            override fun onError(status: com.google.android.gms.common.api.Status) {
                // Manejar error
                Log.e("Places", "Error: ${status.statusMessage}")
            }
        })


    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        mMap.uiSettings.isZoomControlsEnabled=true;
        locationRequest=LocationRequest();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setSmallestDisplacement(5F)
        mMap.uiSettings.isMyLocationButtonEnabled = true




        //boton para redirigir a mi ubicacion cuando mueva el marker de posicion regrese a mi ubicacion



        // mMap.isMyLocationEnabled=true;
        // Add a marker in Sydney and move the camera
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        googleMap.setOnMyLocationButtonClickListener {
            initGps()
            true
        }
        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {
                // Evento cuando el marcador comienza a moverse
                //borrar la ultima posicion del marcador




            }

            override fun onMarkerDrag(marker: Marker) {
                // Evento durante el arrastre del marcador (opcional)
            }

            override fun onMarkerDragEnd(marker: Marker) {
                // Evento cuando el marcador termina de moverse
                //funcion para verificar si se pudo obtener la nueva posicion

                if(serviceGPS.gpsActive()){
                    if(marker.position!=previosLocation){

                        val nuevaPosicion = marker.position

                        mMap.animateCamera(CameraUpdateFactory.newLatLng(nuevaPosicion));
                        obtenerDireccionDesdeLatLng(nuevaPosicion);
                    }else {
                        Toast.makeText(this@SearchMap, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
                        initGps()

                    }
                }else{
                    showAlertDialogNOGPS()
                }







            }

            })

    }
    public override fun onStart(){
        super.onStart()
       initGps();
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    Log.i("Places", "Lugar seleccionado: ${place.name}, ${place.latLng}")
                    setMyLocation(place.latLng!!.latitude, place.latLng!!.longitude)
                }
                AutocompleteActivity.RESULT_ERROR -> {

                    Log.e("Places", "Error: ${Autocomplete.getStatusFromIntent(data!!).statusMessage}")
                }
                Activity.RESULT_CANCELED -> {
                    // El usuario canceló la selección.
                }
            }
        }
    }
    fun showTextInputDialog(context: Context) {
        // Crear un EditText para la entrada de texto
        val input = EditText(context).apply {
            hint = "Casa, Trabajo, etc." // Hint del EditText
            setHintTextColor(resources.getColor(R.color.btn_disabled)) // Color del hint blanco
            setTextColor(Color.WHITE)

        }
        if(intent.hasExtra("lat")) { ///camino update
            input.text=Editable.Factory.getInstance().newEditable(intent.getStringExtra("name").toString())
        }
            disabledButton();
        // Crear un layout para el diálogo
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 20) // Agrega algo de margen
            addView(input)
        }

        val title = TextView(context).apply {
            text = "¿Cómo quieres identificar esta dirección?"
            textSize = 20f
            setTextColor(resources.getColor(R.color.white))
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 20)
        }
        // Configurar el AlertDialog
        val dialog = AlertDialog.Builder(context)
            .setCustomTitle(title) //


            .setView(layout)
            .setPositiveButton("Listo") { dialogInterface, _ ->
                var userInput = input.text.toString()
                saveDirectionFlag=true;
                if(userInput.isEmpty()){
                   userInput="Casa";
                }


                saveDireccion(userInput);


            }
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.setOnShowListener {
            // Fondo negro
            dialog.window?.setBackgroundDrawable(ColorDrawable(getColor(R.color.primary)))
            dialog.findViewById<TextView>(android.R.id.message)?.setTextColor(Color.WHITE)
            dialog.findViewById<TextView>(context.resources.getIdentifier("alertTitle", "id", context.packageName))?.setTextColor(Color.WHITE)
            // Botón positivo (Aceptar) en color naranja
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.apply {
                setTextColor(Color.parseColor("#FFA500")) // Naranja
            }

            // Botón negativo (Cancelar) en color naranja
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.apply {
                setTextColor(Color.parseColor("#FFA500")) // Naranja
            }
        }
        dialog.setOnDismissListener {
            if(!saveDirectionFlag){
                enabledButton();
            }

            // Realiza cualquier acción que necesites al cerrar el diálogo
        }

        dialog.show()
    }
    private fun saveDireccion(name:String){
        popupManager=PopupManager(this)
        popupManager.showPopup("Guardando dirección")


        val mAuth= FirebaseAuth.getInstance();
        val uid=mAuth.currentUser?.uid.toString()
        var default=false;

        if(intent.hasExtra("lat")) { ///camino update
            popupManager.hidePopup()
            default=intent.getBooleanExtra("default",false);
            val direccion=
                DirectionData(previosLocation.latitude,previosLocation.longitude,text_direccion.text.toString(),default,name);
            lifecycleScope.launch {

                var user = UserActive().getUserData(this@SearchMap);
                var direcciones = user?.direcciones as ArrayList<DirectionData>;
                direcciones[intent.getIntExtra("position", 0)] = direccion
                UserActive().updateDirectionBd(this@SearchMap, direcciones).let {
                    UserActive().updateUser(uid, this@SearchMap).let {
//
                        val intent = Intent(this@SearchMap, MyDirections::class.java)
                        startActivity(intent)
                    }

                }


            }




        }else{

            val direccion=
                DirectionData(previosLocation.latitude,previosLocation.longitude,text_direccion.text.toString(),default,name);
            val db = FirebaseFirestore.getInstance()


            val docRef = db.collection("usuarios").document(uid);


            docRef.update("direcciones", FieldValue.arrayUnion(direccion))
                .addOnSuccessListener {

                    lifecycleScope.launch {
                        popupManager.hidePopup()
                        UserActive().updateUser(uid, this@SearchMap)

                        val intent = Intent(this@SearchMap, MyDirections::class.java)
                        startActivity(intent)
                    }


                }
                .addOnFailureListener { exception ->
                    Log.d(GoogleDireccion, "Error al guardar dirección ${uid} error  ${exception.message}")
                    popupManager.hidePopup()
                    Toast.makeText(this, "Error al guardar dirección", Toast.LENGTH_SHORT).show()
                    enabledButton();
                    saveDirectionFlag=false;
                }
        }






    }

    private fun initGps(){
        serviceGPS.checkLocationpermissions();
      //  startLocation();
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { // ID del botón de retroceso
                // Aquí puedes realizar la acción que desees al presionar hacia atrás.
                finish() // Cierra la actividad actual.
                true // Indica que has manejado la acción.
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("onRequestPermissionsResult", "request code == ${requestCode}")
        when(requestCode){


                    LOCATION_REQUEST_CODE->{

                if(grantResults.size>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Log.d("onRequestPermissionsResult", "segundo fine")

                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        Log.d("onRequestPermissionsResult", "INICIA LOOPER ${serviceGPS.gpsActive()}")
                        startLocation();

                    }
                }else{
                    // Permiso denegado, verificar si el usuario seleccionó "No volver a preguntar"
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // El usuario seleccionó "No volver a preguntar", mostrar mensaje o redirigir a configuración
                        showSettingsRedirectDialog()

                    } else {
                        // El usuario simplemente denegó el permiso, mostrar mensaje
                        permissionGpsNative()
                    }
                }
            }
            SETTING_REQUEST_CODE->{
                startLocation();
            }
        }

    }

    private fun disabledButton(){
        btn_save_direction.isEnabled=false;
        btn_save_direction.backgroundTintList=ContextCompat.getColorStateList(this, R.color.btn_disabled)
    }
    private fun enabledButton(){
        btn_save_direction.isEnabled=true;
        btn_save_direction.backgroundTintList=ContextCompat.getColorStateList(this, R.color.primary)

    }
    // Función para obtener la dirección desde las coordenadas
    private fun obtenerDireccionDesdeLatLng(latLng: LatLng) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            // Obtener la dirección desde las coordenadas
            val direcciones: List<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (direcciones != null && direcciones.isNotEmpty()) {


                val direccion: Address = direcciones[0]
                val direccionCompleta = direccion.getAddressLine(0)  // Dirección completa (calle, número, etc.)
                // Mostrar la dirección o hacer algo con ella
                //MyToolbar.setTitle(this,direccionCompleta)
                text_direccion.text=direccionCompleta;
                previosLocation=latLng;
                println("Dirección: $direccionCompleta")
            } else {
                noInternet();
                Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()

            }
        } catch (e: Exception) {

            noInternet();
            e.printStackTrace()
        }
    }
    private fun startLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(serviceGPS.gpsActive()){


                obtenerUbicacion();
                //fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
            }else{
                showAlertDialogNOGPS()
            }

        }


    }
    @SuppressLint("MissingPermission")
    private fun obtenerUbicacion() {

        if(serviceGPS.isInternetAvailable()){
            ///save o update location
            if(intent.hasExtra("lat")){ ///camino update
                val lat = intent.getDoubleExtra("lat", 0.0)
                val long= intent.getDoubleExtra("long", 0.0)
                setMyLocation(lat,long);


            }else{
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        // Procesar la ubicación
                        val latitud = location.latitude
                        val longitud = location.longitude
                        setMyLocation(latitud,longitud);

                    } else {
                        noInternet();
                        Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Error al obtener la ubicación", Toast.LENGTH_SHORT).show()
                }
            }

        }else{
          noInternet();

        }


    }
    private fun noInternet(){
        var intent=Intent(this,NoInternetActivity::class.java);
        startActivity(intent);
    }
    private fun showSettingsRedirectDialog() {

        var intent=Intent(this,NoPermissionsActivity::class.java);
        startActivity(intent);
//        AlertDialog.Builder(this)
//            .setTitle("Permiso requerido")
//            .setMessage("Para continuar usando la aplicación, habilita los permisos de ubicación desde la configuración de la app.")
//            .setPositiveButton("Ir a configuración") { _, _ ->
//                // Redirigir al usuario a la configuración de la aplicación
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                val uri = Uri.fromParts("package", packageName, null)
//                intent.data = uri
//                startActivity(intent)
//            }
//            .setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialogInterface, i ->finish()})
//            .setOnCancelListener {
//                finish()  // Cerrar la actividad si se toca fuera del diálogo
//            }
//            .show()
    }
    fun showAlertDialogNOGPS(){

        val intent =Intent(this,UbicationActivity::class.java);
        startActivity(intent)
    }
    private fun allPermissionsGrantedGPS()= REQUIRED_PERMISSIONS_GPS.all{
        ContextCompat.checkSelfPermission(baseContext,it)==PackageManager.PERMISSION_GRANTED
    }
    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permiso concedido
            if(allPermissionsGrantedGPS()){
                startLocation();
            }else{
                permissionGpsNative()
            }

        } else {
            // Permiso denegado
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                // El usuario no marcó "No volver a preguntar", mostrar un diálogo explicativo
                permissionGpsNative()
            } else {
                // El usuario marcó "No volver a preguntar", redirigir a configuración
                showSettingsRedirectDialog()
            }
        }
    }


    fun permissionGpsNative(){
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MyDirections::class.java)

//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }






}