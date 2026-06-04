package com.softage.net.vilcorp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.softage.net.vilcorp.R
import com.softage.net.vilcorp.adapter.LeadAdapter
import com.softage.net.vilcorp.databinding.FragmentHomeBinding
import com.softage.net.vilcorp.helper.JsonHelper
import com.softage.net.vilcorp.model.attendance.PunchInSuccess
import com.softage.net.vilcorp.model.leads.LeadModel
import com.softage.net.vilcorp.model.leads.TodaysOrderSuccess
import com.softage.net.vilcorp.network.ApiClient
import com.softage.net.vilcorp.network.ApiInterface
import com.softage.net.vilcorp.ui.activity.MainActivity
import com.softage.net.vilcorp.util.CustomProgressDialog
import com.softage.net.vilcorp.util.ToastHelper
import com.softage.net.vilcorp.util.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding?= null
    private var mApiInterface: ApiInterface? = null
    private var mUtil: Utility? = null
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }
    private var leadList = ArrayList<LeadModel>()
    private lateinit var adapter: LeadAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        mApiInterface = ApiClient.getRetrofit().create(ApiInterface::class.java)
        mUtil = Utility(requireContext())
        binding!!.leadRecycler.layoutManager = LinearLayoutManager(requireContext())

        adapter = LeadAdapter(requireContext(), leadList) { lead ->
            openOrderDetailsFragment(lead.leadId)
        }

        binding!!.leadRecycler.adapter = adapter
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun getLeads() {
        progressDialog.start("Loading Leads...")

        val call = mApiInterface!!.getTodayOrder("Bearer ${Utility.token}")

        call.enqueue(object : Callback<ArrayList<TodaysOrderSuccess>> {
            override fun onResponse(
                call: Call<ArrayList<TodaysOrderSuccess>>,
                response: Response<ArrayList<TodaysOrderSuccess>>
            ) {
                progressDialog.stop()

                val data = response.body()

                if (response.isSuccessful && data != null) {

                    leadList.clear()

                    for (item in data) {

                        val lead = LeadModel(
                            item.client_name,
                            item.client_address,
                            item.manager_name,
                            item.manager_phone,
                            item.spoc_name,
                            item.spoc_phone,
                            item.pjp_category,
                            item.sim_count ?: "0",
                            item.target_delivery_date,
                            item.lead_status,
                            item.lead_id
                        )

                        leadList.add(lead)
                    }

                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ArrayList<TodaysOrderSuccess>>, t: Throwable) {
                progressDialog.stop()
                ToastHelper().snackBar(
                    requireActivity().findViewById(android.R.id.content),
                    t.message
                )
            }
        })
    }
    private fun openOrderDetailsFragment(orderId: Int) {

        val fragment = OrderDetailsFragment()

        val bundle = Bundle()
        bundle.putInt("ORDER_ID", orderId)
        mUtil!!.setOrderID(orderId.toString())

        fragment.arguments = bundle

        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment) // 👈 your container id
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        getLeads()
    }
}