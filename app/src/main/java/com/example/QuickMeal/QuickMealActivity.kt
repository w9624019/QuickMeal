package com.example.QuickMeal

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import java.io.File

@HiltAndroidApp
class QuickMealActivity: Application() {
    override fun onCreate() {
        super.onCreate()
        val dexOutputDir: File = File("/data/data/com.example.QuickMeal/code_cache/.overlay/base.apk/classes4.dex")
        dexOutputDir.setReadOnly()
    }
}