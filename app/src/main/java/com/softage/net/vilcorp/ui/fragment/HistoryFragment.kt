package com.softage.net.vilcorp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softage.net.vilcorp.R
import com.softage.net.vilcorp.adapter.AttendanceHistoryAdapter
import com.softage.net.vilcorp.model.PunchHistoryData
import com.softage.net.vilcorp.model.attendance.AttendanceLog
import com.softage.net.vilcorp.model.attendance.HistorySuccess
import com.softage.net.vilcorp.network.ApiClient
import com.softage.net.vilcorp.network.ApiInterface
import com.softage.net.vilcorp.util.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AttendanceHistoryAdapter
    private val historyList = ArrayList<PunchHistoryData>()
    private var nextCursorDate: String? = null
    private var nextCursorId: String? = null

    private var currentPage = 1
    private var isLoading = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        recyclerView = view.findViewById(R.id.recyclerHistory)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchAttendanceHistory()
        adapter = AttendanceHistoryAdapter(requireContext(), historyList)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (!recyclerView.canScrollVertically(1)) {
                    fetchAttendanceHistory()
                }
            }
        })

        return view
    }
    private fun fetchAttendanceHistory() {

        if (isLoading) return
        isLoading = true

        val body = mapOf("username" to Utility.getUserId())

        val call = ApiClient.getRetrofit()
            .create(ApiInterface::class.java)
            .getAttendanceHistory("Bearer ${Utility.token}",currentPage, body)

        call.enqueue(object : Callback<HistorySuccess> {

            override fun onResponse(
                call: Call<HistorySuccess>,
                response: Response<HistorySuccess>
            ) {
                isLoading = false

                if (response.isSuccessful && response.body() != null) {

                    val data = response.body()!!

                    val list = data.logs.map { convertToUI(it) }

                    historyList.clear()   // ⚠️ important (because API returns cumulative data)
                    historyList.addAll(list)

                    adapter.notifyDataSetChanged()

                    // 👉 increase page
                    currentPage++
                }
            }

            override fun onFailure(call: Call<HistorySuccess>, t: Throwable) {
                isLoading = false
            }
        })
    }
    private fun convertToUI(log: AttendanceLog): PunchHistoryData {

        val dateParts = log.attendance_date.split("-") // yyyy-MM-dd

        val year = dateParts[0].toInt()
        val monthNum = dateParts[1].toInt()
        val day = dateParts[2].toInt()

        val monthName = getMonthName(monthNum)
        val dayName = getDayName(log.attendance_date)

        return PunchHistoryData(
            date = day,
            month = monthName,
            year = year,
            day = dayName,
            punchInTime = log.punch_in,
            punchOutTime = log.punch_out,
            totalHour = log.duration,
            status = log.status
        )
    }
    private fun getMonthName(month: Int): String {
        return listOf(
            "January","February","March","April","May","June",
            "July","August","September","October","November","December"
        )[month - 1]
    }
    private fun getDayName(date: String): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val dayFormat = java.text.SimpleDateFormat("EEEE", java.util.Locale.getDefault())
        return dayFormat.format(sdf.parse(date)!!)
    }

}