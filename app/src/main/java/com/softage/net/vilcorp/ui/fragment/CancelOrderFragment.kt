package com.softage.net.vilcorp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.softage.net.vilcorp.R
import com.softage.net.vilcorp.adapter.CancelOrderAdapter
import com.softage.net.vilcorp.adapter.LeadAdapter
import com.softage.net.vilcorp.databinding.FragmentCancelOrderBinding
import com.softage.net.vilcorp.databinding.FragmentHomeBinding
import com.softage.net.vilcorp.model.leads.LeadModel
import com.softage.net.vilcorp.model.leads.TodaysOrderSuccess
import com.softage.net.vilcorp.model.orderHistory.CancelOrderListSuccess
import com.softage.net.vilcorp.network.ApiClient
import com.softage.net.vilcorp.network.ApiInterface
import com.softage.net.vilcorp.util.CustomProgressDialog
import com.softage.net.vilcorp.util.ToastHelper
import com.softage.net.vilcorp.util.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CancelOrderFragment : Fragment() {
    private var binding: FragmentCancelOrderBinding?= null
    private var mApiInterface: ApiInterface? = null
    private var mUtil: Utility? = null
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }
    private var leadList = ArrayList<CancelOrderListSuccess>()
    private lateinit var adapter: CancelOrderAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCancelOrderBinding.inflate(layoutInflater)
        mApiInterface = ApiClient.getRetrofit().create(ApiInterface::class.java)
        mUtil = Utility(requireContext())
        binding!!.leadRecycler.layoutManager = LinearLayoutManager(requireContext())
        getLeads()
        return binding!!.root
    }
    private fun getLeads() {
        progressDialog.start("Loading...")

        val call = mApiInterface!!.getCancelledOrders("Bearer ${Utility.token}")

        call.enqueue(object : Callback<ArrayList<CancelOrderListSuccess>> {
            override fun onResponse(
                call: Call<ArrayList<CancelOrderListSuccess>>,
                response: Response<ArrayList<CancelOrderListSuccess>>
            ) {
                progressDialog.stop()

                val data = response.body()

                if (response.isSuccessful && data != null) {

                    leadList.clear()

                    for (item in data) {

                        val lead = CancelOrderListSuccess(
                            item.client,
                            item.managerName,
                            item.managerPhone,
                            item.managerEmail,
                            item.pjpCategory,
                            item.pjpSegment,
                            item.spocName,
                            item.spocPhone,
                            item.status,
                            item.reason
                        )

                        leadList.add(lead)
                    }
                    adapter = CancelOrderAdapter(requireContext(),leadList)

                    binding!!.leadRecycler.adapter = adapter

                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ArrayList<CancelOrderListSuccess>>, t: Throwable) {
                progressDialog.stop()
                ToastHelper().snackBar(
                    requireActivity().findViewById(android.R.id.content),
                    t.message
                )
            }
        })
    }


}