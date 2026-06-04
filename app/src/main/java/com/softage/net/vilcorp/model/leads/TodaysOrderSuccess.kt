package com.softage.net.vilcorp.model.leads

import com.google.gson.annotations.SerializedName

data class TodaysOrderSuccess(
    val lead_id: Int,
    val client_code: String,
    val client_name: String,
    val client_address: String,
    val manager_name: String,
    val manager_phone: String,
    val spoc_name: String,
    val spoc_phone: String,
    val pjp_category: String,
    val sim_count: String?,
    val target_delivery_date: String,
    val lead_status: String
)
