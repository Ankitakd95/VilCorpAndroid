package com.softage.net.vilcorp.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.softage.net.vilcorp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.softage.net.vilcorp.model.location.LocationRequestBody
import com.softage.net.vilcorp.network.ApiClient
import com.softage.net.vilcorp.network.ApiInterface
import com.softage.net.vilcorp.util.Utility

class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val handler = Handler(Looper.getMainLooper())
    private var mApiInterface: ApiInterface? = null
    private val interval = 1 * 60 * 1000L // 1 minutes

    override fun onCreate() {
        super.onCreate()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mApiInterface = ApiClient.getRetrofit().create(ApiInterface::class.java)
        startForegroundService()
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        handler.post(object : Runnable {
            override fun run() {
                getLocationAndSend()
                handler.postDelayed(this, interval)
            }
        })
    }

    private fun getLocationAndSend() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                sendToApi(it.latitude, it.longitude)
            }
        }
    }

    private fun sendToApi(lat: Double, lng: Double) {

        val call = mApiInterface!!.sendLocation("Bearer ${Utility.token}",
            LocationRequestBody(lat, lng)
        )

        call.enqueue(object : Callback<Any> {

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    Log.d("API", "Success: ${response.body()}")
                } else {
                    Log.e("API", "Error Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.e("API", "Failure: ${t.message}")
            }
        })
    }

    private fun startForegroundService() {
        val channelId = "location_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Location Service",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Do your work")
            .setSmallIcon(R.drawable.logo)
            .build()

        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}