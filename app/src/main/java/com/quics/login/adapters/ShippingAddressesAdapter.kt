package com.quics.login.adapters

import PopupManager
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.quics.login.R
import com.quics.login.SearchMap
import com.quics.login.classes.ConfirmDialog
import com.quics.login.data.DirectionData
import com.quics.login.maps.MyDirections
import com.quics.login.models.AddressItemModel
import com.quics.login.utils.UserActive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class ShippingAddressesAdapter(private val context: Context, private var arrayList: ArrayList<DirectionData>, private val lifecycleScope: CoroutineScope):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var prev: ItemViewHolder? = null
    override fun getItemViewType(position: Int): Int {
        // Getting arrayList data by its position
        val checkoutShippingAddressesItemViewModel = arrayList[position]
        return 2;
        // Returning integer based on condition
//        return if (checkoutShippingAddressesItemViewModel.itemType === "header"){
//            1
//        }else{
//            2
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // Comparing
        return if (viewType == 1) {
            // Inflating layout
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.header_checkout_recyclerview, parent, false)

            // Returning view
            HeaderViewHolder(view)
        } else {
            // Inflating layout
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_shipping_address, parent, false)

            // Returning view
            ItemViewHolder(view)
        }
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Getting arrayList data by its position
        val checkoutShippingAddressesItemViewModel = arrayList[position]

        // Comparing
        if (holder is ItemViewHolder) {
            // Comparing
//            if (checkoutShippingAddressesItemViewModel.isSelected == true){
            if (checkoutShippingAddressesItemViewModel.default) {
                // Setting background resource
                holder.addressView.setBackgroundResource(R.drawable.bg_selected_checkout_address_and_payment_item)
                prev = holder;
                // Setting visibility
                holder.checkmark.visibility = View.VISIBLE;
                holder.addressView.setOnClickListener {
                    changeDefault(arrayList[position], position, holder)
                }
                holder.iconDelete.setOnClickListener {
                    // Implementing delete address functionality
                    deleteAddress(arrayList[position], position)

                }
                holder.iconUpdate.setOnClickListener {
                    editAddress(arrayList[position], position)
                }
            } else {
                // Setting background resource
                holder.addressView.setBackgroundResource(R.drawable.bg_checkout_address_and_payment_item)
                holder.addressView.setOnClickListener {

                    changeDefault(arrayList[position], position, holder)
                }

                // Setting visibility
                holder.checkmark.visibility = View.GONE;
                holder.iconUpdate.setOnClickListener {
                    editAddress(arrayList[position], position)
                }
                holder.iconDelete.setOnClickListener {
                    // Implementing delete address functionality
                    deleteAddress(arrayList[position], position)

                }

            }
//            checkoutShippingAddressesItemViewModel.addressIcon?.let {
//                // Setting image view resource
//                holder.addressIcon.setImageResource(
//                    it
//                )
//            } cambiar el icono dinamiamente

            // Setting text
            holder.addressType.text = checkoutShippingAddressesItemViewModel.name
            holder.address.text = checkoutShippingAddressesItemViewModel.stringDirection

        } else if (holder is HeaderViewHolder) {
            // Setting text
            holder.textView.setText(R.string.add_new_address)

            // Handling click listener
            holder.addressView.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Add New Address")
                builder.setMessage("Okay! let me implement this screen or modal window.")
                builder.show()
            }
        }
    }

    override fun getItemCount(): Int {
        // Returning size of the arrayList
        return arrayList.size
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Getting views by id from inflated layout
        val addressView: View = itemView.findViewById(R.id.view_add_new_label)
        val textView: TextView = itemView.findViewById(R.id.text_add_new_label)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Getting views by id from inflated layout
        val addressView: View = itemView.findViewById(R.id.view_address)
        val iconUpdate: View = itemView.findViewById(R.id.view_update_icon)
        val iconDelete: View = itemView.findViewById(R.id.view_address_icon)
        val addressType: TextView = itemView.findViewById(R.id.text_address_type)
        val checkmark: ImageView = itemView.findViewById(R.id.image_checkmark)

        val address: TextView = itemView.findViewById(R.id.text_address)
    }

    private fun editAddress(item: DirectionData, position: Int) {
        // Implementing edit address functionality
        val intent = Intent(context, SearchMap::class.java)
        intent.putExtra("lat", item.lat)
        intent.putExtra("long", item.long)
        intent.putExtra("name", item.name)
        intent.putExtra("default", item.default)
        intent.putExtra("position", position);

         context.startActivity(intent)

    }

    fun confirmDelete(item: DirectionData, position: Int) {
        lifecycleScope.launch {
            val popupManager = PopupManager(context)
            popupManager.showPopup("Eliminando dirección")
            val user = UserActive()
            user.getUserData(context)?.let { userData ->
                val position = arrayList.indexOf(item)
                arrayList.removeAt(position)
                user.updateDirectionBd(context, arrayList as List<DirectionData>)
                userData.direcciones = arrayList
                user.saveUserData(context, userData)

                // Elimina el ítem de arrayList y notifica el cambio

                notifyItemRemoved(position) // Pasa el índice correcto aquí
                notifyItemRangeChanged(position, arrayList.size - position)

                popupManager.hidePopup()

            } ?: run {
                popupManager.showPopup("Error: No se pudo obtener los datos del usuario")

            }
            popupManager.hidePopup()
        }
    }

    fun deleteAddress(item: DirectionData, position: Int) {
        // Implementing delete address functionality
        //confirmar si desea eliminar la dirección
        var dialog = ConfirmDialog(context);
        dialog.setParams(
            "Eliminar Dirección",
            "¿Está seguro de que desea eliminar esta dirección?"
        )
        dialog.setFunctions { confirmDelete(item, position) }
        dialog.show();


    }

    fun changeDefault(item: DirectionData, position: Int, holder: ItemViewHolder) {
        // Implementing edit address functionality
        if (holder == prev) {
            return;
        }
        lifecycleScope.launch {

            var user = UserActive();
            arrayList.forEach() {
                it.default = false
            }
            user.getUserData(context).let {

                arrayList[position].default = true
                if (it != null) {
                    it.direcciones = arrayList
                }
                user.updateDirectionBd(context, arrayList)
                if (it != null) {
                    user.saveUserData(context, it)
                }
                holder.addressView.setBackgroundResource(R.drawable.bg_selected_checkout_address_and_payment_item)
                holder.checkmark.visibility = View.VISIBLE;
                prev?.addressView?.setBackgroundResource(R.drawable.bg_checkout_address_and_payment_item)
                prev?.checkmark?.visibility = View.GONE;
                prev = holder;


            }

        }

    }
}