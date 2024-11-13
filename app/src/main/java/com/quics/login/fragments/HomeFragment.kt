package com.quics.login.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide

import com.quics.login.classes.AdaptiveSpacingItemDecoration
import com.quics.login.home.HomeActivity



import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.quics.login.R
import com.quics.login.utils.UserActive
import kotlin.math.roundToInt
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Getting menu icon image by its id
        val menuIconImage = view.findViewById<ImageView>(R.id.image_menu_icon)

        // Setting on click listener for the drawer menu icon image
        menuIconImage.setOnClickListener { (activity as HomeActivity).openCloseNavigationDrawer() }

        // Getting see all label(Categories) reference by its id
        val categoriesSeeAllLabel = view.findViewById<TextView>(R.id.text_categories_see_all_label)

        // Setting on click listener for the see all label(categories)
//        categoriesSeeAllLabel.setOnClickListener { context?.let { it1 ->
//            (activity as HomeActivity).openActivity(
//                it1, "com.example.quics.CategoriesActivity")
//        } }

        // Getting see all label(Most popular) reference by its id
        val mostPopularSeeAllLabel = view.findViewById<TextView>(R.id.text_most_popular_see_all_label)

        // Setting on click listener for the see all label(most popular)
//        mostPopularSeeAllLabel.setOnClickListener { context?.let { it1 ->
//            (activity as HomeActivity).openActivity(
//                it1, "com.example.quics.GridViewProductsActivity")
//        } }

        // Getting see all label(Recently viewed) reference by its id
        val recentlyViewedSeeAllLabel = view.findViewById<TextView>(R.id.text_recently_viewed_see_all_label)

        // Setting on click listener for the see all label(recently viewed)
//        recentlyViewedSeeAllLabel.setOnClickListener { context?.let { it1 ->
//            (activity as HomeActivity).openActivity(
//                it1, "com.example.quics.GridViewProductsActivity")
//        } }

        // Calling method to initialize the ViewPager2(Main carousel)
//        initViewPager2(view)

        // Calling method to initialize the RecyclerView(Categories)
       // initRecyclerViewCategories(view)

        // Calling method to initialize the RecyclerView(Most popular)
//        initRecyclerViewMostPopularProducts(view)
//
//        // Calling method to initialize the RecyclerView(Recently viewed)
//        initRecyclerViewRecentlyViewedProducts(view)
//
//        // Calling method to initialize the RecyclerView(Best restaurants)
//        initRecyclerViewBestRestaurants(view)
    }

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override fun onResume() {
        super.onResume()
        changeWelcome(requireView())
    }
    private fun changeWelcome(v:View){
        val label:TextView=v.findViewById(R.id.text_greeting)
        var image_customer=v.findViewById(R.id.image_customer) as ImageView
        image_customer.setOnClickListener(){
            ///open profile
            replaceFragment(MyProfileFragment())
            Log.d("HomeFragment","Image clicked")
        }
        UserActive().getUserData(requireContext())?.let {

            label.text = it.nombre
            if(it?.foto != "Invitado"){
                Glide.with(this) // Usa `this` o el contexto adecuado, como `requireContext()` en un Fragment
                    .load(it?.foto) // Carga la imagen
                    .placeholder(R.drawable.ic_person) // Imagen mientras se carga (opcional)
                    .error(R.drawable.ic_person) // Imagen en caso de error (opcional)
                    .into(image_customer) // Coloca la imagen en el ImageView

            }
        }



    }
    // Method to initialize the ViewPager2(Main carousel)
//    private fun initViewPager2(view: View){
//        // Getting viewPager2 and tabLayout by their ids
//        val viewPager2 = view.findViewById<ViewPager2>(R.id.view_pager_2)
//        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
//
//        // Applying options to the viewPager2
//        viewPager2.apply {
//            isUserInputEnabled = true
//            clipChildren = false  // No clipping the left and right items
//            clipToPadding = false  // Show the viewpager in full width without clipping the padding
//            offscreenPageLimit = 3  // Render the left and right items
//            (getChildAt(0) as RecyclerView).overScrollMode =
//                RecyclerView.OVER_SCROLL_NEVER // Remove the scroll effect
//        }
//
//        // Defining arrayList of the ProductCarouselItemViewModel
//        val homeViewPager2ItemsArrayList = ArrayList<HomeViewPager2ItemModel>()
//
//        // Creating an object of the HomeViewPager2Data class
//        val homeCategoriesData = HomeViewPager2Data()
//
//        // Looping through array elements
//        for (data in homeCategoriesData.data){
//            // Adding data to productIngredientsArrayList
//            homeViewPager2ItemsArrayList.add(data)
//        }
//
//        // Pass productCarouselItemsArrayList to the adapter
//        viewPager2.adapter = HomeViewPager2ItemsAdapter(homeViewPager2ItemsArrayList)
//
//        TabLayoutMediator(tabLayout, viewPager2) { _, _ ->
//            //Some implementation
//        }.attach()
//    }

    // Method to initialize the RecyclerView(Categories)
