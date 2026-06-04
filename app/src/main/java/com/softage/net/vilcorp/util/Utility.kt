
package com.softage.net.vilcorp.util
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import com.softage.net.vilcorp.ui.activity.onboarding.LoginActivity


class Utility(private val mContext: Context) {
    init {
        mSharedPreferences = mContext.getSharedPreferences(
            mPrefName,
            Context.MODE_PRIVATE
        )
    }


    fun setLatitude(onDuty1: String?) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putString(Username, onDuty1)
        editor.apply()
    }
    fun setEmail(onDuty1: String?) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putString(TID, onDuty1)
        editor.apply()
    }

    fun setToken(token: String?) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun setMobile(mobile: String?) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putString(VideoUrl, mobile)
        editor.apply()
    }

    fun setLongitudo(image: String?) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putString(TxtUrl, image)
        editor.apply()
    }

    fun setIsLogin(isLogin: Boolean) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putBoolean(IS_LOGIN, isLogin)
        editor.apply()
    }

    fun setDuty(isLogin: Boolean) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putBoolean(RemoteUser, isLogin)
        editor.apply()
    }

    fun setAttendanceMarked(isLogin: Boolean) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putBoolean(IndianUser, isLogin)
        editor.apply()
    }

    fun setRemotePunchOut(isLogin: Boolean) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putBoolean(DocRejected, isLogin)
        editor.apply()
    }

    fun setIsRemotePunch(isLogin: Boolean) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putBoolean(TermChecked, isLogin)
        editor.apply()
    }




    fun setIsAddress_valid(isLogin: Boolean) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putBoolean(address_valid, isLogin)
        editor.apply()
    }

    fun setOpen_cv_validation_required(isLogin: Boolean) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putBoolean(open_cv_validation_required, isLogin)
        editor.apply()
    }

    fun setPhotography_user(isLogin: Boolean) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putBoolean(is_photography_user, isLogin)
        editor.apply()
    }

    fun setDocuments_required(isLogin: Boolean) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putBoolean(documents_required, isLogin)
        editor.apply()
    }

    fun setProfile_completed(isLogin: Boolean) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putBoolean(profile_completed, isLogin)
        editor.apply()
    }

    fun setOrderID(lang: String?) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putString(Lang, lang)
        editor.apply()
    }

    fun setUserId(userId: String?) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putString(UserId, userId)
        editor.apply()
    }

    fun setName(name: String?) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putString(Name, name)
        editor.apply()
    }

    fun setCategoryID(PunchIn: String?) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putString(CategoryID, PunchIn)
        editor.apply()
    }

    fun setUserType(userType: String?) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putString(USERTYPE, userType)
        editor.apply()
    }

    fun clear() {
        val sharedPreferences: SharedPreferences =
            mContext.getSharedPreferences(mPrefName, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        val intent = Intent(mContext, LoginActivity::class.java)
        intent.putExtra("finish", true)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        mContext.startActivity(intent)
    }

    val oS: String
        get() = try {
            val sdkVersion: String = Build.VERSION.RELEASE // e.g. sdkVersion := 8;
            "Android SDK $sdkVersion"
        } catch (ex: Exception) {
            ""
        }

    companion object {
        const val BASE_URL = "https://cirdev.softage.net/vi-softage-api/api/v1/"
        private lateinit var mSharedPreferences: SharedPreferences
        private const val mPrefName = "digiit"
        private const val IS_LOGIN = "is_login"
        private const val Lang = "lang"
        private const val USERTYPE = "user_type"
        private const val Name = "name"
        private const val CategoryID = "punch_in"
        private const val VideoUrl = "video_url"
        private const val TxtUrl = "txt_url"
        private const val TOKEN = "token"
        private const val UserId = "user_id"
        private const val Username = "onDuty"
        private const val TID = "task_id"
        private const val RemoteUser = "is_bio_enable"
        private const val IndianUser = "is_indian_user"
        private const val DocRejected = "is_doc_rejected"
        private const val TermChecked = "is_bio_checked"


        private const val address_valid = "address_valid"
        private const val open_cv_validation_required = "open_cv_validation_required"
        private const val is_photography_user = "is_photography_user"
        private const val documents_required = "documents_required"
        private const val profile_completed = "profile_completed"


        fun init(context: Context) {
            if (!::mSharedPreferences.isInitialized) {
                mSharedPreferences =
                    context.applicationContext.getSharedPreferences(
                        mPrefName,
                        Context.MODE_PRIVATE
                    )
            }
        }
        fun getLatitude(): String? {
            return mSharedPreferences.getString(Username, "")
        }

        fun getEmail(context: Context): String {
            val prefs = context.getSharedPreferences(mPrefName, Context.MODE_PRIVATE)
            return prefs.getString(TID, "") ?: ""
        }

        fun getMobile(): String? {
            return mSharedPreferences.getString(VideoUrl, "")
        }

        fun getLongitude(): String? {
            return mSharedPreferences.getString(TxtUrl, "")
        }

        val token: String?
            get() = mSharedPreferences.getString(TOKEN, "")

        fun getOrderID(): String? {
            return mSharedPreferences.getString(Lang, "")
        }

        fun getUserId(): String? {
            return mSharedPreferences.getString(UserId, "")
        }

        fun getName(): String? {
            return mSharedPreferences.getString(Name, "")
        }

        fun CategoryID(): String?{
            return mSharedPreferences.getString(CategoryID, "")
        }

        val userType: String?
            get() = mSharedPreferences.getString(USERTYPE, "")
        val isLogin: Boolean
            get() = mSharedPreferences.getBoolean(IS_LOGIN, false)

        val isAttendanceMarked: Boolean
            get() = mSharedPreferences.getBoolean(IndianUser, false)

        val isRemotePunchOut: Boolean
            get() = mSharedPreferences.getBoolean(DocRejected, false)

        val isDuty: Boolean
            get() = mSharedPreferences.getBoolean(RemoteUser, false)




        val isAddress_valid: Boolean
            get() = mSharedPreferences.getBoolean(address_valid, false)

        val isOpen_cv_validation_required: Boolean
            get() = mSharedPreferences.getBoolean(open_cv_validation_required, false)

        val isPhotography_user: Boolean
            get() = mSharedPreferences.getBoolean(is_photography_user, false)

        val isDocuments_required: Boolean
            get() = mSharedPreferences.getBoolean(documents_required, false)

        val isProfile_completed: Boolean
            get() = mSharedPreferences.getBoolean(profile_completed, false)




        val isRemotePunch: Boolean
            get() = mSharedPreferences.getBoolean(TermChecked, false)

        fun formateMilliSeccond(milliseconds: Long): String {
            var finalTimerString = ""
            var secondsString = ""
            var minuteString = ""

            // Convert total duration into time
            val hours = (milliseconds / (1000 * 60 * 60)).toInt()
            val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
            val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()

            // Add hours if there
            if (hours < 10) {
                finalTimerString = "0$hours:"
            }

            // Prepending 0 to seconds if it is one digit
            minuteString = if (minutes < 10) {
                "0$minutes"
            } else {
                "" + minutes
            }

            // Prepending 0 to seconds if it is one digit
            secondsString = if (seconds < 10) {
                "0$seconds"
            } else {
                "" + seconds
            }
            finalTimerString = "$finalTimerString$minuteString:$secondsString"

            //      return  String.format("%02d Min, %02d Sec",
            //                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
            //                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
            //                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));

            // return timer string
            return finalTimerString
        }
    }
}