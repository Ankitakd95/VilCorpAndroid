package com.softage.net.vilcorp.model.notification

data class NotificationResponse(
    val id: Int,
    val title: String,
    val message: String,
    val remarks: String,
    var is_read: Boolean,
    val date: String,
    val relative_time: String
)
