package com.softage.net.vilcorp.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.softage.net.vilcorp.R
import com.softage.net.vilcorp.helper.JsonHelper
import com.softage.net.vilcorp.model.attendance.PresignedUrlSuccess
import com.softage.net.vilcorp.model.attendance.PunchInSuccess
import com.softage.net.vilcorp.model.login.ForgotPassResponse
import com.softage.net.vilcorp.network.ApiClient
import com.softage.net.vilcorp.network.ApiInterface
import com.softage.net.vilcorp.ui.activity.MainActivity
import com.softage.net.vilcorp.ui.activity.onboarding.ResetPasswordActivity
import com.softage.net.vilcorp.util.CustomProgressDialog
import com.softage.net.vilcorp.util.ToastHelper
import com.softage.net.vilcorp.util.Utility
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection

class SelfieFormCloseSheet : BottomSheetDialogFragment() {

    private lateinit var previewView: PreviewView
    private lateinit var captureButton: Button
    private lateinit var imgPreview: ImageView
    private lateinit var retryButton: Button
    private lateinit var faceGuide: ImageView
    private lateinit var doneButton: Button
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    private var mApiInterface: ApiInterface? = null
    private var mUtil: Utility? = null
    private var uploadUrl =""
    private var fileUrl =""
    private var key =""
    private var capturedFile: File? = null

