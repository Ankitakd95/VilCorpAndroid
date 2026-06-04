package com.softage.net.vilcorp.model.leads

import com.google.gson.annotations.SerializedName

data class PjpStageSuccess(
    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : PjpStageData?   = PjpStageData()
)
