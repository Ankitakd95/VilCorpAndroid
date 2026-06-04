package com.softage.net.vilcorp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.softage.net.vilcorp.R
import com.softage.net.vilcorp.adapter.LeadAdapter
import com.softage.net.vilcorp.databinding.FragmentHomeBinding
import com.softage.net.vilcorp.databinding.FragmentProfileBinding
import com.softage.net.vilcorp.model.profile.ProfileData
import com.softage.net.vilcorp.model.profile.ProfileResponse
import com.softage.net.vilcorp.network.ApiClient
import com.softage.net.vilcorp.network.ApiInterface
import com.softage.net.vilcorp.util.CustomProgressDialog
import com.softage.net.vilcorp.util.NetworkHelper.findViewById
import com.softage.net.vilcorp.util.ToastHelper
import com.softage.net.vilcorp.util.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {
    private var binding: FragmentProfileBinding?= null
    private var mApiInterface: ApiInterface? = null
    private var mUtil: Utility? = null
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)
        mApiInterface = ApiClient.getRetrofit().create(ApiInterface::class.java)
        mUtil = Utility(requireContext())
        getProfile()
        return binding!!.root
    }
    private fun getProfile() {

        progressDialog.start("Loading Profile...")

        val call: Call<ProfileResponse> =
            mApiInterface!!.getProfile("Bearer ${Utility.token}")

        call.enqueue(object : Callback<ProfileResponse?> {

            override fun onResponse(
                call: Call<ProfileResponse?>,
                response: Response<ProfileResponse?>
            ) {
                progressDialog.stop()

                if (response.code() == 200 && response.body() != null) {

                    val data = response.body()!!.data

                    binding!!.tvName.text = data.full_name
                    binding!!.tvUsername.text = "@${data.username}"

                    setRow(binding!!.rowEmail.root, "Email", data.email)
                    setRow(binding!!.rowMobile.root, "Mobile", data.mobile)
                    setRow(binding!!.rowDesignation.root, "Designation", data.designation)
                    setRow(binding!!.rowDepartment.root, "Department", data.department)
                    setRow(binding!!.rowBranch.root, "Branch", "${data.branch} (${data.branch_id})")
                    setRow(binding!!.rowCity.root, "City", data.city)
                    setRow(binding!!.rowManager.root, "Manager",
                        "${data.reporting_manager_name} (${data.reporting_manager_username})")
                    setRow(binding!!.rowShift.root, "Shift", data.shift)
                    setRow(binding!!.rowEmployment.root, "Employment Type", data.employment_type)

                } else {
                    ToastHelper().snackBar(
                        requireActivity().findViewById(android.R.id.content),
                        "Failed, Please try again"
                    )
                }
            }

            override fun onFailure(call: Call<ProfileResponse?>, t: Throwable) {
                progressDialog.stop()
                ToastHelper().snackBar(
                    findViewById(android.R.id.content),
                    t.message ?: "Something went wrong"
                )
                call.cancel()
            }
        })
    }

    private fun setRow(view: View, label: String, value: String?) {
        view.findViewById<TextView>(R.id.tvLabel).text = label
        view.findViewById<TextView>(R.id.tvValue).text = value ?: "-"
    }

}