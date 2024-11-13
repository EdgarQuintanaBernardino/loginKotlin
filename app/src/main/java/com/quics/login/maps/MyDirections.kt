package com.quics.login.maps


import android.content.Intent

import android.os.Bundle
import android.view.View

import androidx.appcompat.widget.Toolbar

import androidx.appcompat.app.AppCompatActivity


import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quics.login.R
import com.quics.login.SearchMap
import com.quics.login.adapters.ShippingAddressesAdapter
import com.quics.login.data.DirectionData

import com.quics.login.home.HomeActivity

import com.quics.login.utils.UserActive

class MyDirections : AppCompatActivity() {
    private lateinit var btnAdd:View;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_directions)
        btnAdd=findViewById(R.id.view_add_new_label);
        btnAdd.setOnClickListener {
            val intent = Intent(this, SearchMap::class.java)
            startActivity(intent)
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // Setting the Toolbar to act as the ActionBar
        // setSupportActionBar(toolbar)

        // Setting click listener
        toolbar.setNavigationOnClickListener {
            // Finishing current activity
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("OPEN_FRAGMENT", "MyProfileFragment")
            startActivity(intent)
        }
        initShippingAddressesRecyclerView()
    }


    override fun onResume() {
        super.onResume()
        initShippingAddressesRecyclerView()
    }

    private fun initShippingAddressesRecyclerView() {
        // Obtener el recyclerView por su ID
        val recyclerViewShippingAddresses = findViewById<RecyclerView>(R.id.recycler_view_shipping_addresses)

        // Configurar el layout manager para el recyclerView
        recyclerViewShippingAddresses.layoutManager = LinearLayoutManager(this)

        // Definir la lista de direcciones
        val shippingAddressesArrayList = ArrayList<DirectionData>()
        // Asegurarse de que `direcciones` no sea nulo

        UserActive().getUserData(this)?.direcciones?.let { shippingAddressesData ->
                for (data in shippingAddressesData) {
                    println("Data: $data")
                    shippingAddressesArrayList.add(data)
                }
            }
        // Crear el adaptador con la lista de direcciones
        val shippingAddressesAdapter = ShippingAddressesAdapter(this, shippingAddressesArrayList,lifecycleScope)

        // Asignar el adaptador al recyclerView
        recyclerViewShippingAddresses.adapter = shippingAddressesAdapter
    }
}