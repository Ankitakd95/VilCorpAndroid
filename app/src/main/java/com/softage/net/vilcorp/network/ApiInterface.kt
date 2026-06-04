package com.softage.net.vilcorp.network

import com.softage.net.vilcorp.model.attendance.AttendanceStatus
import com.softage.net.vilcorp.model.attendance.HistorySuccess
import com.softage.net.vilcorp.model.attendance.PresignedUrlSuccess
import com.softage.net.vilcorp.model.attendance.PunchInSuccess
import com.softage.net.vilcorp.model.leads.PjpStageSuccess
import com.softage.net.vilcorp.model.leads.TodaysOrderSuccess
import com.softage.net.vilcorp.model.location.LocationRequestBody
import com.softage.net.vilcorp.model.login.ForgotPassResponse
import com.softage.net.vilcorp.model.login.LoginResponse
import com.softage.net.vilcorp.model.notification.NotificationResponse
import com.softage.net.vilcorp.model.notification.ReadAllResponse
import com.softage.net.vilcorp.model.orderHistory.CancelOrderListSuccess
import com.softage.net.vilcorp.model.profile.ProfileResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface ApiInterface {


    @Multipart
    @POST("general/auth/login")
    fun userLogin(
        @Part("username") name: RequestBody,
        @Part("password") userID: RequestBody,
        @Part("fcm_token") language: RequestBody
    ): Call<LoginResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("general/auth/forget-password")
    fun forgotPass(@Body requestData: String): Call<ForgotPassResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("fulfillment/check-attendence")
    fun getAttendance(@Body requestData: String): Call<AttendanceStatus>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("general/auth/resend-otp")
    fun resendOTP(@Body requestData: String): Call<ForgotPassResponse>

    @GET("general/profile")
    fun getProfile(
        @Header("Authorization") token: String
    ): Call<ProfileResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("fulfillment/generate-real-time-log")
    fun sendLocation(
        @Header("Authorization") token: String,
        @Body body: LocationRequestBody
    ): Call<Any>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @PATCH("notification/{id}/mark-read")
    fun markNotificationRead(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<Unit>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @PATCH("notification/mark-all-read")
    fun markNotificationReadAll(
        @Header("Authorization") token: String
    ): Call<ReadAllResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("notification/list")
    fun getNotifications(
        @Header("Authorization") token: String,
        @Query("status") status : String
    ): Call<ArrayList<NotificationResponse>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("general/auth/verify-email-otp")
    fun verifyOTP(@Body requestData: String): Call<ForgotPassResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("general/auth/reset-password")
    fun resetPas(@Body requestData: String): Call<ForgotPassResponse>
//
//    @GET("get-user-notifications")
//    fun getNotifications(
//        @Query("username") username: String,
//        @Query("page") page: Int
//    ): Call<NotificationResponse>
//
//// Uncomment and adapt as necessary
//// @Multipart
//// @POST("Registration")
//// fun userRegister(
////     @Part media: MultipartBody.Part,
////     @Part("Name") name: RequestBody,
////     @Part("UserID") userID: RequestBody,
////     @Part("Language") language: RequestBody,
////     @Part("UserType") userType: RequestBody,
////     @Part("Password") password: RequestBody
//// ): Call<RegisterResponseModel>
//

//    @GET("api-user-validity/")
//    fun getPhotoCategory(): Call<CategorySucess>

//    @PUT
//    fun uploadFile(
//        @Url url: String,
//        @Header("Content-Type") contentType: String,
//        @Body body: RequestBody
//    ): Call<ResponseBody>
//
// In your ApiInterface.kt
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("fulfillment/pjp-details")
    fun submitPjpDetails(
    @Header("Authorization") auth: String,
        @Body body: RequestBody
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("fulfillment/order_details")
    fun getOrderDetails(
        @Header("Authorization") auth: String,
        @Query("order_id") stageId: String
    ): Call<TodaysOrderSuccess>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("fulfillment/pjp-stage")
    fun getStage(
        @Header("Authorization") auth: String,
        @Query("pjp_id") pjp_id: String
    ): Call<PjpStageSuccess>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("general/generate-presigned-url")
    fun getPreSignedUrl(@Header("Authorization") auth: String, @Body requestData: String): Call<PresignedUrlSuccess>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("fulfillment/today")
    fun getTodayOrder(@Header("Authorization") auth: String): Call<ArrayList<TodaysOrderSuccess>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("fulfillment/orders/history/cancelled")
    fun getCancelledOrders(@Header("Authorization") auth: String): Call<ArrayList<CancelOrderListSuccess>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("fulfillment/orders/history/delivered")
    fun getDeliveredOrders(@Header("Authorization") auth: String): Call<ArrayList<CancelOrderListSuccess>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("general/attendance/punch-in")
    fun punchIN(@Header("Authorization") auth: String, @Body requestData: String): Call<PunchInSuccess>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("fulfillment/pjp-save-image")
    fun saveMeetingImage(@Header("Authorization") auth: String, @Body requestData: String): Call<PunchInSuccess>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("general/attendance/punch-out")
    fun punchOut(@Header("Authorization") auth: String, @Body requestData: String): Call<PunchInSuccess>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("general/attendance/fetch-attendance-history")
    fun getAttendanceHistory(
        @Header("Authorization") auth: String,
        @Query("page_size") pageSize: Int,
        @Body body: Map<String, String?>
    ): Call<HistorySuccess>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("fulfillment/{order_id}/cancel")
    fun cancelOrder(
        @Header("Authorization") auth: String,
        @Path("order_id") orderID: Int,   // ✅ FIXED
        @Body body: RequestBody
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("fulfillment/pjp-feedback")
    fun submitPjpFeedback(
        @Header("Authorization") auth: String,
        @Body body: RequestBody
    ): Call<ResponseBody>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @GET("upload_output_file_url/")
//    fun getOutputUrl(@Query("tid") tid: String, @Query("file_extension") file_extension: String,@Query("content_type") content_type: String): Call<OutFileUrl>
//
//    @POST("api_task_submitted_post/")
//    fun updateDatabase(@Body body: Map<String, @JvmSuppressWildcards Any>): Call<Map<String, Any>>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("api_save_system_info/")
//    fun systemDetails(@Body requestData: Payload): Call<SystemInfoSuccess>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @GET("favorites/list/")
//    fun getFavList(@Header("Authorization") auth: String): Call<FavListResponse>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @GET("get_recent_files")
//    fun getRecentList(@Header("Authorization") auth: String): Call<HomeRecentDAO>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("reset_password/")
//    fun changePassword(@Header("Authorization") auth: String, @Body requestData: String): Call<ChangePassData>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("favorites/remove/")
//    fun removeFav(@Header("Authorization") auth: String, @Body requestData: String): Call<ChangePassData>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("rename_folder/")
//    fun getRenameFolder(@Header("Authorization") auth: String, @Body requestData: String): Call<String>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("rename_document/")
//    fun getRenameDoc(@Header("Authorization") auth: String, @Body requestData: String): Call<ChangePassData>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("deletion_api/")
//    fun getDelete(@Header("Authorization") auth: String, @Body requestData: String): Call<DeleteSuccess>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("invite_collaboration/")
//    fun getCollab(@Header("Authorization") auth: String, @Body requestData: String): Call<String>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @GET("email-autocomplete/")
//    fun getEmailList(
//        @Header("Authorization") auth: String,
//        @Query("email") email: String
//    ): Call<EmailListSuccess>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("view_file/")
//    fun viewFile(@Header("Authorization") auth: String,@Body requestData: String): Call<FileViewRes>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("get_chat_trail/")
//    fun viewComment(@Header("Authorization") auth: String,@Body requestData: String): Call<CommentSuccess>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("file_version_list/")
//    fun viewVersion(@Header("Authorization") auth: String,@Body requestData: String): Call<VersionSuccess>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("revert_to_version/")
//    fun revertVersion(@Header("Authorization") auth: String,@Body requestData: String): Call<CommentSuccess>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("create_chat_trail/")
//    fun doComment(@Header("Authorization") auth: String,@Body requestData: String): Call<CommentSuccess>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("get_data/")
//    fun getData(@Header("Authorization") auth: String, @Body requestData: String): Call<GetDataSuccess>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("delete_account/")
//    fun accountDelete(@Header("Authorization") auth: String): Call<ChangePassData>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("advance_search/?page_size=1&offset=1")
//    fun getOcrSearch(@Header("Authorization") auth: String, @Body requestData: String): Call<GetOcrSuccess>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("all_notifications/?page_size=1&offset=1")
//    fun getAllNotif(@Header("Authorization") auth: String, @Body requestData: String): Call<NotifSuccess>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("notifications/")
//    fun markRead(@Header("Authorization") auth: String, @Body requestData: String): Call<NotifSuccess>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @GET("notifications/")
//    fun getNotif(@Header("Authorization") auth: String): Call<NotifSuccess>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @GET("recycle_bin/")
//    fun getTrash(@Header("Authorization") auth: String): Call<TrashSuccess>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("permanent_delete/")
//    fun getPermanentDelete(@Header("Authorization") auth: String, @Body requestData: String): Call<DeleteSuccess>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("recycle_bin/restore/")
//    fun getRestore(@Header("Authorization") auth: String, @Body requestData: String): Call<RestoreSuccess>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("folder_children/")
//    fun getRestoreFL(@Header("Authorization") auth: String, @Body requestData: String): Call<RestoreFListSuceess>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @GET("get_collaborations/")
//    fun getCollabList(@Header("Authorization") auth: String): Call<CollabSuccess>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @GET("get_collaborations/")
//    fun getCollabListWith(@Header("Authorization") auth: String): Call<CollabSuccessWith>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("end_collaboration/")
//    fun endCollab(@Header("Authorization") auth: String, @Body requestData: String): Call<String>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("create_folder/")
//    fun createFolder(@Header("Authorization") auth: String, @Body requestData: String): Call<String>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @GET("get_collections/")
//    fun getCollection(@Header("Authorization") auth: String): Call<CollectionSuccess>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("create_collection/")
//    fun createCollection(@Header("Authorization") auth: String, @Body requestData: String): Call<String>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @POST("delete_collec_andr/")
//    fun deleteCollection(@Header("Authorization") auth: String, @Body requestData: String): Call<String>
//
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @GET("get_item_collection/")
//    fun getCollectionItems(
//        @Header("Authorization") auth: String,
//        @Query("collection_id") collection_id: String
//    ): Call<CollectionViewSucces>

}
