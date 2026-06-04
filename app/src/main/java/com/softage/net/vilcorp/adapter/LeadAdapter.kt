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
import com.softage.net.vilcorp.model.leads.LeadModel
import com.softage.net.vilcorp.ui.fragment.HomeFragment

class LeadAdapter(
    private val context: Context,
    private val list: List<LeadModel>,
    private val onItemClick: (LeadModel) -> Unit

) : RecyclerView.Adapter<LeadAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val clientName: TextView = view.findViewById(R.id.tvClientName)
        val address: TextView = view.findViewById(R.id.tvAddress)
        val manager: TextView = view.findViewById(R.id.tvManager)
        val managerPhone: TextView = view.findViewById(R.id.tvManagerPhone)
        val spoc: TextView = view.findViewById(R.id.tvSpoc)
        val spocPhone: TextView = view.findViewById(R.id.tvSpocPhone)
        val category: TextView = view.findViewById(R.id.tvCategory)
        val deliveryDate: TextView = view.findViewById(R.id.tvDeliveryDate)
        val status: TextView = view.findViewById(R.id.tvStatus)
        val tvSimCount: TextView = view.findViewById(R.id.tvSimCount)
        val tvNavigate: TextView = view.findViewById(R.id.tvNavigate)

        val callManager: ShapeableImageView = view.findViewById(R.id.btnCallManager)
        val callSpoc: ShapeableImageView = view.findViewById(R.id.btnCallSpoc)
        val cardLv: CardView = view.findViewById(R.id.cardLv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_lead, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = list[position]

        holder.clientName.text = item.clientName
        holder.address.text = item.clientAddress
        holder.manager.text = "Manager: ${item.managerName}"
        holder.managerPhone.text = item.managerPhone
        holder.spoc.text = "Spoc: ${item.spocName}"
        holder.spocPhone.text = item.spocPhone
        holder.category.text = "Category: ${item.pjpCategory}"
        holder.tvSimCount.text = "Total SIM: ${item.simCount}"
        holder.deliveryDate.text = "${item.targetDeliveryDate}"
        holder.status.text = "${item.leadStatus}"

        holder.callManager.setOnClickListener {

            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${item.managerPhone}")
            context.startActivity(intent)
        }

        holder.callSpoc.setOnClickListener {

            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${item.spocPhone}")
            context.startActivity(intent)
        }
        holder.tvNavigate.setOnClickListener {

            val gmmIntentUri = Uri.parse("google.navigation:q=" + item.clientAddress)

            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            context.startActivity(mapIntent)
        }
        holder.itemView.setOnClickListener {
            onItemClick(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}