package com.softage.net.vilcorp.helper.logger

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar

class ToastLogger(private val mContext: Context) {
    fun showSnackBar(root: View?, message: String?) {
        val snackbar = Snackbar
            .make(root!!, message!!, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}
