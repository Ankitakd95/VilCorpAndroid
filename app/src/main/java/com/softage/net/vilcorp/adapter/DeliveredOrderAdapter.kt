package com.softage.net.vilcorp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.softage.net.vilcorp.R
import com.softage.net.vilcorp.model.leads.CancelOrderSuccess
import com.softage.net.vilcorp.model.leads.LeadModel
import com.softage.net.vilcorp.model.orderHistory.CancelOrderListSuccess
import com.softage.net.vilcorp.ui.fragment.HomeFragment

class DeliveredOrderAdapter(
    private val context: Context,
    private val list: List<CancelOrderListSuccess>,


) : RecyclerView.Adapter<DeliveredOrderAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val clientName: TextView = view.findViewById(R.id.tvClientName)

        val manager: TextView = view.findViewById(R.id.tvManager)
        val managerPhone: TextView = view.findViewById(R.id.tvManagerPhone)
        val managerEmail: TextView = view.findViewById(R.id.tvManagerEmail) // ✅ NEW

        val spoc: TextView = view.findViewById(R.id.tvSpoc)
        val spocPhone: TextView = view.findViewById(R.id.tvSpocPhone)

        val category: TextView = view.findViewById(R.id.tvCategory)
        val segment: TextView = view.findViewById(R.id.tvSegment) // ✅ NEW

//        val status: TextView = view.findViewById(R.id.tvStatus)
        val reason: TextView = view.findViewById(R.id.tvReason) // ✅ NEW


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_deliver_order, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = list[position]

        // ✅ Basic Info
        holder.clientName.text = item.client

        holder.category.text = "Category: ${item.pjpCategory}"
        holder.segment.text = "Segment: "+item.pjpSegment   // NEW

        // ✅ Manager
        holder.manager.text = "Manager: ${item.managerName}"
        holder.managerPhone.text = item.managerPhone.toString()
        holder.managerEmail.text = item.managerEmail  // NEW

        // ✅ SPOC
        holder.spoc.text = "Spoc: ${item.spocName}"
        holder.spocPhone.text = item.spocPhone.toString()






    }

    override fun getItemCount(): Int {
        return list.size
    }
}