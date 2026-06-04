package com.softage.net.vilcorp.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.softage.net.vilcorp.R
import com.softage.net.vilcorp.util.Utility
import java.text.SimpleDateFormat
import java.util.*

class PunchFragment : Fragment(R.layout.fragment_punch), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private lateinit var txtDate: TextView
    private lateinit var txtTime: TextView
    private lateinit var txtName: TextView
    private lateinit var txtDistance: TextView
    private lateinit var txtDutyStatus: TextView
    private lateinit var statusDot: View

    private lateinit var btnPunchIn: AppCompatButton
    private lateinit var btnPunchOut: AppCompatButton
    private var mUtil: Utility? = null
    private var currentLocation: Location? = null

    // Office location example
    private var officeLatLng = LatLng(28.4965,77.0797)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        txtDate = view.findViewById(R.id.txtDate)
        txtTime = view.findViewById(R.id.txtTime)
        txtDistance = view.findViewById(R.id.txtDistance)
        txtName = view.findViewById(R.id.txtWelcome)

        btnPunchIn = view.findViewById(R.id.btnPunchIn)
        btnPunchOut = view.findViewById(R.id.btnPunchOut)
        txtDutyStatus = view.findViewById(R.id.txtDutyStatus)
        statusDot = view.findViewById(R.id.statusDot)
        mUtil = Utility(requireContext())
        //officeLatLng = LatLng(Utility.getLatitude()!!.toDouble(),Utility.getLongitude()!!.toDouble())

        txtName.text= "Hi, "+ Utility.getName()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        startClock()

        btnPunchIn.setOnClickListener {

            if(currentLocation == null){
                Toast.makeText(context,"Location not ready",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (Utility.isRemotePunch){
                val bottomSheet = SelfieBottomSheet.newInstance(
                    "punchIn",
                    currentLocation!!.latitude,
                    currentLocation!!.longitude
                )
                bottomSheet.show(parentFragmentManager, "SelfieBottomSheet")
            } else{
                if(isInsideOffice(currentLocation!!)){

                    val bottomSheet = SelfieBottomSheet.newInstance(
                        "punchIn",
                        currentLocation!!.latitude,
                        currentLocation!!.longitude
                    )
                    bottomSheet.show(parentFragmentManager, "SelfieBottomSheet")

                }else{

                    Toast.makeText(context,"You are outside office range",Toast.LENGTH_LONG).show()

                }
            }


        }

        btnPunchOut.setOnClickListener {

            if(currentLocation == null){
                Toast.makeText(context,"Location not ready",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (Utility.isRemotePunchOut){
                val bottomSheet = SelfieBottomSheet.newInstance(
                    "punchOut",
                    currentLocation!!.latitude,
                    currentLocation!!.longitude
                )
                bottomSheet.show(parentFragmentManager, "SelfieBottomSheet")
            } else{
                if(isInsideOffice(currentLocation!!)){

                    val bottomSheet = SelfieBottomSheet.newInstance(
                        "punchOut",
                        currentLocation!!.latitude,
                        currentLocation!!.longitude
                    )
                    bottomSheet.show(parentFragmentManager, "SelfieBottomSheet")

                }else{

                    Toast.makeText(context,"You are outside office range",Toast.LENGTH_SHORT).show()

                }
            }


        }
    }

    override fun onMapReady(map: GoogleMap) {

        googleMap = map

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        googleMap.isMyLocationEnabled = true

        startLocationUpdates()
    }

    // Start Live Location Update (every 5 seconds)
    private fun startLocationUpdates(){

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,5000
        ).build()

        locationCallback = object : LocationCallback(){

            override fun onLocationResult(locationResult: LocationResult) {

                val location = locationResult.lastLocation ?: return

                if(isMockLocation(location)){

                    Toast.makeText(
                        context,
                        "Fake GPS detected. Punch not allowed.",
                        Toast.LENGTH_LONG
                    ).show()

                    return
                }

                currentLocation = location

                updateMap(location)

                updateDistance(location)
            }
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    // Update map marker and circle
    private fun updateMap(location: Location){

        val userLatLng = LatLng(location.latitude,location.longitude)

        googleMap.clear()

//        googleMap.addMarker(
//            MarkerOptions().position(userLatLng).title("You are here")
//        )

        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(userLatLng,17f)
        )

        // Office geofence
        googleMap.addCircle(
            CircleOptions()
                .center(officeLatLng)
                .radius(100.0)
                .strokeColor(Color.RED)
                .fillColor(0x220000FF)
                .strokeWidth(3f)
        )
    }

    // Calculate distance from office
    private fun updateDistance(location: Location){

        val officeLocation = Location("office")

        officeLocation.latitude = officeLatLng.latitude
        officeLocation.longitude = officeLatLng.longitude

        val distance = location.distanceTo(officeLocation)

        val distanceText = if(distance > 1000){

            val km = distance / 1000
            String.format("Distance from office: %.2f km", km)

        }else{

            "Distance from office: ${distance.toInt()} meters"

        }

        txtDistance.text = distanceText
    }

    private fun isMockLocation(location: Location): Boolean {

        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            location.isMock
        } else {
            location.isFromMockProvider
        }

    }

    // Check geofence restriction
    private fun isInsideOffice(userLocation: Location): Boolean {

        val office = Location("office")

        office.latitude = officeLatLng.latitude
        office.longitude = officeLatLng.longitude

        val distance = userLocation.distanceTo(office)

        return distance <= 100
    }

    // Live clock
    private fun startClock(){

        val timer = object : TimerTask(){

            override fun run() {

                activity?.runOnUiThread {

                    val dateFormat =
                        SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())

                    val timeFormat =
                        SimpleDateFormat("hh:mm a", Locale.getDefault())

                    val now = Date()

                    txtDate.text = dateFormat.format(now)
                    txtTime.text = timeFormat.format(now)

                }
            }
        }

        Timer().schedule(timer,0,1000)
    }

    private fun setDutyStatus(){

        if(Utility.isDuty){

            txtDutyStatus.text = "On Duty"
            txtDutyStatus.setTextColor(Color.parseColor("#2E7D32"))
            statusDot.setBackgroundResource(R.drawable.status_dot_green)

        }else{

            txtDutyStatus.text = "Off Duty"
            txtDutyStatus.setTextColor(Color.parseColor("#F44336"))
            statusDot.setBackgroundResource(R.drawable.status_dot_red)

        }
    }

    override fun onResume() {
        super.onResume()
        setDutyStatus()
    }


}