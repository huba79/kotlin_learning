package com.example.mathgame.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import com.example.mathgame.databinding.ActivityMainBinding
import com.example.mathgame.game.GameSettings
import com.example.mathgame.game.GameTypesEnum

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), OnClickListener {
    private lateinit var activityMainBinding: ActivityMainBinding
    private var settings: GameSettings? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val defaultSettings = GameSettings(3,30,Pair(10,30),Pair(10,50),false)

        settings = intent?.getSerializableExtra("settings") as GameSettings?

        if(settings==null) {
            settings = defaultSettings
        }

        activityMainBinding.additionButton.setOnClickListener(this)
        activityMainBinding.multiplicationButton.setOnClickListener(this)
        activityMainBinding.settingsButton.setOnClickListener(this)
        activityMainBinding.exitButton.setOnClickListener(this)

    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
            activityMainBinding.additionButton.id -> {
                Log.d("mathLog","Addition clicked")
                val intent = Intent(this@MainActivity, GameActivity::class.java)
                intent.putExtra("game", GameTypesEnum.ADDITION.name)
                intent.putExtra("settings",settings)
                startActivity(intent)
            }

            activityMainBinding.multiplicationButton.id -> {
                Log.d("mathLog","Multiplication clicked")
                val intent = Intent(this@MainActivity, GameActivity::class.java)
                intent.putExtra("game", GameTypesEnum.MULTIPLICATION.name)
                intent.putExtra("settings",settings)

                startActivity(intent)
            }

            activityMainBinding.settingsButton.id -> {
                Log.d("mathLog","Settings clicked")
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                intent.putExtra("currentSettings",settings)
                startActivity(intent)
                //TODO remember to handle returned settings
            }

            activityMainBinding.exitButton.id -> {
                Log.d("mathLog","Exit clicked")
                this.finishAffinity() }

            else -> {
                Log.d("mathLog","Invalid element source, will not be handled ${p0?.id} -> ${p0!!::class.java.name}")}
        }

    }
}