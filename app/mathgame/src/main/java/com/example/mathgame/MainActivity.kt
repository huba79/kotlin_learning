package com.example.mathgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mathgame.databinding.ActivityMainBinding
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        activityMainBinding.additionButton.text=resources.getText(R.string.menu_addition)
        activityMainBinding.multiplicationButton.text=resources.getText(R.string.menu_multiplication)
        activityMainBinding.exitButton.text=resources.getText(R.string.menu_exit)

        activityMainBinding.additionButton.setOnClickListener{
          val intent = Intent(this@MainActivity,GameActivity::class.java)
          intent.putExtra("game",GamesEnum.ADDITION.name)
            startActivity(intent)
        }

        activityMainBinding.multiplicationButton.setOnClickListener{
            val intent = Intent(this@MainActivity,GameActivity::class.java)
            intent.putExtra("game",GamesEnum.MULTIPLICATION.name)
            startActivity(intent)
        }
        activityMainBinding.exitButton.setOnClickListener { exitProcess(0) }
    }
}