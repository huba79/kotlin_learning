package com.example.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class ClassicServiceExample:Service() {
    override fun onBind(intent: Intent?): IBinder? {
        Log.i("service", "OnBindService")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("service", "OnCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.i("service", "OnStartCommand")
        Log.i("service thread:", Thread.currentThread().name)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.i("service", "OnDestroy")
        super.onDestroy()
    }
}