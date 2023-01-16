package com.example.mathgame.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mathgame.R
import com.example.mathgame.databinding.ActivityResultBinding
import com.example.mathgame.game.Game


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

        binding.closeButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.dialog_confirm_exit_game_title))
            builder.setMessage(getString(R.string.dialog_confirm_exit_app_message))

            builder.setPositiveButton(getString(R.string.dialog_positive_button)) { dialog, which ->
                this.finishAffinity()
            }
            builder.setNegativeButton(getString(R.string.dialog_negative_button)) { dialog, which -> run {} }

            builder.show()
        }

        binding.replayButton.setOnClickListener{
            val intent = Intent(this@ResultActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        //cleanup insights: must stay in the Activity logic
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialog_confirm_exit_game_title))
        builder.setMessage(getString(R.string.dialog_confirm_exit_app_message))

        builder.setPositiveButton(getString(R.string.dialog_positive_button)) { dialog, which ->
            this.finishAffinity()
        }
        builder.setNegativeButton(getString(R.string.dialog_negative_button)) { dialog, which -> run {} }

        builder.show()
    }
}