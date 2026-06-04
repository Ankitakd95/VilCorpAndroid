package com.softage.net.vilcorp.model.attendance

import com.google.gson.annotations.SerializedName

data class PresignedUrlSuccess(

    @SerializedName("upload_url" ) var uploadUrl : String? = null,
    @SerializedName("file_url"   ) var fileUrl   : String? = null,
    @SerializedName("key"        ) var key       : String? = null
)
