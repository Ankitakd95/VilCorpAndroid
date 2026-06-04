package com.softage.net.vilcorp.model.attendance

import com.google.gson.annotations.SerializedName

data class PunchInSuccess(
    @SerializedName("id"              ) var id             : Int?    = null,
    @SerializedName("username"        ) var username       : String? = null,
    @SerializedName("punch_in"        ) var punchIn        : String? = null,
    @SerializedName("punch_out"       ) var punchOut       : String? = null,
    @SerializedName("attendance_date" ) var attendanceDate : String? = null,
    @SerializedName("duration"        ) var duration       : String? = null,
    @SerializedName("latitude"        ) var latitude       : Double? = null,
    @SerializedName("longitude"       ) var longitude      : Double? = null,
    @SerializedName("punch_in_url"    ) var punchInUrl     : String? = null,
    @SerializedName("punch_out_url"   ) var punchOutUrl    : String? = null,
    @SerializedName("status"          ) var status         : String? = null
)
