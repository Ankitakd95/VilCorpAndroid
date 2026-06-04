package com.softage.net.vilcorp.ui.activity

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.softage.net.vilcorp.R
import com.softage.net.vilcorp.adapter.NotificationAdapter
import com.softage.net.vilcorp.databinding.ActivityNotificationBinding
import com.softage.net.vilcorp.helper.JsonHelper
import com.softage.net.vilcorp.model.login.ForgotPassResponse
import com.softage.net.vilcorp.model.notification.NotificationResponse
import com.softage.net.vilcorp.model.notification.ReadAllResponse
import com.softage.net.vilcorp.network.ApiClient
import com.softage.net.vilcorp.network.ApiInterface
import com.softage.net.vilcorp.util.CustomProgressDialog
import com.softage.net.vilcorp.util.ToastHelper
import com.softage.net.vilcorp.util.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationActivity : AppCompatActivity() {

    private val progressDialog by lazy { CustomProgressDialog(this) }
    private lateinit var binding: ActivityNotificationBinding
    private lateinit var mApiInterface: ApiInterface
    private lateinit var adapter: NotificationAdapter

    private val notificationList = ArrayList<NotificationResponse>()
    private val filteredList = ArrayList<NotificationResponse>()

    // current filter
    private var currentStatus = "all"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()

        mApiInterface = ApiClient.getRetrofit().create(ApiInterface::class.java)

        setupRecycler()
        setupSwitch()

        // default
        getNotifications("all")
        binding!!.tvMarkAllRead.setOnClickListener {
            doReadAll()
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Notifications"
    }

    private fun setupRecycler() {
        adapter = NotificationAdapter(filteredList) { item ->
            markAsRead(item)
        }

        binding.leadRecycler.layoutManager = LinearLayoutManager(this)
        binding.leadRecycler.adapter = adapter
    }

    // ✅ SWITCH LOGIC
    private fun setupSwitch() {

        // Default = All
        selectAllUI()

        binding.switchFilter.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                // 👉 Unseen selected
                selectUnseenUI()
                getNotifications("unseen")
            } else {
                // 👉 All selected
                selectAllUI()
                getNotifications("all")
            }
        }
    }

    // ✅ API CALL
    private fun getNotifications(status: String) {

        currentStatus = status

        progressDialog.start("Loading...")

        val call = mApiInterface.getNotifications("Bearer ${Utility.token}", status)

        call.enqueue(object : Callback<ArrayList<NotificationResponse>> {

            override fun onResponse(
                call: Call<ArrayList<NotificationResponse>>,
                response: Response<ArrayList<NotificationResponse>>
            ) {
                progressDialog.stop()

                val data = response.body()

                if (response.isSuccessful && data != null) {

                    notificationList.clear()
                    notificationList.addAll(data)

                    applyFilter()
                }
            }

            override fun onFailure(call: Call<ArrayList<NotificationResponse>>, t: Throwable) {
                progressDialog.stop()

                ToastHelper().snackBar(
                    findViewById(android.R.id.content),
                    t.message
                )
            }
        })
    }

    private fun doReadAll() {
        progressDialog.start("Processing...")
        val call: Call<ReadAllResponse> = mApiInterface!!.markNotificationReadAll("Bearer ${Utility.token}")
        call.enqueue(object : Callback<ReadAllResponse?> {
            override fun onResponse(
                call: Call<ReadAllResponse?>,
                response: Response<ReadAllResponse?>
            ) {
                progressDialog.stop()
                if (response.code() == 200) {
                    assert(response.body() != null)
                 getNotifications("all")
                }  else{
                    ToastHelper().snackBar(findViewById(android.R.id.content),"Failed , Please try again")
                }
            }

            override fun onFailure(call: Call<ReadAllResponse?>, t: Throwable) {
                progressDialog.stop()
                ToastHelper().snackBar(findViewById(android.R.id.content), t.message)
                call.cancel()
            }
        })
    }

    // ✅ FILTER (UI safety)
    private fun applyFilter() {

        filteredList.clear()

        if (currentStatus == "all") {
            filteredList.addAll(notificationList)
        } else {
            filteredList.addAll(notificationList.filter { !it.is_read })
        }

        adapter.notifyDataSetChanged()
    }

    private fun selectAllUI() {
        binding.tvAll.setTextColor(getColor(R.color.vil_red))
        binding.tvAll.setTypeface(null, Typeface.BOLD)

        binding.tvUnseen.setTextColor(getColor(R.color.dark_grey))
        binding.tvUnseen.setTypeface(null, Typeface.NORMAL)
    }

    private fun selectUnseenUI() {
        binding.tvUnseen.setTextColor(getColor(R.color.vil_red))
        binding.tvUnseen.setTypeface(null, Typeface.BOLD)

        binding.tvAll.setTextColor(getColor(R.color.dark_grey))
        binding.tvAll.setTypeface(null, Typeface.NORMAL)
    }

    // ✅ MARK AS READ (PATCH API)
    private fun markAsRead(item: NotificationResponse) {

        if (item.is_read) return

        progressDialog.start("Updating...")

        val call = mApiInterface.markNotificationRead(
            "Bearer ${Utility.token}",
            item.id
        )

        call.enqueue(object : Callback<Unit> {

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                progressDialog.stop()

                if (response.isSuccessful) {

                    // update local
                    item.is_read = true
                    notificationList.find { it.id == item.id }?.is_read = true

                    applyFilter()

                } else {
                    ToastHelper().snackBar(
                        findViewById(android.R.id.content),
                        "Failed to mark as read"
                    )
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                progressDialog.stop()

                ToastHelper().snackBar(
                    findViewById(android.R.id.content),
                    t.message
                )
            }
        })
    }
}