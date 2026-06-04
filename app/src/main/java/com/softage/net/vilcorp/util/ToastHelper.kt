package com.softage.net.vilcorp.util

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class ToastHelper : AppCompatActivity() {
    fun snackBar(view: View?, message: String?) {
        val snakbar = Snackbar.make(view!!, message!!, Snackbar.LENGTH_LONG)
        snakbar.show()
    }
}
