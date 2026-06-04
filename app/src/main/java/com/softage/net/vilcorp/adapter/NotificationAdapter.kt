package com.softage.net.vilcorp.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.softage.net.vilcorp.R
import com.softage.net.vilcorp.model.notification.NotificationResponse
import java.text.SimpleDateFormat
import java.util.Locale

class NotificationAdapter(
    private val list: MutableList<NotificationResponse>,
    private val onClick: (NotificationResponse) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        val tvRemarks: TextView = itemView.findViewById(R.id.tvRemarks)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)

        val viewDot: View = itemView.findViewById(R.id.viewDot)
        val viewIndicator: View = itemView.findViewById(R.id.viewIndicator)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]

        holder.tvTitle.text = item.title
        holder.tvMessage.text = item.message
        holder.tvRemarks.text = item.remarks
        holder.tvDate.text = item.relative_time

        // ✅ LINK SUPPORT
        Linkify.addLinks(holder.tvMessage, Linkify.WEB_URLS)
        holder.tvMessage.movementMethod = LinkMovementMethod.getInstance()
        holder.tvMessage.setLinkTextColor(Color.BLUE)

        // ✅ READ / UNREAD UI
        if (item.is_read) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#F5F5F5"))
            holder.viewDot.visibility = View.GONE
            holder.viewIndicator.visibility = View.GONE
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE)
            holder.viewDot.visibility = View.VISIBLE
            holder.viewIndicator.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }

}