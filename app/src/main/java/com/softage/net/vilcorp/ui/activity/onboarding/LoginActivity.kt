package com.softage.net.vilcorp.ui.activity.onboarding

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.softage.net.vilcorp.R
import com.softage.net.vilcorp.databinding.ActivityLoginBinding
import com.softage.net.vilcorp.helper.JsonHelper
import com.softage.net.vilcorp.model.attendance.AttendanceStatus
import com.softage.net.vilcorp.model.login.LoginResponse
import com.softage.net.vilcorp.network.ApiClient
import com.softage.net.vilcorp.network.ApiInterface
import com.softage.net.vilcorp.services.LocationService
import com.softage.net.vilcorp.ui.activity.MainActivity
import com.softage.net.vilcorp.util.CustomProgressDialog
import com.softage.net.vilcorp.util.NetworkHelper
import com.softage.net.vilcorp.util.ToastHelper
import com.softage.net.vilcorp.util.Utility
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), OnClickListener {
    private var binding: ActivityLoginBinding? = null
    private val progressDialog by lazy { CustomProgressDialog(this) }

    private var mApiInterface: ApiInterface? = null
    private var mUtil: Utility? = null
    var androidId :String =""
    var fcm =""
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private val REQUIRED_PERMISSIONS = when {
        // Android 13+
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA
            )
        }
        // Android 10, 11, and 12 (API 29, 30, 31, 32)
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA
                // Notice: WRITE_EXTERNAL_STORAGE is removed here for API 29+
            )
        }
        // Android 9 and below
        else -> {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initLayout()
        mApiInterface = ApiClient.getRetrofit().create(ApiInterface::class.java)
        mUtil = Utility(this)
        androidId = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            Log.d("FCM_TOKEN", token)
            fcm = token
        }.addOnFailureListener { e ->
            Log.d("FCM_TOKEN", "Failed", e)
        }
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

                val deniedPermissions = permissions.filter { !it.value }.keys

                if (deniedPermissions.isEmpty()) {
                    // ✅ ALL permissions granted
                    Toast.makeText(this, "All permissions granted", Toast.LENGTH_SHORT).show()
                } else {

                    // Check if we can ask again
                    val shouldAskAgain = deniedPermissions.any { perm ->
                        ActivityCompat.shouldShowRequestPermissionRationale(this, perm)
                    }

                    if (shouldAskAgain) {
                        // 🔁 Ask again
                        Toast.makeText(
                            this,
                            "Permissions are required to continue",
                            Toast.LENGTH_LONG
                        ).show()

                        permissionLauncher.launch(REQUIRED_PERMISSIONS)

                    } else {
                        // 🚫 User selected "Don't ask again"
                        Toast.makeText(
                            this,
                            "Please enable permissions from Settings",
                            Toast.LENGTH_LONG
                        ).show()

                        openAppSettings()
                    }
                }
            }

        if (hasPermissions()) {

        } else {
            permissionLauncher.launch(REQUIRED_PERMISSIONS)
        }


    }

    private fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }



    private fun initLayout() {
        binding!!.btnLogin.setOnClickListener(this)
        binding!!.txtForgot.setOnClickListener(this)
        binding!!.temp.setOnClickListener(this)

    }
    private fun hasPermissions(): Boolean {
        return REQUIRED_PERMISSIONS.all { perm ->
            ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED
        }
    }
    private fun doValidation() {
        if (NetworkHelper.isConnected(applicationContext)) {
            if (binding!!.etMobile.text.toString().trim().isEmpty()) {
                ToastHelper().snackBar(findViewById(android.R.id.content), getString(R.string.enter_valid_Mobile_no))

            } else if (binding!!.etPass.text.toString().trim().isEmpty()){
                ToastHelper().snackBar(findViewById(android.R.id.content), getString(R.string.enter_valid_mobile))
            } else {
                doRequest(binding!!.etMobile.text.toString().trim(),binding!!.etPass.text.toString().trim(),fcm)
            }
        } else {
            ToastHelper().snackBar(findViewById(android.R.id.content), getString(R.string.no_network))
        }
    }

    private fun doRequest(username: String, pass: String,fcm_token:String) {
        progressDialog.start("Singing In...")
        val usernameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), username)
        val passBody = RequestBody.create("text/plain".toMediaTypeOrNull(), pass)
        val fcmBody = RequestBody.create("text/plain".toMediaTypeOrNull(), fcm_token)
        val call: Call<LoginResponse> = mApiInterface!!.userLogin(usernameBody, passBody,fcmBody)
        call.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                progressDialog.stop()
                if (response.code() == 200) {
                    assert(response.body() != null)


//                    mUtil!!.setMobile(response.body()!!.user!!.mobile.toString())
//                    mUtil!!.setEmail(response.body()!!.user!!.email.toString())
                    mUtil!!.setToken(response.body()!!.accessToken.toString())
                    mUtil!!.setIsRemotePunch(response.body()!!.isRemotePunchIn!!)
                    mUtil!!.setRemotePunchOut(response.body()!!.isRemotePunchOut!!)
                    mUtil!!.setLongitudo(response.body()!!.branchLongitude.toString())
                    mUtil!!.setLatitude(response.body()!!.branchLatitude.toString())
                    val json = decodeJwt(response.body()!!.accessToken.toString())

                    json?.let {
                        val userName = it.optString("userName")
                        val fullName = it.optString("fullName")

                        mUtil!!.setName(fullName)
                        mUtil!!.setUserId(userName)
                        Log.d("jwt",fullName+" "+userName)
                    }
                    doRequestAttendance(Utility.getUserId().toString())


                } else if (response.code()==401){
                    ToastHelper().snackBar(
                        findViewById(android.R.id.content),
                        "Invalid Username and Password"
                    )
                } else{
                    ToastHelper().snackBar(findViewById(android.R.id.content),"Failed , Please try again")
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                progressDialog.stop()
                ToastHelper().snackBar(findViewById(android.R.id.content), t.message)
                call.cancel()
            }
        })
    }
    private fun doRequestAttendance(username: String) {
        val call: Call<AttendanceStatus> = mApiInterface!!.getAttendance(JsonHelper.forgotPass(username))
        call.enqueue(object : Callback<AttendanceStatus?> {
            override fun onResponse(
                call: Call<AttendanceStatus?>,
                response: Response<AttendanceStatus?>
            ) {
                if (response.code() == 200) {
                    assert(response.body() != null)

                    mUtil!!.setAttendanceMarked(response.body()!!.markedAttendance!!)
                    mUtil!!.setIsLogin(true)
                    if (Utility.isAttendanceMarked){
                        val intent = Intent(this@LoginActivity,MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    } else{
                        val intent = Intent(this@LoginActivity,AttendanceActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }


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
    fun decodeJwt(token: String): JSONObject? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null

            var payload = parts[1]

            // 🔥 Fix padding issue
            val remainder = payload.length % 4
            if (remainder != 0) {
                payload += "=".repeat(4 - remainder)
            }

            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
            val decodedString = String(decodedBytes, Charsets.UTF_8)

            JSONObject(decodedString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.btnLogin -> {
                if (!hasPermissions()) {
                    Toast.makeText(
                        this,
                        "Please grant all permissions first",
                        Toast.LENGTH_LONG
                    ).show()
                    permissionLauncher.launch(REQUIRED_PERMISSIONS)
                    return
                }
                doValidation()

            }

            R.id.txtForgot -> {
                startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
            }


        }
    }
}