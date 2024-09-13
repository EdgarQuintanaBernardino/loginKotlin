package com.quics.login.login

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quics.login.adapters.PoliciesAdapter
import com.quics.login.data.PoliciesData
import com.quics.login.R
import com.quics.login.classes.AdaptiveSpacingItemDecoration
import com.quics.login.models.PolicyItemModel
import kotlin.math.roundToInt

class PoliciesActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policies)

        // Calling method to initialize the RecyclerView
        initRecyclerView()
    }

    // Method to initialize the RecyclerView
    private fun initRecyclerView(){
        // Getting the recyclerView by its id
        val recyclerViewPolicies = findViewById<RecyclerView>(R.id.recycler_view_policies)

        // Setting layout manager for the recyclerView
        recyclerViewPolicies.layoutManager = GridLayoutManager(this, 2)

        // Defining arrayList of the GridViewProductsItemModel
        val arrayList = ArrayList<PolicyItemModel>()

        // Creating an object of the PoliciesData class
        val policiesData = PoliciesData(this)

        // Looping through array elements
        for (data in policiesData.data){
            // Adding data to arrayList
            arrayList.add(data)
        }

        // Passing arrayList to the adapter
        val policiesAdapter = PoliciesAdapter(arrayList)

        // Setting adapter for the recyclerView
        recyclerViewPolicies.adapter = policiesAdapter

        // Assigning equal spacing to the each item of the recyclerView
        recyclerViewPolicies.addItemDecoration(AdaptiveSpacingItemDecoration(resources.getDimension(com.intuit.sdp.R.dimen._10sdp).roundToInt(), true))
    }
}