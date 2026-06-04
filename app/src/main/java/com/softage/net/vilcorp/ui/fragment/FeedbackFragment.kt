package com.softage.net.vilcorp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softage.net.vilcorp.R
import com.softage.net.vilcorp.databinding.FragmentFeedbackBinding
import com.softage.net.vilcorp.databinding.FragmentOrderDetailsBinding
import com.softage.net.vilcorp.databinding.ItemRequirementRowBinding
import com.softage.net.vilcorp.network.ApiClient
import com.softage.net.vilcorp.network.ApiInterface
import com.softage.net.vilcorp.util.CustomProgressDialog
import com.softage.net.vilcorp.util.ToastHelper
import com.softage.net.vilcorp.util.Utility
import org.json.JSONObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FeedbackFragment : Fragment() {
    private var binding: FragmentFeedbackBinding?= null
    private var mApiInterface: ApiInterface? = null
    private var mUtil: Utility? = null
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedbackBinding.inflate(layoutInflater)
        mApiInterface = ApiClient.getRetrofit().create(ApiInterface::class.java)
        mUtil = Utility(requireContext())
        setupRows(binding!!)
        binding!!.btnCancel.setOnClickListener {
            clearForm(binding!!)
        }
        val stage = arguments?.getBoolean("stage", false)
        Log.d("stage",stage.toString())
        if (stage==true){
            binding!!.mainLv.visibility = View.GONE
            binding!!.tvTxt.visibility = View.VISIBLE
            binding!!.lvSelfie.visibility = View.VISIBLE
        }
        // SUBMIT BUTTON
        binding!!.btnDeliver.setOnClickListener {

//            if (!isFormValid(binding!!)) {
//                ToastHelper().snackBar(
//                    requireActivity().findViewById(android.R.id.content),
//                    "Please fill all fields"
//                )
//                return@setOnClickListener
//            }

            val data = getFormData(binding!!)
            val jsonObject = JSONObject(data)

            callFeedbackApi(jsonObject)
        }

        binding!!.btnCancel2.setOnClickListener {
            val fragment = HomeFragment()

            val bundle = Bundle()

            fragment.arguments = bundle

            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment) // 👈 your container id
                .addToBackStack(null)
                .commit()
        }

        // SUBMIT BUTTON
        binding!!.btnDeliver2.setOnClickListener {
            val bottomSheet = SelfieFormCloseSheet()
            bottomSheet.show(parentFragmentManager, "SelfieFormCloseSheet")
        }
        return binding!!.root
    }

    private fun setupRows(binding: FragmentFeedbackBinding) {

        val titles = listOf(
            "Voice Post-paid CUG connection",
            "Vodafone Dongles 3G/4G MiFi",
            "Mobile Call Recording Solutions",
            "M2M",
            "Internet leased Line 1:1",
            "SIP",
            "Toll Free Number 1800",
            "Vi Business true telco grade advanced CPaaS solution",
            "MPLS",
            "Vodafone Idea Location Tracker",
            "Cloud services",
            "Office365",
            "Google Workspace",
            "Device Security Solutions",
            "Bulk SMS",
            "Data Analytics",
            "Leads Feedback"
        )

        val rows = listOf(
            binding.row1, binding.row2, binding.row3, binding.row4,
            binding.row5, binding.row6, binding.row7, binding.row8,
            binding.row9, binding.row10, binding.row11, binding.row12,
            binding.row13, binding.row14, binding.row15, binding.row16,binding.row17
        )

        for (i in rows.indices) {
            val rowBinding = ItemRequirementRowBinding.bind(rows[i].root)

            rowBinding.tvTitle.text = titles[i]

            // YES / NO mutual selection
            rowBinding.cbYes.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) rowBinding.cbNo.isChecked = false
            }

            rowBinding.cbNo.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) rowBinding.cbYes.isChecked = false
            }
        }
    }

    private fun getRowBinding(view: View): ItemRequirementRowBinding {
        return ItemRequirementRowBinding.bind(view)
    }
    private fun clearForm(binding: FragmentFeedbackBinding) {

        val rows = listOf(
            binding.row1, binding.row2, binding.row3, binding.row4,
            binding.row5, binding.row6, binding.row7, binding.row8,
            binding.row9, binding.row10, binding.row11, binding.row12,
            binding.row13, binding.row14, binding.row15, binding.row16,binding.row17
        )

        rows.forEach {
            val row = getRowBinding(it.root)
            row.cbYes.isChecked = false
            row.cbNo.isChecked = false
        }
    }

    private fun getFormData(binding: FragmentFeedbackBinding): Map<String, Any> {

        fun isYes(view: View): Boolean {
            return getRowBinding(view).cbYes.isChecked
        }

        return mapOf(
            "pjp_id" to Utility.getOrderID()!!.toInt(),

            "voice_post_paid_cug_connection" to isYes(binding.row1.root),
            "vodafone_dongles_3g_4g_mifi" to isYes(binding.row2.root),
            "mobile_call_recording_solutions" to isYes(binding.row3.root),
            "m2m" to isYes(binding.row4.root),
            "internet_leased_line_1_1" to isYes(binding.row5.root),
            "sip" to isYes(binding.row6.root),
            "toll_free_number_1800" to isYes(binding.row7.root),
            "vi_business_true_telco_grade_advanced_cpaas_solution" to isYes(binding.row8.root),
            "mpls" to isYes(binding.row9.root),
            "vodafone_idea_location_tracker" to isYes(binding.row10.root),
            "cloud_services" to isYes(binding.row11.root),
            "office365" to isYes(binding.row12.root),
            "google_workspace" to isYes(binding.row13.root),
            "device_security_solutions" to isYes(binding.row14.root),
            "bulk_sms" to isYes(binding.row15.root),
            "data_analytics" to isYes(binding.row16.root),

            // extra field in your JSON
            "leads_feedback" to isYes(binding.row17.root),
        )
    }
    private fun isFormValid(binding: FragmentFeedbackBinding): Boolean {

        val rows = listOf(
            binding.row1, binding.row2, binding.row3, binding.row4,
            binding.row5, binding.row6, binding.row7, binding.row8,
            binding.row9, binding.row10, binding.row11, binding.row12,
            binding.row13, binding.row14, binding.row15, binding.row16,binding.row17
        )

        for (rowView in rows) {
            val row = ItemRequirementRowBinding.bind(rowView.root)

            // if neither YES nor NO is selected → invalid
            if (!row.cbYes.isChecked && !row.cbNo.isChecked) {
                return false
            }
        }

        return true
    }

    fun callFeedbackApi(json: JSONObject) {

        progressDialog.start("Submitting Feedback...")

        val requestBody = json.toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

        val call = mApiInterface!!.submitPjpFeedback("Bearer ${Utility.token}",requestBody)

        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                progressDialog.stop()

                if (response.isSuccessful) {
                    val res = response.body()?.string()
                    println("SUCCESS: $res")

                    ToastHelper().snackBar(
                        requireActivity().findViewById(android.R.id.content),
                        "Feedback Submitted Successfully"
                    )
                    binding!!.mainLv.visibility = View.GONE
                    binding!!.tvTxt.visibility = View.VISIBLE
                    binding!!.lvSelfie.visibility = View.VISIBLE



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

}