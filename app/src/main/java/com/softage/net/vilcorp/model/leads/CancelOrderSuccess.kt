package com.softage.net.vilcorp.model.leads

import com.google.gson.annotations.SerializedName

data class CancelOrderSuccess(
    @SerializedName("message"  ) var message : String? = null,
    @SerializedName("success"  ) var success : Int?    = null,
    @SerializedName("order_id" ) var orderId : Int?    = null
)
