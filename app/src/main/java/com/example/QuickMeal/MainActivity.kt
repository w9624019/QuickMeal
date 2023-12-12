package com.example.QuickMeal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dexOutputDir: File = File("/data/data/com.example.QuickMeal/code_cache/.overlay/base.apk/classes4.dex")
        dexOutputDir.setReadOnly()
    }
}