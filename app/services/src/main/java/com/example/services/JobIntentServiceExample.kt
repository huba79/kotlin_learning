package com.example.services

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

class JobIntentServiceExample:JobIntentService() {
    override fun onHandleWork(intent: Intent) {
        Log.i("jobintentservice","onHandle")
        Log.i("jobintentservice", "Current thread: ${Thread.currentThread().name}")

    }

    override fun onCreate() {
        super.onCreate()
        Log.i("jobintentservice","Creating...Yesh Miloord")
        Log.i("jobintentservice", "Current thread: ${Thread.currentThread().name}")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("jobintentservice","onStartCommand")
        Log.i("jobintentservice", "Current thread: ${Thread.currentThread().name}")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onStopCurrentWork(): Boolean {
        Log.i("jobintentservice","Stopping ... Work done")
        return super.onStopCurrentWork()
    }

    override fun onDestroy() {
        Log.i("jobintentservice","onDestroy")
        super.onDestroy()
    }

    companion object{
        fun myBackgroundService(pContext: Context, pIntent:Intent){
            enqueueWork(pContext, JobIntentServiceExample::class.java,0,pIntent)
        }
    }
}