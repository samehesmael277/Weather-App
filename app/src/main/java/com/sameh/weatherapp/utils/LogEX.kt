package com.sameh.weatherapp.utils

import android.util.Log

fun String.toLogD(tag: String = "myDebugTAG") {
    Log.d(tag, this)
}

fun String.toLogE(tag: String = "myDebugTAG") {
    Log.e(tag, this)
}

fun String.toLogW(tag: String = "myDebugTAG") {
    Log.w(tag, this)
}