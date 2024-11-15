package com.quics.login.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.quics.login.R

import com.google.firebase.auth.FirebaseAuth
import com.quics.login.utils.UserActive
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.quics.login.home.HomeActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MyProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_my_profile, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Getting card references by their ids
        val cardLink1 = view.findViewById<CardView>(R.id.card_link_1)
        val cardLink2 = view.findViewById<CardView>(R.id.card_link_2)
        val cardLink3 = view.findViewById<CardView>(R.id.card_link_3)
        val cardLink5 = view.findViewById<CardView>(R.id.card_link_5)
        val cardLink7 = view.findViewById<CardView>(R.id.card_link_7)
        val cardLink8 = view.findViewById<CardView>(R.id.card_link_8)
        val cadDirecciones=view.findViewById<CardView>(R.id.card_direcciones)



        val toolbar =  requireView().findViewById<Toolbar>(R.id.toolbar)
        // Setting click listener
        toolbar.setNavigationOnClickListener {
            // Finishing current activity
            val intentHome= Intent(requireContext(), HomeActivity::class.java);
            startActivity(intentHome);
        }
        // Setting click listeners
        cardLink1.setOnClickListener {
            // Calling method to open specified activity
            context?.let { it -> openActivity(it, "com.quics.login.EditProfileActivity") }
        }
        cadDirecciones.setOnClickListener {
            // Calling method to open specified activity
            context?.let { it -> openActivity(it, "com.quics.login.maps.MyDirections") }
        }
        cardLink2.setOnClickListener {
            // Calling method to open specified activity
            context?.let { it -> openActivity(it, "com.example.quics.SettingsActivity") }
        }

        cardLink3.setOnClickListener {
            // Calling method to open specified activity
            context?.let { it -> openActivity(it, "com.example.quics.OrdersActivity") }
        }



        cardLink5.setOnClickListener {
            // Calling method to open specified activity
            context?.let { it -> openActivity(it, "com.example.quics.MyWalletActivity") }
        }



        cardLink7.setOnClickListener {
            // Calling method to open specified activity
            context?.let { it -> openActivity(it, "com.example.quics.CouponsActivity") }
        }
        cardLink8.setOnClickListener {

            UserActive().signOut(requireContext());
            // Calling method to open specified activity


        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun updateUser(){



    }
    // Method to open specified activity
    private fun openActivity(context: Context, activityName: String) {
        // Getting a reference to the activity class based on its name
        val activityClass = Class.forName(activityName)

        // Creating an object of the Intent class
        val intent = Intent(context, activityClass)

        // Starting activity
        context.startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        updateUser()
    }
}