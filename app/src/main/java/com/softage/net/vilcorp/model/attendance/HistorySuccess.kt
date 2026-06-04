package com.softage.net.vilcorp.model.attendance

import com.google.gson.annotations.SerializedName

data class HistorySuccess(
    val logs: List<AttendanceLog>,
    val page_size: Int,
    val next_cursor_date: String?,
    val next_cursor_id: String?
)

data class AttendanceLog(
    val username: String,
    val attendance_date: String,
    val punch_in: String?,
    val punch_out: String?,
    val duration: String?,
    val overtime_hours: Double,
    val status: String
)
