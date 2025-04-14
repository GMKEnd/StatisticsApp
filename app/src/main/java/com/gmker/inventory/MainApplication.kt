package com.gmker.inventory

import android.app.Application
import android.util.Log
import com.gmker.inventory.dataBase.AppDatabase
import com.gmker.inventory.util.ActivityUtil

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i("MainApplication", "onCreate")
        AppDatabase.getInstance(this)
        ActivityUtil.init(this)
    }
}