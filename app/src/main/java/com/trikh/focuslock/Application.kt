package com.trikh.focuslock

import android.app.Application

class Application : Application(){

    companion object {
        lateinit var instance: com.trikh.focuslock.Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}