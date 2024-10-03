package com.quics.login

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.quics.login.classes.serviceGps
import com.quics.login.databinding.ActivitySearchMapBinding
import com.quics.login.home.HomeActivity.Companion.REQUIRED_PERMISSIONS_GPS
import com.quics.login.home.HomeActivity.Companion.fusedLocation
import com.quics.login.includes.MyToolbar;
class SearchMap : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivitySearchMapBinding
    companion object{
        val LOCATION_REQUEST_CODE=1;
        val SETTING_REQUEST_CODE=2;
    }
    private lateinit var locationRequest:LocationRequest;
    private lateinit var fusedLocationClient:FusedLocationProviderClient;

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {


                Log.d("LocationCallback", "*****************************")

                locationResult.lastLocation?.let { location ->
                    // Aquí recibes la nueva ubicación
                    val latitude = location.latitude
                    val longitude = location.longitude
                    // Usar la ubicación, por ejemplo, mostrarla en un mapa o actualizar la UI
                    Log.d("LocationCallback", "Latitud: $latitude, Longitud: $longitude")
                    val quics = LatLng(latitude, longitude)
                    mMap.addMarker(MarkerOptions().position(quics).title("Callback"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(quics))
                    val cameraPosition = CameraPosition.Builder()
                        .target(quics)      // La ubicación deseada
                        .zoom(20f)             // Nivel de zoom
                        .bearing(180f)          // Orientación de la cámara hacia el este
                        .tilt(30f)             // Ángulo de la cámara hacia abajo
                        .build()

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                }
            }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        MyToolbar.show(this,getString(R.string.header_maps),true);

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
        // Add a marker in Sydney and move the camera
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)




    }
    public override fun onStart(){
        super.onStart()
       initGps();
    }

    private fun initGps(){
        checkLocationpermissions();
      //  startLocation();
    }
    fun checkLocationpermissions(){
        Log.d("PermissionsGPS", "PIDE PERMISO")
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
            !=PackageManager.PERMISSION_GRANTED && ActivityCompat.shouldShowRequestPermissionRationale
                (this,Manifest.permission.ACCESS_FINE_LOCATION)&&gpsActive()) {
            Log.d("PermissionsGPS", "NO TIENE PERMISO")

            AlertDialog.Builder(this)
                .setTitle(getString(R.string.header_maps))
                .setMessage(getString(R.string.message_permission_gps))
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                    ActivityCompat.requestPermissions(
                        this,  // Aquí va tu Activity o Fragment
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),  // Array con los permisos
                        LOCATION_REQUEST_CODE  // El código que manejará la respuesta
                    )
                })
                .create()
                .show()

        }else{
            Log.d("PermissionsGPS", "ELSE DE PERMISSOS")

            ActivityCompat.requestPermissions(
                this,  // Aquí va tu Activity o Fragment
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),  // Array con los permisos
                LOCATION_REQUEST_CODE  // El código que manejará la respuesta
            )
        }
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
                        Log.d("onRequestPermissionsResult", "INICIA LOOPER ${gpsActive()}")
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
    private fun startLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(gpsActive()){
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
            }else{
                showAlertDialogNOGPS()
            }

        }
//        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
//            !=PackageManager.PERMISSION_GRANTED && ActivityCompat.shouldShowRequestPermissionRationale
//                (this,Manifest.permission.ACCESS_FINE_LOCATION)) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//                Log.d("LocationCallback", "ENTRA FINE")
//
//                fusedLocationClient.requestLocationUpdates(
//                    locationRequest,
//                    locationCallback,
//                    Looper.myLooper()
//                );
//            }else{
//                checkLocationpermissions();
//            }
//        }

    }
    private fun showSettingsRedirectDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permiso requerido")
            .setMessage("Para continuar usando la aplicación, habilita los permisos de ubicación desde la configuración de la app.")
            .setPositiveButton("Ir a configuración") { _, _ ->
                // Redirigir al usuario a la configuración de la aplicación
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialogInterface, i ->finish()})
            .setOnCancelListener {
                finish()  // Cerrar la actividad si se toca fuera del diálogo
            }
            .show()
    }
    fun showAlertDialogNOGPS(){
        AlertDialog.Builder(this)
            .setTitle("Activa el GPS")
            .setMessage("Para continuar usando la aplicación, habilita el GPS de tu dispositivo")
            .setPositiveButton("Ir a configuración") { _, _ ->
                // Redirigir al usuario a la configuración de la aplicación
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            .setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialogInterface, i ->finish()})
            .setOnCancelListener {
                finish()  // Cerrar la actividad si se toca fuera del diálogo
            }
            .show()
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

    private fun gpsActive(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    fun permissionGpsNative(){
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }





}