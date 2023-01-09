package com.example.mathgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mathgame.databinding.ActivityResultBinding
import kotlin.system.exitProcess


class ResultActivity : AppCompatActivity() {
    lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val game = intent.getSerializableExtra("game", Game::class.java) as Game as from API 33
        @Suppress("DEPRECATION")
        val game = intent.getSerializableExtra("game") as Game
        binding.finalScoreView.text = game.currentScore.toString()

        binding.closeButton.setOnClickListener { exitProcess(0) }

        binding.replayButton.setOnClickListener{
            val intent = Intent(this@ResultActivity, MainActivity::class.java)
            startActivity(/* intent = */ intent)
        }
    }
}