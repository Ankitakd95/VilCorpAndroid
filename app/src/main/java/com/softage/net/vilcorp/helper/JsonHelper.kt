package com.softage.net.vilcorp.helper

import com.google.gson.JsonArray
import com.google.gson.JsonObject


object JsonHelper {
    fun getLoginJson(username: String? , password: String?,fcm_token:String? ): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("username", username)
        jsonObject.addProperty("password", password)
        jsonObject.addProperty("fcm_token", fcm_token)
        return jsonObject.toString()
    }

    fun getRegister(username: String?, email: String?, password: String?,name: String?,phone:String?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("username", username)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("password", password)
        jsonObject.addProperty("name", name)
        jsonObject.addProperty("phone", phone)
        return jsonObject.toString()
    }

    fun getComment(doc_id: String?, comment: String?, collab_id: String?,emails: List<String>?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("doc_id", doc_id)
        jsonObject.addProperty("comment", comment)
        jsonObject.addProperty("collab_id", collab_id)
        val emailArray = JsonArray()
        emails?.forEach { email ->
            emailArray.add(email)
        }
        jsonObject.add("emails", emailArray)
        return jsonObject.toString()
    }

    fun verifyOTP(username: String?,passkey:String?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("username", username)
        jsonObject.addProperty("passkey", passkey)
        return jsonObject.toString()
    }

    fun forgotPass(username: String?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("username", username)
        return jsonObject.toString()
    }

    fun resetPass(username: String?,password:String?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("username", username)
        jsonObject.addProperty("password", password)
        return jsonObject.toString()
    }
    fun getRemoveFavDoc(doc_id: String?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("doc_id", doc_id)
        return jsonObject.toString()
    }


    fun getMarkRead(notification_id: String?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("notification_id", notification_id)
        return jsonObject.toString()
    }

    fun saveMeetImage(pjp_id: Int?, meeting_picture_url: String?,meeting_picture_key: String?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("pjp_id", pjp_id)
        jsonObject.addProperty("meeting_picture_url", meeting_picture_url)
        jsonObject.addProperty("meeting_picture_key", meeting_picture_key)
        return jsonObject.toString()
    }
    fun cancelOrder(latitude: Double, longitude: Double,reason:String): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("latitude", latitude)
        jsonObject.addProperty("longitude", longitude)
        jsonObject.addProperty("reason", reason)
        return jsonObject.toString()
    }
    fun getData(folder_id: String?, search_text: String?,recent:Boolean?,collab_id:String?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("folder_id", folder_id)
        jsonObject.addProperty("search_text", search_text)
        jsonObject.addProperty("recent", recent)
        jsonObject.addProperty("collab_id", collab_id)
        return jsonObject.toString()
    }

    fun getRenameDoc(doc_id: String?,collab_id:String?, new_name: String?, rename_all_versions:Boolean): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("doc_id", doc_id)
        jsonObject.addProperty("collab_id", collab_id)
        jsonObject.addProperty("new_name", new_name)
        jsonObject.addProperty("rename_all_versions",rename_all_versions)
        return jsonObject.toString()
    }

    fun getPreSignedUrl(file_type: String?,file_name:String?,presignedURLFor:String?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("file_type", file_type)
        jsonObject.addProperty("file_name", file_name)
        jsonObject.addProperty("presignedURLFor", presignedURLFor)
        return jsonObject.toString()
    }

    fun punchIn(url: String?,key:String?,image_type:String?,latitude:Double?,longitude:Double?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("url", url)
        jsonObject.addProperty("key", key   )
        jsonObject.addProperty("image_type", image_type)
        jsonObject.addProperty("latitude", latitude)
        jsonObject.addProperty("longitude", longitude)
        return jsonObject.toString()
    }

    fun getRemoveCollab(collab_id:String?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("collab_id", collab_id)
        return jsonObject.toString()
    }

    fun getPermDelete(doc_id: List<String>?,folder_id:List<String>?): String {
        val jsonObject = JsonObject()
        val docArray = JsonArray()
        doc_id?.forEach { docid ->
            docArray.add(docid)
        }
        jsonObject.add("docs_ids", docArray)
        val foldArray = JsonArray()
        folder_id?.forEach { foldid ->
            foldArray.add(foldid)
        }

        jsonObject.add("folder_ids", foldArray)
        return jsonObject.toString()
    }

    fun getRestore(docs_id:String,fol_id:String,new_folder_id:String,restore_option:String,doc_id: List<String>?,folder_id:List<String>?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("docs_id", docs_id)
        jsonObject.addProperty("folder_id", fol_id)
        jsonObject.addProperty("new_folder_id", new_folder_id)
        jsonObject.addProperty("restore_option", restore_option)
        val docArray = JsonArray()
        doc_id?.forEach { docid ->
            docArray.add(docid)
        }
        jsonObject.add("doc_ids", docArray)
        val foldArray = JsonArray()
        folder_id?.forEach { foldid ->
            foldArray.add(foldid)
        }

        jsonObject.add("folder_ids", foldArray)
        return jsonObject.toString()
    }

    fun getRevertVersion(doc_id: String?,version:String?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("doc_id", doc_id)
        jsonObject.addProperty("version", version)

        return jsonObject.toString()
    }

    fun getFolderList(is_root: Boolean?,folder_uid:String?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("is_root", is_root)
        jsonObject.addProperty("folder_uid", folder_uid)

        return jsonObject.toString()
    }

    fun getFileView(file_id: String?,folder_id:String?,collab_id:String?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("file_id", file_id)
        jsonObject.addProperty("folder_id", folder_id)
        jsonObject.addProperty("collab_id", collab_id)
        return jsonObject.toString()
    }

    fun createCollection(collection_name: String?,collection_id:String?,rename_collection:Boolean?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("collection_name", collection_name)
        jsonObject.addProperty("collection_id", collection_id)
        jsonObject.addProperty("rename_collection", rename_collection)
        return jsonObject.toString()
    }

    fun deleteCollection(collection_id:String?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("collection_id", collection_id)
        return jsonObject.toString()
    }

    fun createCollaborationJson(
        folder_id: String?,
        collab_id: String?,
        doc_id: String?,
        emails: List<String>?,
        can_edit: Boolean,
        can_view: Boolean,
        expiration_date: String?
    ): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("folder_id", folder_id)
        jsonObject.addProperty("collab_id", collab_id)
        jsonObject.addProperty("doc_id", doc_id)

        // Add emails as a JSON array
        val emailArray = JsonArray()
        emails?.forEach { email ->
            emailArray.add(email)
        }
        jsonObject.add("emails", emailArray)

        jsonObject.addProperty("can_edit", can_edit)
        jsonObject.addProperty("can_view", can_view)
        jsonObject.addProperty("expiration_date", expiration_date)
        return jsonObject.toString()
    }

    fun addUser(
        Name: String?,
        UserID: String?,
        Password: String?,
        Language: String?,
        UserType: String?,
        ImageFilePath: String?
    ): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("Name", Name)
        jsonObject.addProperty("UserID", UserID)
        jsonObject.addProperty("Password", Password)
        jsonObject.addProperty("Language", Language)
        jsonObject.addProperty("UserType", UserType)
        jsonObject.addProperty("ImageFilePath", ImageFilePath)
        return jsonObject.toString()
    }

    fun editAddress(
        id: Int,
        mobile: String?,
        Name: String?,
        address1: String?,
        address2: String?,
        city: String?,
        landmark: String?,
        state: String?,
        postalCode: String?,
        country: String?
    ): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("id", id)
        jsonObject.addProperty("mobile", mobile)
        jsonObject.addProperty("Name", Name)
        jsonObject.addProperty("address1", address1)
        jsonObject.addProperty("address2", address2)
        jsonObject.addProperty("city", city)
        jsonObject.addProperty("landmark", landmark)
        jsonObject.addProperty("state", state)
        jsonObject.addProperty("postalCode", postalCode)
        jsonObject.addProperty("country", country)
        return jsonObject.toString()
    }

    fun changePass(old_password: String?, new_password: String?, confirm_password: String?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("old_password", old_password)
        jsonObject.addProperty("new_password", new_password)
        jsonObject.addProperty("confirm_password", confirm_password)
        return jsonObject.toString()
    }
    fun editProfile(name: String?, email: String?, phone: String?): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("name", name)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("phone", phone)
        return jsonObject.toString()
    }
}
