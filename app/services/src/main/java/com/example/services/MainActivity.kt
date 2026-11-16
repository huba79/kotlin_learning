package com.example.services

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var  startClassic:Button
    private lateinit var startJobIntent:Button
    private lateinit var stopClassic: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startClassic = findViewById(R.id.startClassicServiceButtonId)
        startJobIntent = findViewById(R.id.startJobIntentServiceButtonId)
        stopClassic = findViewById(R.id.stopClassicServiceButtonId)

        startClassic.setOnClickListener{
            Log.i("service", "About to launch Classic Service")
            val intentClassic = Intent(this@MainActivity,ClassicServiceExample::class.java)
            startService(intentClassic)
        }

        stopClassic.setOnClickListener{
            Log.i("service", "About to stop Classic Service")
            val intentClassic = Intent(this@MainActivity,ClassicServiceExample::class.java)
            stopService(intentClassic)
        }

        startJobIntent.setOnClickListener{
            val intent = Intent(this@MainActivity,JobIntentServiceExample::class.java)
            JobIntentServiceExample.myBackgroundService(this@MainActivity,intent)
        }


    }
}