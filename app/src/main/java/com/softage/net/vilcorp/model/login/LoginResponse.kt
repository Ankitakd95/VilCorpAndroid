package com.softage.net.vilcorp.model.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token"     ) var accessToken     : String? = null,
    @SerializedName("token_type"       ) var tokenType       : String? = null,
    @SerializedName("refresh_token"    ) var refreshToken    : String? = null,
    @SerializedName("branch"           ) var branch          : String? = null,
    @SerializedName("branch_id"        ) var branchId        : String? = null,
    @SerializedName("branch_latitude"  ) var branchLatitude  : Double? = null,
    @SerializedName("branch_longitude" ) var branchLongitude : Double? = null,
    @SerializedName("is_remote_punch_in" ) var isRemotePunchIn : Boolean? = null,
    @SerializedName("is_remote_punch_out" ) var isRemotePunchOut : Boolean? = null

)
