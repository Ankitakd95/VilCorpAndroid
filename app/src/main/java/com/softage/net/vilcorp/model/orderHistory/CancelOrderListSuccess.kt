package com.softage.net.vilcorp.model.orderHistory

import com.google.gson.annotations.SerializedName

data class CancelOrderListSuccess(
    @SerializedName("client"        ) var client       : String? = null,
    @SerializedName("manager_name"  ) var managerName  : String? = null,
    @SerializedName("manager_phone" ) var managerPhone : Long?    = null,
    @SerializedName("manager_email" ) var managerEmail : String? = null,
    @SerializedName("pjp_category"  ) var pjpCategory  : String? = null,
    @SerializedName("pjp_segment"   ) var pjpSegment   : String? = null,
    @SerializedName("spoc_name"     ) var spocName     : String? = null,
    @SerializedName("spoc_phone"    ) var spocPhone    : Long?    = null,
    @SerializedName("status"        ) var status       : String? = null,
    @SerializedName("reason"        ) var reason       : String? = null
)