//    private fun initRecyclerViewCategories(view: View){
//        // Getting the recyclerView by its id
//        val recyclerViewCategories = view.findViewById<RecyclerView>(R.id.recycler_view_categories)
//
//        // Setting layout manager for the recyclerView
//        recyclerViewCategories.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//
//        // Defining arrayList of the HomeCategoryItemModel
//        val categoriesArrayList = ArrayList<HomeCategoryItemModel>()
//
//        // Creating an object of the HomeCategoriesData class
//        val categoriesCarouselData = HomeCategoriesData()
//
//        // Looping through array elements
//        for (data in categoriesCarouselData.data){
//            // Adding data to categoriesArrayList
//            categoriesArrayList.add(data)
//        }
//
//        // Pass arrayList to the adapter
//        val homeCategoriesAdapter = HomeCategoriesAdapter(categoriesArrayList)
//
//        // Setting adapter for the recyclerView
//        recyclerViewCategories.adapter = homeCategoriesAdapter
//
//        // Assigning equal spacing to the each item of the recyclerView
//        recyclerViewCategories.addItemDecoration(
//            AdaptiveSpacingItemDecoration(resources.getDimension(
//                com.intuit.sdp.R.dimen._15sdp).roundToInt(), false)
//        )
//    }

    // Method to initialize the RecyclerView(Most popular)
//    private fun initRecyclerViewMostPopularProducts(view: View){
//        // Getting the recyclerView by its id
//        val recyclerViewMostPopularProducts = view.findViewById<RecyclerView>(R.id.recycler_view_most_popular_products)
//
//        // Setting layout manager for the recyclerView
//        recyclerViewMostPopularProducts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//
//        // Defining arrayList of the HomeCategoryItemModel
//        val mostPopularProductsArrayList = ArrayList<HomeMostPopularProductItemModel>()
//
//        // Creating an object of the HomeMostPopularProductsData class
//        val homeMostPopularProductsData = HomeMostPopularProductsData()
//
//        // Looping through array elements
//        for (data in homeMostPopularProductsData.data){
//            // Adding data to categoriesArrayList
//            mostPopularProductsArrayList.add(data)
//        }
//
//        // Pass arrayList to the adapter
//        val homeMostPopularProductsAdapter = HomeMostPopularProductsAdapter(mostPopularProductsArrayList)
//
//        // Setting adapter for the recyclerView
//        recyclerViewMostPopularProducts.adapter = homeMostPopularProductsAdapter
//
//        // Assigning equal spacing to the each item of the recyclerView
//        recyclerViewMostPopularProducts.addItemDecoration(
//            AdaptiveSpacingItemDecoration(resources.getDimension(
//                com.intuit.sdp.R.dimen._15sdp).roundToInt(), false)
//        )
//    }

    // Method to initialize the RecyclerView(Recently viewed)
//    private fun initRecyclerViewRecentlyViewedProducts(view: View){
//        // Getting the recyclerView by its id
//        val recyclerViewRecentlyViewedProducts = view.findViewById<RecyclerView>(R.id.recycler_view_recently_viewed_products)
//
//        // Setting layout manager for the recyclerView
//        recyclerViewRecentlyViewedProducts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//
//        // Defining arrayList of the HomeRecentlyViewedProductItemModel
//        val recentlyViewedProductsArrayList = ArrayList<HomeRecentlyViewedProductItemModel>()
//
//        // Creating an object of the HomeRecentlyViewedProductsData class
//        val recentlyViewedProductsData = HomeRecentlyViewedProductsData()
//
//        // Looping through array elements
//        for (data in recentlyViewedProductsData.data){
//            // Adding data to recentlyViewedProductsArrayList
//            recentlyViewedProductsArrayList.add(data)
//        }
//
//        // Pass arrayList to the adapter
//        val homeRecentlyViewedProductsAdapter = HomeRecentlyViewedProductsAdapter(recentlyViewedProductsArrayList)
//
//        // Setting adapter for the recyclerView
//        recyclerViewRecentlyViewedProducts.adapter = homeRecentlyViewedProductsAdapter
//
//        // Assigning equal spacing to the each item of the recyclerView
//        recyclerViewRecentlyViewedProducts.addItemDecoration(
//            AdaptiveSpacingItemDecoration(resources.getDimension(
//                com.intuit.sdp.R.dimen._15sdp).roundToInt(), false)
//        )
//    }

    // Method to initialize the RecyclerView(Best restaurants)
//    private fun initRecyclerViewBestRestaurants(view: View){
//        // Getting the recyclerView by its id
//        val recyclerViewBestRestaurants = view.findViewById<RecyclerView>(R.id.recycler_view_best_restaurants)
//
//        // Setting layout manager for the recyclerView
//        recyclerViewBestRestaurants.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//
//        // Defining arrayList of the HomeBestRestaurantItemModel
//        val bestRestaurantsArrayList = ArrayList<HomeBestRestaurantItemModel>()
//
//        // Creating an object of the HomeBestRestaurantsData class
//        val bestRestaurantsData = HomeBestRestaurantsData()
//
//        // Looping through array elements
//        for (data in bestRestaurantsData.data){
//            // Adding data to bestRestaurantsArrayList
//            bestRestaurantsArrayList.add(data)
//        }
//
//        // Pass arrayList to the adapter
//        val homeBestRestaurantsAdapter = HomeBestRestaurantsAdapter(bestRestaurantsArrayList)
//
//        // Setting adapter for the recyclerView
//        recyclerViewBestRestaurants.adapter = homeBestRestaurantsAdapter
//
//        // Assigning equal spacing to the each item of the recyclerView
//        recyclerViewBestRestaurants.addItemDecoration(
//            AdaptiveSpacingItemDecoration(resources.getDimension(
//                com.intuit.sdp.R.dimen._15sdp).roundToInt(), false)
//        )
//    }
    private fun replaceFragment(fragment: Fragment) {
        // Getting supportFragmentManager
        val fragmentManager = parentFragmentManager

        // Beginning transaction
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Replacing FrameLayout with a desired Fragment
        fragmentTransaction.replace(R.id.frame_layout, fragment)

        // Committing Fragment transaction
        fragmentTransaction.commit()
    }
}