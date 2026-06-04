package com.softage.net.vilcorp.ui.activity

import android.app.Application
import com.softage.net.vilcorp.util.Utility

class VilCorpApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // ✅ Initialize Utility ONCE for entire app lifecycle
        Utility.init(this)

    }
}