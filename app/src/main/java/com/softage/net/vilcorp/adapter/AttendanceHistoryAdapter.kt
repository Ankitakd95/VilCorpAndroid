package com.softage.net.vilcorp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softage.net.vilcorp.R
import com.softage.net.vilcorp.model.PunchHistoryData


class AttendanceHistoryAdapter(
    private val mContext: Context,
    private val mData: List<PunchHistoryData>
) : RecyclerView.Adapter<AttendanceHistoryAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attendance, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = mData[position]

        // Status
        if (data.status == null) {
            holder.status.text = ""
        } else {
            holder.status.text = data.status
            if (data.status!!.contains("Absent")) {
                holder.status.setTextColor(
                    mContext.resources.getColor(R.color.vil_red)
                )
            } else {
                holder.status.setTextColor(
                    mContext.resources.getColor(R.color.green_dark)
                )
            }
        }

        // Date
        holder.date.text = (data.date ?: "").toString()

        // Month
        holder.month.text = data.month?.substring(0, 3) ?: ""

        // Year
        holder.year.text = (data.year ?: "").toString()

        // Day
        holder.day.text = data.day?.substring(0, 3) ?: ""

        // Punch In
        holder.punchin.text = data.punchInTime ?: ""

        // Punch Out
        holder.punchout.text = data.punchOutTime ?: ""

        // Total Hours
        holder.total.text = data.totalHour ?: ""
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val date: TextView = itemView.findViewById(R.id.tvDate)
        val month: TextView = itemView.findViewById(R.id.tvMonth)
        val year: TextView = itemView.findViewById(R.id.tvYear)
        val day: TextView = itemView.findViewById(R.id.tvDay)
        val punchin: TextView = itemView.findViewById(R.id.pnchIn)
        val punchout: TextView = itemView.findViewById(R.id.pnchOut)
        val status: TextView = itemView.findViewById(R.id.tvStatus)
        val total: TextView = itemView.findViewById(R.id.totalHr)

    }
}