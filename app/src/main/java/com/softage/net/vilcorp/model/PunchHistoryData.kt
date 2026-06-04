package com.softage.net.vilcorp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PunchHistoryData(
    @SerializedName("date")
    @Expose
    var date: Int? = null,

    @SerializedName("month")
    @Expose
    var month: String? = null,

    @SerializedName("year")
    @Expose
    var year: Int? = null,

    @SerializedName("Day")
    @Expose
    var day: String? = null,

    @SerializedName("punch_in_time")
    @Expose
    var punchInTime: String? = null,

    @SerializedName("punch_out_time")
    @Expose
    var punchOutTime: String? = null,

    @SerializedName("total_hour")
    @Expose
    var totalHour: String? = null,

    @SerializedName("status")
    @Expose
    var status: String? = null
)
