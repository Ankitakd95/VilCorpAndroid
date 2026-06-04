package com.softage.net.vilcorp.ui.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.softage.net.vilcorp.R
import com.softage.net.vilcorp.adapter.LeadAdapter
import com.softage.net.vilcorp.databinding.FragmentHomeBinding
import com.softage.net.vilcorp.databinding.FragmentPjpDetailsBinding
import com.softage.net.vilcorp.network.ApiClient
import com.softage.net.vilcorp.network.ApiInterface
import com.softage.net.vilcorp.util.CustomProgressDialog
import com.softage.net.vilcorp.util.Utility
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar


class PjpDetailsFragment : Fragment() {

    private var binding: FragmentPjpDetailsBinding?= null
    private var mApiInterface: ApiInterface? = null
    private var mUtil: Utility? = null
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPjpDetailsBinding.inflate(layoutInflater)
        mApiInterface = ApiClient.getRetrofit().create(ApiInterface::class.java)
        mUtil = Utility(requireContext())
        setupDropdowns()
        binding!!.etCloserDate.setOnClickListener { showDatePicker(binding!!.etCloserDate) }
        binding!!.etFollowDate.setOnClickListener { showDatePicker(binding!!.etFollowDate) }
        binding!!.btnSubmit.setOnClickListener {
            binding!!.btnSubmit.setOnClickListener {
                if (!isFormValid()) {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val json = JSONObject().apply {
                    put("pjp_id", Utility.getOrderID()!!.toInt())
                    put("existing_service_provider", binding!!.spServiceProvider.text.toString())
                    put("existing_cocp_count", binding!!.etCocpCount.text.toString())
                    put("plan_details", binding!!.etPlanDetails.text.toString())
                    put("employees_base", binding!!.etEmployeesBase.text.toString())
                    put("desk_potential", binding!!.spDeskPotential.text.toString())
                    put("desk_activity", binding!!.spDeskActivity.text.toString())
                    put("follow_required", binding!!.spFollowRequired.text.toString())
                    put("tentative_closer_date", binding!!.etCloserDate.text.toString())
                    put("support_required", binding!!.etSupport.text.toString())
                    put("follow_up_date", binding!!.etFollowDate.text.toString())
                    put("call_out_come", binding!!.spCallOutcome.text.toString())
                    put("remarks", binding!!.etRemarks.text.toString())
                    put("order_closed_type", binding!!.spOrderType.text.toString())
                    put("closed_number", binding!!.etClosedNumber.text.toString())
                    put("activation_type", binding!!.spActivationType.text.toString())
                    put("account_type", binding!!.spAccountType.text.toString())
                    put("account_category", binding!!.spAccountCategory.text.toString())
                    put("funnel_count", binding!!.etFunnelCount.text.toString())
                }

                callPjpDetailsApi(json)
            }
        }
        return binding!!.root
    }
    fun setupDropdowns() {

        setAdapter(binding!!.spServiceProvider, listOf("VI", "JIO", "BSNL", "Other"))

        setAdapter(binding!!.spDeskPotential, listOf("Yes", "No"))

        setAdapter(binding!!.spDeskActivity, listOf("Desking", "Non-desking"))

        setAdapter(binding!!.spFollowRequired, listOf("Yes", "No"))

        setAdapter(binding!!.spCallOutcome, listOf(
            "Prospect",
            "Seek Permission/Appointment",
            "Revisit Required",
            "No Entry",
            "Meeting Done & Details Captured",
            "Untraceable",
            "Sold Nos",
            "Incomplete Remark",
            "WFH No Entry",
            "Company Closed Shifted"
        ))

        setAdapter(binding!!.spOrderType, listOf("COCP", "IOT", "IOIP", "CTS"))

        setAdapter(binding!!.spActivationType, listOf("Voice", "MBB", "IOT", "CTS"))

        setAdapter(binding!!.spAccountType, listOf("HQ", "NHQ", "GOVT", "SME", "SOHO"))

        setAdapter(binding!!.spAccountCategory, listOf("CSA", "NSA", "GOVT", "SME", "SOHO"))
    }

    fun setAdapter(view: AutoCompleteTextView, list: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, list)
        view.setAdapter(adapter)
    }
    fun showDatePicker(editText: EditText) {
        val cal = Calendar.getInstance()

        DatePickerDialog(requireContext(),
            { _, y, m, d ->

                val date = String.format("%04d-%02d-%02d", y, m + 1, d)
                editText.setText(date)

            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    private fun openOrderDetailsFragment() {

        val fragment = FeedbackFragment()

        val bundle = Bundle()


        fragment.arguments = bundle

        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment) // 👈 your container id
            .addToBackStack(null)
            .commit()
    }

    fun callPjpDetailsApi(json: JSONObject) {

        progressDialog.start("Saving Details...")

        val requestBody = json.toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

        val call = mApiInterface!!.submitPjpDetails("Bearer ${Utility.token}",requestBody)

        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                progressDialog.stop()

                if (response.isSuccessful) {
                    val res = response.body()?.string()
                    println("SUCCESS: $res")

                    openOrderDetailsFragment()
                } else {
                    println("ERROR: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialog.stop()
                println("FAIL: ${t.message}")
            }
        })
    }
    private fun isFormValid(): Boolean {

        if (binding!!.spServiceProvider.text.isNullOrEmpty()) return false
        if (binding!!.etCocpCount.text.isNullOrEmpty()) return false
        if (binding!!.etPlanDetails.text.isNullOrEmpty()) return false
        if (binding!!.etEmployeesBase.text.isNullOrEmpty()) return false
        if (binding!!.spDeskPotential.text.isNullOrEmpty()) return false
        if (binding!!.spDeskActivity.text.isNullOrEmpty()) return false
        if (binding!!.spFollowRequired.text.isNullOrEmpty()) return false
        if (binding!!.etCloserDate.text.isNullOrEmpty()) return false
        if (binding!!.etSupport.text.isNullOrEmpty()) return false
        if (binding!!.etFollowDate.text.isNullOrEmpty()) return false
        if (binding!!.spCallOutcome.text.isNullOrEmpty()) return false
        if (binding!!.etRemarks.text.isNullOrEmpty()) return false
        if (binding!!.spOrderType.text.isNullOrEmpty()) return false
        if (binding!!.etClosedNumber.text.isNullOrEmpty()) return false
        if (binding!!.spActivationType.text.isNullOrEmpty()) return false
        if (binding!!.spAccountType.text.isNullOrEmpty()) return false
        if (binding!!.spAccountCategory.text.isNullOrEmpty()) return false
        if (binding!!.etFunnelCount.text.isNullOrEmpty()) return false

        return true
    }
}