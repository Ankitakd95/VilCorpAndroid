package com.softage.net.vilcorp.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.loadinganimation.LoadingAnimation

import com.softage.net.vilcorp.R


class CustomProgressDialog (context: Context) {

    private var dialog: CustomDialog

    private var cpCardView: CardView

    private var loadingAnim: LoadingAnimation


    fun start(title: String = "") {
        loadingAnim.setTextMsg(title)
        dialog.show()
    }

    fun stop() {
        dialog.dismiss()
    }

    init {
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(R.layout.progress_dialog_view, null)
        cpCardView = view.findViewById(R.id.cp_cardview)
        loadingAnim = view.findViewById(R.id.loadingAnim)
        cpCardView.setCardBackgroundColor(Color.parseColor("#70000000"))
        dialog = CustomDialog(context)
        dialog.setContentView(view)
    }

    private fun setColorFilter(drawable: Drawable, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION")
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    class CustomDialog(context: Context) : Dialog(context, R.style.CustomDialogTheme) {
        init {
            // Set Semi-Transparent Color for Dialog Background
            window?.decorView?.rootView?.setBackgroundResource(R.color.dialoge_transp)
            window?.decorView?.setOnApplyWindowInsetsListener { _, insets ->
                insets.consumeSystemWindowInsets()
            }
        }
    }
}