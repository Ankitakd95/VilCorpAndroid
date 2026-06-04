package com.softage.net.vilcorp.ui.activity.onboarding

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.softage.net.vilcorp.R
import com.softage.net.vilcorp.databinding.ActivityForgotPasswordBinding
import com.softage.net.vilcorp.databinding.ActivityVerifyOtpactivityBinding
import com.softage.net.vilcorp.helper.JsonHelper
import com.softage.net.vilcorp.model.login.ForgotPassResponse
import com.softage.net.vilcorp.network.ApiClient
import com.softage.net.vilcorp.network.ApiInterface
import com.softage.net.vilcorp.util.CustomProgressDialog
import com.softage.net.vilcorp.util.NetworkHelper
import com.softage.net.vilcorp.util.ToastHelper
import com.softage.net.vilcorp.util.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyOTPActivity : AppCompatActivity() {
    private var binding: ActivityVerifyOtpactivityBinding? = null
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private var countDownTimer: CountDownTimer? = null
    private var mApiInterface: ApiInterface? = null
    private var mUtil: Utility? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mApiInterface = ApiClient.getRetrofit().create(ApiInterface::class.java)
        mUtil = Utility(this)
        startResendTimer()
        binding!!.btnVerify.setOnClickListener {
           doValidation()
        }
        binding!!.tvResendNow.setOnClickListener {

            // 🔥 Call your resend OTP API here
            doResesnd()

            // restart timer

        }
    }
    private fun doValidation() {
        if (NetworkHelper.isConnected(applicationContext)) {
            if (binding!!.customOtpView.text.toString().trim().isEmpty()) {
                ToastHelper().snackBar(findViewById(android.R.id.content), getString(R.string.enter_valid_OTP))

            } else {
                doRequest(binding!!.customOtpView.text.toString().trim())
            }
        } else {
            ToastHelper().snackBar(findViewById(android.R.id.content), getString(R.string.no_network))
        }
    }

    private fun doRequest(OTP:String) {
        progressDialog.start("Verifying OTP...")
        val call: Call<ForgotPassResponse> = mApiInterface!!.verifyOTP(JsonHelper.verifyOTP(Utility.getUserId(),OTP))
        call.enqueue(object : Callback<ForgotPassResponse?> {
            override fun onResponse(
                call: Call<ForgotPassResponse?>,
                response: Response<ForgotPassResponse?>
            ) {
                progressDialog.stop()
                if (response.code() == 200) {
                    assert(response.body() != null)
                    ToastHelper().snackBar(
                        findViewById(android.R.id.content),
                        response.body()!!.message.toString()
                    )

                    val intent = Intent(this@VerifyOTPActivity, ResetPasswordActivity::class.java)
                    startActivity(intent)

                }  else{
                    ToastHelper().snackBar(findViewById(android.R.id.content),"Failed , Please try again")
                }
            }

            override fun onFailure(call: Call<ForgotPassResponse?>, t: Throwable) {
                progressDialog.stop()
                ToastHelper().snackBar(findViewById(android.R.id.content), t.message)
                call.cancel()
            }
        })
    }
    private fun doResesnd() {
        progressDialog.start("Sending OTP...")
        val call: Call<ForgotPassResponse> = mApiInterface!!.resendOTP(JsonHelper.forgotPass(Utility.getUserId()))
        call.enqueue(object : Callback<ForgotPassResponse?> {
            override fun onResponse(
                call: Call<ForgotPassResponse?>,
                response: Response<ForgotPassResponse?>
            ) {
                progressDialog.stop()
                if (response.code() == 200) {
                    assert(response.body() != null)
                    ToastHelper().snackBar(
                        findViewById(android.R.id.content),
                        response.body()!!.message.toString()
                    )

                    binding!!.tvResendOtp.visibility = View.VISIBLE
                    binding!!.tvResendNow.visibility = View.GONE
                    startResendTimer()
                }  else{
                    ToastHelper().snackBar(findViewById(android.R.id.content),"Failed , Please try again")
                }
            }

            override fun onFailure(call: Call<ForgotPassResponse?>, t: Throwable) {
                progressDialog.stop()
                ToastHelper().snackBar(findViewById(android.R.id.content), t.message)
                call.cancel()
            }
        })
    }
    private fun startResendTimer() {

        binding!!.tvResendNow.visibility = View.GONE
        binding!!.tvResendOtp.visibility = View.VISIBLE

        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                val seconds = millisUntilFinished / 1000
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60

                binding!!.tvResendOtp.text =
                    "Resend OTP in %02d:%02d".format(minutes, remainingSeconds)
            }

            override fun onFinish() {
                binding!!.tvResendOtp.visibility = View.GONE
                binding!!.tvResendNow.visibility = View.VISIBLE
            }
        }.start()
    }
}