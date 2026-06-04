package com.softage.net.vilcorp.ui.activity.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.softage.net.vilcorp.R
import com.softage.net.vilcorp.databinding.ActivityForgotPasswordBinding
import com.softage.net.vilcorp.databinding.ActivityResetPasswordBinding
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

class ResetPasswordActivity : AppCompatActivity() {
    private var binding: ActivityResetPasswordBinding? = null
    private val progressDialog by lazy { CustomProgressDialog(this) }

    private var mApiInterface: ApiInterface? = null
    private var mUtil: Utility? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mApiInterface = ApiClient.getRetrofit().create(ApiInterface::class.java)
        mUtil = Utility(this)
        binding!!.btnSubmit.setOnClickListener {
            doValidation()
        }

    }
    private fun doValidation() {
        if (NetworkHelper.isConnected(applicationContext)) {
            if (binding!!.etPass.text.toString().trim().isEmpty()) {
                ToastHelper().snackBar(findViewById(android.R.id.content), getString(R.string.enter_new_password))

            } else  if (binding!!.etConfirmPass.text.toString().trim().isEmpty()) {
                ToastHelper().snackBar(findViewById(android.R.id.content), getString(R.string.confirm_new_password))

            } else if (binding!!.etPass.text.toString().trim() != binding!!.etConfirmPass.text.toString().trim()){
                ToastHelper().snackBar(findViewById(android.R.id.content), getString(R.string.pass_not_same))
            }else {
                doRequest(binding!!.etPass.text.toString().trim())
            }
        } else {
            ToastHelper().snackBar(findViewById(android.R.id.content), getString(R.string.no_network))
        }
    }

    private fun doRequest(password:String) {
        progressDialog.start("Verifying OTP...")
        val call: Call<ForgotPassResponse> = mApiInterface!!.resetPas(JsonHelper.resetPass(Utility.getUserId(),password))
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

                    val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()

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
}