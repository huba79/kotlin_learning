package com.example.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.sharedpreferences.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    var name:String? = null
    var message:String? = null
    var isChecked:Boolean? = null
    var count:Int? = null
    lateinit var preferences:SharedPreferences
    var clickCount:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        mainBinding.button.setOnClickListener{
            mainBinding.button.text = (clickCount++).toString()
        }

    }

    override fun onPause() {
        savePreferences()
        super.onPause()
    }

    override fun onResume() {
        loadPreferences()
        super.onResume()
    }

    fun savePreferences(){
        preferences=getSharedPreferences("mainPreferences", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt("counter",mainBinding.button.text.toString().toInt())
        editor.putBoolean("checked",mainBinding.checkBox.isChecked)
        editor.putString("title",mainBinding.editText.text.toString())
        editor.putString("message",mainBinding.editTextTextMultiLine.text.toString())
        editor.apply()
        Toast
            .makeText(this@MainActivity,"Settings saved successfully!",Toast.LENGTH_SHORT)
            .show()
    }

    fun loadPreferences(){
        val prefs = getSharedPreferences("mainPreferences", Context.MODE_PRIVATE)
            mainBinding.button.text = prefs.getInt("counter",0).toString()
            mainBinding.editText.setText(prefs.getString("title","error"))
            mainBinding.editTextTextMultiLine.setText(prefs.getString("message","error"))
            mainBinding.checkBox.isChecked = prefs.getBoolean("checked",false)
        clickCount = prefs.getInt("counter",0)
    }
}