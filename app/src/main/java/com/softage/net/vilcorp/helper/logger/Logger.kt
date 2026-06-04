package com.softage.net.vilcorp.helper.logger

import android.util.Log

class Logger {
    fun debugLogger(log: String?) {}
    fun verboseLogger(log: String?) {}
    fun infoLogger(log: String?) {}

    companion object {
        fun warningLogger(tagName: String?, log: String) {
            Log.w(tagName, "warningLogger: $log")
        }

        fun errorLogger(tagName: String?, log: String) {
            Log.e(tagName, "errorLogger: $log")
        }

        fun errorLogger(tagName: String?, log: Int) {
            Log.e(tagName, "errorLogger: $log")
        }

        fun errorLogger(tagName: String?, log: String, statusCode: Int) {
            Log.e(tagName, "errorLogger: $statusCode: $log")
        }
    }
}
