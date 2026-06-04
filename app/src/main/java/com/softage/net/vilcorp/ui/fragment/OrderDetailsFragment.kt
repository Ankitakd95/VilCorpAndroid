package com.softage.net.vilcorp.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.softage.net.vilcorp.R
import com.softage.net.vilcorp.adapter.LeadAdapter
import com.softage.net.vilcorp.databinding.FragmentHomeBinding
import com.softage.net.vilcorp.databinding.FragmentOrderDetailsBinding
import com.softage.net.vilcorp.model.attendance.PresignedUrlSuccess
import com.softage.net.vilcorp.model.leads.PjpStageSuccess
import com.softage.net.vilcorp.model.leads.TodaysOrderSuccess
import com.softage.net.vilcorp.network.ApiClient
import com.softage.net.vilcorp.network.ApiInterface
import com.softage.net.vilcorp.util.CustomProgressDialog
import com.softage.net.vilcorp.util.ToastHelper
import com.softage.net.vilcorp.util.Utility
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderDetailsFragment : Fragment() {
    private var binding: FragmentOrderDetailsBinding?= null
    private var mApiInterface: ApiInterface? = null
    private var mUtil: Utility? = null
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderDetailsBinding.inflate(layoutInflater)
        mApiInterface = ApiClient.getRetrofit().create(ApiInterface::class.java)
        mUtil = Utility(requireContext())
        val stageId = arguments?.getInt("ORDER_ID", -1) ?: -1

        getOrderDetails(stageId)
        binding!!.btnDeliver.setOnClickListener {
           getOrderStage(Utility.getOrderID()!!.toInt())
        }
        binding!!.btnCancel.setOnClickListener {
            showCancelPopup()
        }
        return binding!!.root
    }
    private fun getOrderDetails(stageId: Int) {

        progressDialog.start("Loading Details...")
        val call: Call<TodaysOrderSuccess> = mApiInterface!!.getOrderDetails("Bearer ${Utility.token}",stageId.toString())


        Log.d("token",Utility.token.toString())

        call.enqueue(object : Callback<TodaysOrderSuccess> {

            override fun onResponse(
                call: Call<TodaysOrderSuccess>,
                response: Response<TodaysOrderSuccess>
            ) {
                progressDialog.stop()
                if (response.isSuccessful && response.body() != null) {

                    val data = response.body()
                    binding!!.tvClientName.text = data!!.client_name.toString()
                    binding!!.tvAddress.text = data!!.client_address.toString()
                    binding!!.tvSimCount.text = "Total SIM: "+data!!.sim_count.toString()
                    binding!!.tvManager.text = "Manager: "+data!!.manager_name.toString()
                    binding!!.tvManagerPhone.text = data!!.manager_phone.toString()
                    binding!!.tvSpoc.text = "Spoc: "+data!!.spoc_name.toString()
                    binding!!.tvSpocPhone.text = data!!.spoc_phone.toString()
                    binding!!.tvCategory.text = "Category: "+data!!.pjp_category.toString()
                    binding!!.tvDeliveryDate.text = data!!.target_delivery_date.toString()
                    binding!!.tvStatus.text = data!!.lead_status.toString()
                    binding!!.btnCallManager.setOnClickListener {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:${data!!.manager_phone}")
                       requireContext().startActivity(intent)
                    }
                    binding!!.btnCallSpoc.setOnClickListener {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:${data!!.spoc_phone}")
                        requireContext().startActivity(intent)
                    }
                    binding!!.tvNavigate.setOnClickListener {
                        val gmmIntentUri = Uri.parse("google.navigation:q=" + data.client_address)

                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")

                        requireContext().startActivity(mapIntent)
                    }


                } else {
                    ToastHelper().snackBar(
                        requireActivity().findViewById(android.R.id.content),
                        "Failed to load details"
                    )
                }
            }

            override fun onFailure(call: Call<TodaysOrderSuccess>, t: Throwable) {
                progressDialog.stop()
                ToastHelper().snackBar(
                    requireActivity().findViewById(android.R.id.content),
                    t.message
                )
            }
        })
    }


    private fun showCancelPopup() {

        val dialogView = layoutInflater.inflate(R.layout.dialog_tripod_warning, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        val btnOk = dialogView.findViewById<Button>(R.id.btnOk)
        val spReason = dialogView.findViewById<AutoCompleteTextView>(R.id.spServiceProvider)

        // Dropdown values
        val reasons = listOf(
            "Customer Not Available",
            "Weather disruption",
            "Wrong Address Pincode",
            "Vehicle Breakdown"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            reasons
        )

        spReason.setAdapter(adapter)

        btnOk.setOnClickListener {

            val selectedReason = spReason.text.toString()

            if (selectedReason.isEmpty()) {
                ToastHelper().snackBar(
                    requireActivity().findViewById(android.R.id.content),
                    "Please select reason"
                )
                return@setOnClickListener
            }

            dialog.dismiss()

            getCurrentLocation { lat, lng ->
                callCancelOrderApi(lat, lng, selectedReason)
            }
        }

        dialog.show()
    }
    fun callCancelOrderApi(latitude: Double,longitude:Double, reason: String) {

        progressDialog.start("Cancelling Order...")

        val json = JSONObject().apply {
            put("latitude", latitude)
            put("longitude", longitude)
            put("reason", reason)
        }

        val requestBody = json.toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

        val call = mApiInterface!!.cancelOrder("Bearer ${Utility.token}",Utility.getOrderID()!!.toInt(),requestBody)

        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                progressDialog.stop()

                if (response.isSuccessful) {

                    Toast.makeText(requireContext(),"Order Cancelled Successfully",Toast.LENGTH_LONG).show()
                    val fragment = HomeFragment()

                    val bundle = Bundle()

                    fragment.arguments = bundle

                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment) // 👈 your container id
                        .addToBackStack(null)
                        .commit()
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
    private fun getCurrentLocation(onLocation: (Double, Double) -> Unit) {

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // request permission first
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                onLocation(location.latitude, location.longitude)
            } else {
                ToastHelper().snackBar(
                    requireActivity().findViewById(android.R.id.content),
                    "Unable to get location"
                )
            }
        }
    }

    private fun getOrderStage(pjpID: Int) {

        progressDialog.start("Checking Details...")
        val call: Call<PjpStageSuccess> = mApiInterface!!.getStage("Bearer ${Utility.token}",pjpID.toString())


        Log.d("token",Utility.token.toString())

        call.enqueue(object : Callback<PjpStageSuccess> {

            override fun onResponse(
                call: Call<PjpStageSuccess>,
                response: Response<PjpStageSuccess>
            ) {
                progressDialog.stop()
                if (response.isSuccessful && response.body() != null) {

                    val data = response.body()
                    if (data!!.data!!.stageOne==false){
                        val fragment = PjpDetailsFragment()

                        val bundle = Bundle()


                        fragment.arguments = bundle

                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment) // 👈 your container id
                            .addToBackStack(null)
                            .commit()
                    } else if (data!!.data!!.stageTwo==false){
                        val fragment = FeedbackFragment()

                        val bundle = Bundle()
                        bundle.putBoolean("stage", false)

                        fragment.arguments = bundle

                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment) // 👈 your container id
                            .addToBackStack(null)
                            .commit()
                    } else if (data!!.data!!.stageThree==false) {
                        val fragment = FeedbackFragment()

                        val bundle = Bundle()
                        bundle.putBoolean("stage", true)

                        fragment.arguments = bundle

                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment) // 👈 your container id
                            .addToBackStack(null)
                            .commit()
                    }



                } else {
                    ToastHelper().snackBar(
                        requireActivity().findViewById(android.R.id.content),
                        "Failed to load details"
                    )
                }
            }

            override fun onFailure(call: Call<PjpStageSuccess>, t: Throwable) {
                progressDialog.stop()
                ToastHelper().snackBar(
                    requireActivity().findViewById(android.R.id.content),
                    t.message
                )
            }
        })
    }
}