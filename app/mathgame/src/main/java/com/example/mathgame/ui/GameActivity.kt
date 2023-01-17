package com.example.mathgame.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.mathgame.R
import com.example.mathgame.databinding.ActivityGameBinding
import com.example.mathgame.game.Game
import com.example.mathgame.game.GameSettings
import com.example.mathgame.game.GameTypesEnum

class GameActivity : AppCompatActivity() {

    private lateinit var currentGame: Game
    private lateinit var binding: ActivityGameBinding
    private lateinit var currentGameType: GameTypesEnum
    private lateinit var operator:String
    private lateinit var timer:CountDownTimer
    private  var settings: GameSettings? = null
    private val timeLeft: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settings =intent.getSerializableExtra("settings") as GameSettings?
        Log.d("mathLog", "settings from intent acquired: additionRange:  ${settings!!.additionRange}")

        when(intent.getStringExtra("game")) {
            GameTypesEnum.ADDITION.name->{
                currentGameType = GameTypesEnum.ADDITION
                currentGame= Game(GameTypesEnum.ADDITION,settings!!)
                operator="+"
                setTitle(R.string.game_addition_title)
            }
            GameTypesEnum.MULTIPLICATION.name->{
                currentGameType = GameTypesEnum.MULTIPLICATION
                currentGame= Game(GameTypesEnum.MULTIPLICATION,settings!!)
                operator="*"
                setTitle(R.string.game_multiplication_title)
            }
        }

        newTask()

        timeLeft.observe(this@GameActivity, fun(it: Long) {
            //Log.d("mathLog","time remaining...$it")
            if (it > 0) binding.timerView.text = it.toString()
            currentGame.timeToExpire = it.toInt()
        })

        binding.buttonCheck.setOnClickListener{
            if(!binding.resultInputView.text.toString().equals("")) {
                timer.cancel()
                checkAnswer(false)
            } else checkAnswer(false)
        }

        binding.buttonExit.setOnClickListener{
            if(!currentGame.isGameOver){
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.dialog_confirm_exit_game_title))
                builder.setMessage(getString(R.string.dialog_confirm_exit_game_message))

                builder.setPositiveButton(getString(R.string.dialog_positive_button)) { dialog, which ->
                    timer.cancel()
                    proceedToSummary()
                }
                builder.setNegativeButton(getString(R.string.dialog_negative_button)) { dialog, which -> run {} }
                builder.show()
            } else {
                timer.cancel()
                proceedToSummary()
            }
        }

    }
    private fun proceedToSummary(){
        //cleanup insights: should stay in the Activity logic
        val intent = Intent(this@GameActivity, ResultActivity::class.java)
        intent.putExtra("game",currentGame)
        startActivity(intent)
    }
    private fun newTask(){
        //cleanup insights: should not stay in the Activity logic, have to move to a viewmodel/service layer
        currentGame.getNewTask()

        timer = object : CountDownTimer((settings!!.timeFrame * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft.postValue(millisUntilFinished/1000)
            }
            override fun onFinish() {
                Log.d("mathLog","Time is up. Autocheck called from Timer.onFinish.")
                checkAnswer(true)
            }
        }.start()

        binding.resultInputView.text.clear()
        binding.assessmentView.text = resources.getString(
            R.string.task_assessment,
                                    currentGame.currentTask.first,operator,currentGame.currentTask.second)
        updateResults()
    }
    private fun updateResults(){
        //cleanup insights: should stay in the Activity logic
        binding.lifeView.text="${currentGame.livesLeft}"
        binding.scoreView.text="${currentGame.currentScore}"
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        //cleanup insights: must stay in the Activity logic
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialog_confirm_exit_game_title))
        builder.setMessage(getString(R.string.dialog_confirm_exit_game_message))

        builder.setPositiveButton(getString(R.string.dialog_positive_button)) { dialog, which ->
            proceedToSummary()
        }
        builder.setNegativeButton(getString(R.string.dialog_negative_button)) { dialog, which -> run {} }
        builder.show()
    }

    fun checkAnswer(isTimeUp:Boolean){
        //cleanup insights: should not stay in the Activity logic, have to move to a viewmodel/service layer
        //TODO: input result as a param for later we need ot move this logic out of the Activity
        val answerString = binding.resultInputView.text.toString()
        val bonusGainable = currentGame.timeToExpire
        if( !answerString.isEmpty()){
            val result = answerString.toInt()
            currentGame.evaluateResponse(result,bonusGainable)
            Log.d("mathLog","is game over? ${currentGame.isGameOver}")
            Log.d("mathLog","Bonus on the line:...$bonusGainable")
            if(!currentGame.isGameOver) {
                updateResults()
                newTask()
            } else proceedToSummary()
        } else if(!isTimeUp) {
            Toast.makeText(this@GameActivity, R.string.message_missing_result,Toast.LENGTH_SHORT)
                .show()
        } else {
            currentGame.evaluateResponse(0,bonusGainable)
            Log.d("mathLog","is game over? ${currentGame.isGameOver}")
            if(!currentGame.isGameOver) {
                updateResults()
                newTask()
            } else proceedToSummary()
        }

    }

}