    private lateinit var imageCapture: ImageCapture
    companion object {
        private const val KEY_TYPE = "type"

        fun newInstance(type: String): SelfieFormCloseSheet {
            val fragment = SelfieFormCloseSheet()
            val bundle = Bundle()
            bundle.putString(KEY_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var type: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.bottomsheet_selfie, container, false)

        previewView = view.findViewById(R.id.cameraPreview)
        captureButton = view.findViewById(R.id.btnCapture)
        imgPreview = view.findViewById(R.id.imgPreview)
        retryButton = view.findViewById(R.id.btnRetry)
        faceGuide = view.findViewById(R.id.faceGuide)
        doneButton = view.findViewById(R.id.btnDone)
        startCamera()

        captureButton.setOnClickListener {

            animateCapture()
            takePhoto()

        }
        retryButton.setOnClickListener {

            imgPreview.visibility = View.GONE
            retryButton.visibility = View.GONE
            doneButton.visibility = View.GONE

            previewView.visibility = View.VISIBLE
            faceGuide.visibility = View.VISIBLE
            captureButton.visibility = View.VISIBLE
        }

        doneButton.setOnClickListener {

//            val intent = Intent(requireContext(), MainActivity::class.java)
//            startActivity(intent)
//            requireActivity().finish()
//            dismiss()
            val fileName = capturedFile?.name

            if (fileName.isNullOrEmpty()) {
                Toast.makeText(context, "No image captured", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Call API here
            getPreSignedUrls(fileName)

        }

        return view
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getString(KEY_TYPE)
        mApiInterface = ApiClient.getRetrofit().create(ApiInterface::class.java)
        mUtil = Utility(requireContext())
    }

    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({

            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()

            preview.setSurfaceProvider(previewView.surfaceProvider)

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            cameraProvider.unbindAll()

            cameraProvider.bindToLifecycle(
                viewLifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {

        capturedFile = File(
            requireContext().cacheDir,
            "selfie_${System.currentTimeMillis()}.jpg"
        )

        val photoFile = capturedFile!!

        val outputOptions =
            ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {

                    // show original image without bitmap compression
                    imgPreview.scaleX = -1f
                    imgPreview.setImageURI(Uri.fromFile(photoFile))

                    imgPreview.visibility = View.VISIBLE
                    retryButton.visibility = View.VISIBLE
                    doneButton.visibility = View.VISIBLE

                    previewView.visibility = View.GONE
                    faceGuide.visibility = View.GONE
                    captureButton.visibility = View.GONE

                }

                override fun onError(exception: ImageCaptureException) {

                    Toast.makeText(context,"Capture Failed",Toast.LENGTH_SHORT).show()

                }
            }
        )
    }

    private fun animateCapture() {

        captureButton.animate()
            .scaleX(0.7f)
            .scaleY(0.7f)
            .setDuration(80)
            .withEndAction {

                captureButton.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(80)
                    .start()

            }.start()
    }

    override fun getTheme(): Int {
        return com.google.android.material.R.style.Theme_Material3_Light_BottomSheetDialog
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setCanceledOnTouchOutside(false)

        dialog.setOnShowListener {

            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            bottomSheet?.let {

                it.setBackgroundResource(android.R.color.transparent)

                val behavior =
                    com.google.android.material.bottomsheet.BottomSheetBehavior.from(it)

                behavior.state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true
            }
        }

        return dialog
    }

    private fun getPreSignedUrls(file_name:String) {
        progressDialog.start("Processing...")
        val call: Call<PresignedUrlSuccess> = mApiInterface!!.getPreSignedUrl("Bearer ${Utility.token}",JsonHelper.getPreSignedUrl("image/jpeg",file_name,"meeting-image"))
        call.enqueue(object : Callback<PresignedUrlSuccess?> {
            override fun onResponse(
                call: Call<PresignedUrlSuccess?>,
                response: Response<PresignedUrlSuccess?>
            ) {
                progressDialog.stop()
                if (response.code() == 200) {
                    assert(response.body() != null)
                    fileUrl = response.body()!!.fileUrl.toString()
                    uploadUrl = response.body()!!.uploadUrl.toString()
                    key = response.body()!!.key.toString()
                    uploadToS3()

                }  else{
                    ToastHelper().snackBar(requireActivity().findViewById(android.R.id.content),"Failed , Please try again")
                }
            }

            override fun onFailure(call: Call<PresignedUrlSuccess?>, t: Throwable) {
                progressDialog.stop()
                ToastHelper().snackBar(requireActivity().findViewById(android.R.id.content), t.message)
                call.cancel()
            }
        })
    }

    private fun uploadToS3() {

        val file = capturedFile ?: return

        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

        val request = okhttp3.Request.Builder()
            .url(uploadUrl)
            .put(requestBody)
            .header("Content-Type", "image/jpeg") // ⚠️ must match EXACT
            .build()

        val client = okhttp3.OkHttpClient()

        client.newCall(request).enqueue(object : okhttp3.Callback {

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                requireActivity().runOnUiThread {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                requireActivity().runOnUiThread {

                    if (response.code in 200..299) {
                        Toast.makeText(context, "Upload Success ✅", Toast.LENGTH_SHORT).show()


                        saveImage(fileUrl, key)

                    } else {
                        Toast.makeText(context, "Upload Failed: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
    private fun saveImage(url: String,key:String) {
        progressDialog.start("Completing Order...")
        val call: Call<PunchInSuccess> = mApiInterface!!.saveMeetingImage("Bearer ${Utility.token}",JsonHelper.saveMeetImage(Utility.getOrderID()!!.toInt(),url,key))
        call.enqueue(object : Callback<PunchInSuccess?> {
            override fun onResponse(
                call: Call<PunchInSuccess?>,
                response: Response<PunchInSuccess?>
            ) {
                progressDialog.stop()
                if (response.code() == 200) {
                    assert(response.body() != null)
                    Toast.makeText(requireContext(),"Order Delivered",Toast.LENGTH_LONG).show()
                    dismiss()
                    val fragment = HomeFragment()

                    val bundle = Bundle()

                    fragment.arguments = bundle

                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment) // 👈 your container id
                        .addToBackStack(null)
                        .commit()

                }  else{
                    ToastHelper().snackBar(requireActivity().findViewById(android.R.id.content),"Failed , Please try again")
                }
            }

            override fun onFailure(call: Call<PunchInSuccess?>, t: Throwable) {
                progressDialog.stop()
                ToastHelper().snackBar(requireActivity().findViewById(android.R.id.content), t.message)
                call.cancel()
            }
        })
    }


}