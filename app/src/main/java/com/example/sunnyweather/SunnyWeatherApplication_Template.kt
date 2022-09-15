package com.example.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication_Template: Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val CY_TOKEN: String = "<CaiYun Token>"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}