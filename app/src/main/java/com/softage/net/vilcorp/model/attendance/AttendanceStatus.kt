package com.softage.net.vilcorp.model.attendance

import com.google.gson.annotations.SerializedName

data class AttendanceStatus(
    @SerializedName("status"            ) var status           : String?  = null,
    @SerializedName("username"          ) var username         : String?  = null,
    @SerializedName("marked_attendance" ) var markedAttendance : Boolean? = null

)
