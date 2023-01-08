package com.example.mathgame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mathgame.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    private lateinit var currentGame:Game
    private lateinit var binding: ActivityGameBinding
    private lateinit var currentGameType:GamesEnum
    private lateinit var operator:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        when(intent.getStringExtra("game")) {
            GamesEnum.ADDITION.name->{
                currentGameType = GamesEnum.ADDITION
                currentGame=Game(GamesEnum.ADDITION)
                operator="+"
            }
            GamesEnum.MULTIPLICATION.name->{
                currentGameType = GamesEnum.MULTIPLICATION
                currentGame=Game(GamesEnum.MULTIPLICATION)
                operator="*"
            }
        }
        newTask()

        binding.buttonCheck.setOnClickListener{
            if(binding.resultInputView.text.toString() != ""){
                val result = binding.resultInputView.text.toString().toInt()
                currentGame.evaluate(result)
                Log.d("resultCheck","is game over? ${currentGame.isGameOver}")
                if(!currentGame.isGameOver) {
                    updateStatus()
                    newTask()
                } else proceedToSummary()
            } else
                Toast.makeText(this@GameActivity,R.string.message_missing_result,Toast.LENGTH_SHORT)
                    .show()
        }

        binding.buttonExit.setOnClickListener{
            proceedToSummary()
        }

    }
    private fun proceedToSummary(){
        val intent = Intent(this@GameActivity, ResultActivity::class.java)
        intent.putExtra("game",currentGame)
        startActivity(intent)
    }

    private fun newTask(){
        currentGame.getNewTask()
        binding.resultInputView.text.clear()
        binding.assessmentView.text = "${currentGame.currentTask.first} $operator ${currentGame.currentTask.second} = ?"
        updateStatus()
    }

    private fun updateStatus(){
        binding.lifeView.text="${currentGame.livesLeft}"
        binding.scoreView.text="${currentGame.currentScore}"
    }
}