package com.softage.net.vilcorp.model.notification

import com.google.gson.annotations.SerializedName

data class ReadAllResponse(
    @SerializedName("status"        ) var status       : Boolean? = null,
    @SerializedName("error"         ) var error        : Boolean? = null,
    @SerializedName("message"       ) var message      : String?  = null,
    @SerializedName("updated_count" ) var updatedCount : Int?     = null
)
