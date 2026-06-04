package com.softage.net.vilcorp.model.login

import com.google.gson.annotations.SerializedName

data class ForgotPassResponse (
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("status"  ) var status  : String?  = null,
    @SerializedName("error"   ) var error   : Boolean? = null
)