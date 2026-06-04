package com.softage.net.vilcorp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.softage.net.vilcorp.R
import com.softage.net.vilcorp.helper.JsonHelper
import com.softage.net.vilcorp.model.attendance.AttendanceStatus
import com.softage.net.vilcorp.model.login.ForgotPassResponse
import com.softage.net.vilcorp.network.ApiClient
import com.softage.net.vilcorp.network.ApiInterface
import com.softage.net.vilcorp.ui.activity.onboarding.AttendanceActivity
import com.softage.net.vilcorp.ui.activity.onboarding.LoginActivity
import com.softage.net.vilcorp.ui.activity.onboarding.VerifyOTPActivity
import com.softage.net.vilcorp.util.NetworkHelper
import com.softage.net.vilcorp.util.ToastHelper
import com.softage.net.vilcorp.util.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {
    private val SPLASH_DISPLAY_LENGTH = 1000
    private var mApiInterface: ApiInterface? = null
    private var mUtil: Utility? = null
    private lateinit var activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>
    private var appUpdateManager: AppUpdateManager? = null
    var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mUtil = Utility(this)
        mApiInterface = ApiClient.getRetrofit().create(ApiInterface::class.java)
        appUpdateManager = AppUpdateManagerFactory.create(this)
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result: ActivityResult ->
            // handle callback
            if (result.resultCode != RESULT_OK) {
                Toast.makeText(
                    applicationContext,
                    "You must update to use this app.",
                    Toast.LENGTH_LONG
                ).show()
                checkForUpdate()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        checkForUpdate()
        // Prompt for authentication if not already authenticated


    }

    private fun checkForUpdate() {
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager!!.getAppUpdateInfo()

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                appUpdateManager!!.startUpdateFlowForResult( // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,  // an activity result launcher registered via registerForActivityResult
                    activityResultLauncher,  // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                    // flexible updates.
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                )
                // Request the update.
            } else {
                doRequest(Utility.getUserId().toString())
            }
        }.addOnFailureListener { e: Exception? ->
            // Toast.makeText(this, "Failed to check for updates.", Toast.LENGTH_SHORT).show();
            doRequest(Utility.getUserId().toString())
        }
    }

    private fun proceedToMainContent() {
        if (NetworkHelper.isConnected(applicationContext)) {
            handler = Handler()
            handler!!.postDelayed({
                if (NetworkHelper.isConnected(applicationContext)) {
                    if (Utility.isLogin) {
                        if (Utility.isAttendanceMarked){
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        } else{
                            startActivity(Intent(this@SplashActivity, AttendanceActivity::class.java))
                        }

                    } else {
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    }
                    finish()
                } else {
                    ToastHelper().snackBar(
                        findViewById(android.R.id.content),
                        getResources().getString(R.string.no_network)
                    )
                }
            }, SPLASH_DISPLAY_LENGTH.toLong())
        } else {
            ToastHelper().snackBar(
                findViewById(android.R.id.content),
                getResources().getString(R.string.no_network)
            )
        }
    }

    private fun doRequest(username: String) {
        val call: Call<AttendanceStatus> = mApiInterface!!.getAttendance(JsonHelper.forgotPass(username))
        call.enqueue(object : Callback<AttendanceStatus?> {
            override fun onResponse(
                call: Call<AttendanceStatus?>,
                response: Response<AttendanceStatus?>
            ) {
                if (response.code() == 200) {
                    assert(response.body() != null)

                    mUtil!!.setAttendanceMarked(response.body()!!.markedAttendance!!)
                    proceedToMainContent()


                } else if (response.code()==404||response.code()==405){
                    mUtil!!.clear()
                    mUtil!!.setAttendanceMarked(false)
                    finish()
                } else{
                    ToastHelper().snackBar(findViewById(android.R.id.content),"Failed , Please reopen app")
                }
            }

            override fun onFailure(call: Call<AttendanceStatus?>, t: Throwable) {
                ToastHelper().snackBar(findViewById(android.R.id.content), t.message)
                call.cancel()
            }
        })
    }
}