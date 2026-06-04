package com.softage.net.vilcorp.helper

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.google.gson.JsonParser

class SessionHelper(private val mContext: Context) {
    fun sessionExpired(view: View?) {
        Toast.makeText(mContext, "Session has Expired ,Please login again", Toast.LENGTH_LONG)
            .show()
    }

    fun sessionExpired() {
        Toast.makeText(mContext, "Session has Expired ,Please login again", Toast.LENGTH_LONG)
            .show()
    }

    fun initMessage(message: String?, mView: View?) {
        val snackbar = Snackbar
            .make(mView!!, message!!, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    fun messageToast(message: String?) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show()
    }

    fun requestErrorMessage(response: String?) {
        try {
            val parser = JsonParser()
            var jsonObj: JsonObject? = null
            try {
                jsonObj = parser.parse(response) as JsonObject
                Toast.makeText(mContext, jsonObj["Message"].asString, Toast.LENGTH_SHORT).show()
            } catch (ex: Exception) {
                try {
                    Toast.makeText(mContext, jsonObj!!["details"].asString, Toast.LENGTH_SHORT)
                        .show()
                } catch (exs: Exception) {
                }
            }
        } catch (Ex: Exception) {
        }
    }

    fun requestErrorMessage(response: String?, mView: View?) {
        try {
            val parser = JsonParser()
            var jsonObj: JsonObject? = null
            try {
                jsonObj = parser.parse(response) as JsonObject
                val snackbar = Snackbar
                    .make(mView!!, jsonObj!!["message"].asString, Snackbar.LENGTH_LONG)
                snackbar.show()
            } catch (ex: Exception) {
                try {
                    Toast.makeText(mContext, jsonObj!!["details"].asString, Toast.LENGTH_SHORT)
                        .show()
                } catch (exs: Exception) {
                }
            }
        } catch (Ex: Exception) {
        }
    }

    fun requestMessage(response: String?) {
        try {
            val parser = JsonParser()
            var jsonObj: JsonObject? = null
            try {
                jsonObj = parser.parse(response) as JsonObject
                Toast.makeText(mContext, jsonObj!!["message"].asString, Toast.LENGTH_SHORT).show()
            } catch (ex: Exception) {
                try {
                    Toast.makeText(mContext, jsonObj!!["details"].asString, Toast.LENGTH_SHORT)
                        .show()
                } catch (exs: Exception) {
                }
            }
        } catch (Ex: Exception) {
        }
    }

    fun showToast(message: String?) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show()
    }
}
