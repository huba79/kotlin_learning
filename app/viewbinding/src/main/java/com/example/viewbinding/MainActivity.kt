package com.example.viewbinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.viewbinding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        mainBinding.button.text = "ButtonCaptionByBinding"
        mainBinding.textView.text="Text Displayed By Bound Element"

        mainBinding.button.setOnClickListener{
            Toast
                .makeText(this@MainActivity,"button click can be handled this way, too!", Toast.LENGTH_SHORT)
                .show()
        }
    }
}