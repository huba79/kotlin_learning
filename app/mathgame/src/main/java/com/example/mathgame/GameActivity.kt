package com.example.mathgame

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.mathgame.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    private lateinit var currentGame:Game
    private lateinit var binding: ActivityGameBinding
    private lateinit var currentGameType:GamesEnum
    private lateinit var operator:String
    private lateinit var timer:CountDownTimer
    private val MILLIS:Long = 30000
    private val STEPS:Long = 1000
    private val timeLeft: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        when(intent.getStringExtra("game")) {
            GamesEnum.ADDITION.name->{
                currentGameType = GamesEnum.ADDITION
                currentGame=Game(GamesEnum.ADDITION)
                operator="+"
                setTitle(R.string.game_addition_title)
            }
            GamesEnum.MULTIPLICATION.name->{
                currentGameType = GamesEnum.MULTIPLICATION
                currentGame=Game(GamesEnum.MULTIPLICATION)
                operator="*"
                setTitle(R.string.game_multiplication_title)
            }
        }
        newTask()

        timeLeft.observe(this@GameActivity) {
            //Log.d("resultCheck","time remaining...$it")
            if (it > 0) binding.timerView.text = it.toString()
            else {
                //Log.d("resultCheck","Time is up. Autocheck called from observer.")
                // checkAnswer(true)
            }
        }

        binding.buttonCheck.setOnClickListener{
            timer.cancel()
            checkAnswer(false)
        }

        binding.buttonExit.setOnClickListener{
            timer.cancel()
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

        timer = object : CountDownTimer(MILLIS, STEPS) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft.postValue(millisUntilFinished/STEPS)
            }
            override fun onFinish() {
                Log.d("resultCheck","Time is up. Autocheck called from Timer.onFinish.")
                checkAnswer(true)
            }
        }.start()
        binding.resultInputView.text.clear()
        binding.assessmentView.text = "${currentGame.currentTask.first} $operator ${currentGame.currentTask.second} = ?"
        updateStatus()
    }

    private fun updateStatus(){
        binding.lifeView.text="${currentGame.livesLeft}"
        binding.scoreView.text="${currentGame.currentScore}"
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        proceedToSummary()
    }

    fun checkAnswer(isTimeUp:Boolean){
        if(binding.resultInputView.text.toString() != ""){
            val result = binding.resultInputView.text.toString().toInt()
            currentGame.evaluate(result)
            Log.d("resultCheck","is game over? ${currentGame.isGameOver}")
            if(!currentGame.isGameOver) {
                updateStatus()
                newTask()
            } else proceedToSummary()
        } else if(!isTimeUp) {
            Toast.makeText(this@GameActivity,R.string.message_missing_result,Toast.LENGTH_SHORT)
                .show()
        } else {
            currentGame.evaluate(0)
            Log.d("resultCheck","is game over? ${currentGame.isGameOver}")
            if(!currentGame.isGameOver) {
                updateStatus()
                newTask()
            } else proceedToSummary()
        }

    }
}