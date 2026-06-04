package com.softage.net.vilcorp.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.softage.net.vilcorp.R
import com.softage.net.vilcorp.adapter.CancelOrderAdapter
import com.softage.net.vilcorp.adapter.DeliveredOrderAdapter
import com.softage.net.vilcorp.databinding.ActivityDeliveredBinding
import com.softage.net.vilcorp.databinding.ActivityLoginBinding
import com.softage.net.vilcorp.model.orderHistory.CancelOrderListSuccess
import com.softage.net.vilcorp.network.ApiClient
import com.softage.net.vilcorp.network.ApiInterface
import com.softage.net.vilcorp.util.CustomProgressDialog
import com.softage.net.vilcorp.util.ToastHelper
import com.softage.net.vilcorp.util.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveredActivity : AppCompatActivity() {
    private var binding: ActivityDeliveredBinding? = null
    private val progressDialog by lazy { CustomProgressDialog(this) }

    private var mApiInterface: ApiInterface? = null
    private var mUtil: Utility? = null
    private var toolbar: Toolbar? = null
    private var leadList = ArrayList<CancelOrderListSuccess>()
    private lateinit var adapter: DeliveredOrderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveredBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initToolbar()
        binding!!.leadRecycler.layoutManager = LinearLayoutManager(applicationContext)
        mApiInterface = ApiClient.getRetrofit().create(ApiInterface::class.java)
        mUtil = Utility(this)
        getLeads()
    }
    private fun initToolbar() {
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        supportActionBar!!.title = "Delivered Orders"

    }
    private fun getLeads() {
        progressDialog.start("Loading...")

        val call = mApiInterface!!.getDeliveredOrders("Bearer ${Utility.token}")

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
                    adapter = DeliveredOrderAdapter(applicationContext,leadList)

                    binding!!.leadRecycler.adapter = adapter

                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ArrayList<CancelOrderListSuccess>>, t: Throwable) {
                progressDialog.stop()
                ToastHelper().snackBar(
                    findViewById(android.R.id.content),
                    t.message
                )
            }
        })
    }
}