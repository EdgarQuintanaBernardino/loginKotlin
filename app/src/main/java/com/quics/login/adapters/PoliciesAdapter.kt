package com.quics.login.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.quics.login.R

import com.quics.login.login.PolicyActivity
import com.quics.login.models.PolicyItemModel

class PoliciesAdapter(private val arrayList: ArrayList<PolicyItemModel>):
    RecyclerView.Adapter<PoliciesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflating layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_policy, parent, false)

        // Returning view
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        // Returning size of the arrayList
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Getting arrayList data by its position
        val policiesItemModel = arrayList[position]

        // Setting text
        holder.policyTitle.text = policiesItemModel.policyTitle
        holder.policyShortInfo.text = policiesItemModel.policyShortInfo

        // Setting click listener for the card
        holder.cardPolicy.setOnClickListener {
            // Getting reference of the context
            val context = holder.cardPolicy.context
            val intent = Intent(context, PolicyActivity::class.java)
            intent.putExtra("id",policiesItemModel.Id)
            context.startActivity(intent)

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardPolicy:CardView = itemView.findViewById(R.id.card_policy)
        val policyTitle: TextView = itemView.findViewById(R.id.text_policy_title)
        val policyShortInfo: TextView = itemView.findViewById(R.id.text_policy_short_info)
    }
}