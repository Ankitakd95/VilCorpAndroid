package com.softage.net.vilcorp.model.leads

import com.google.gson.annotations.SerializedName

data class PjpStageData(
    @SerializedName("pjp_id"      ) var pjpId      : Int?     = null,
    @SerializedName("stage_one"   ) var stageOne   : Boolean? = null,
    @SerializedName("stage_two"   ) var stageTwo   : Boolean? = null,
    @SerializedName("stage_three" ) var stageThree : Boolean? = null
)
