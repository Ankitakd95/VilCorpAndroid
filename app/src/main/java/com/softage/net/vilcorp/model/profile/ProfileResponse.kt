package com.softage.net.vilcorp.model.profile

data class ProfileResponse(
    val status: Boolean,
    val error: Boolean,
    val message: String,
    val data: ProfileData
)

data class ProfileData(
    val username: String,
    val full_name: String,
    val email: String,
    val mobile: String,
    val designation: String,
    val department: String,
    val branch: String,
    val branch_id: String,
    val city: String,
    val circle: String,
    val reporting_manager_name: String,
    val reporting_manager_username: String,
    val shift: String,
    val employment_type: String

)
