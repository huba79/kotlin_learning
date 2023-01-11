package com.example.mathgame

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mathgame.databinding.ActivityResultBinding


class ResultActivity : AppCompatActivity() {
    lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val game = intent.getSerializableExtra("game", Game::class.java) as Game as from API 33
        @Suppress("DEPRECATION")
        val game = intent.getSerializableExtra("game") as? Game
        binding.finalScoreView.text =
            if (game == null) "0" else game.currentScore.toString()

        binding.closeButton.setOnClickListener { this.finishAffinity() }

        binding.replayButton.setOnClickListener{
            val intent = Intent(this@ResultActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        Toast
            .makeText(this@ResultActivity,"Use the exit button to leave the application!", Toast.LENGTH_SHORT)
            .show()
    }
}