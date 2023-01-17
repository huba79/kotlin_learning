package com.example.mathgame.game
import android.util.Log
import java.io.Serializable
import kotlin.random.Random

class Game(val type: GameTypesEnum, settings: GameSettings) : Serializable {

    var isGameOver = false
    var gameSettings = settings
    var currentScore = 0
    var livesLeft = gameSettings.livesAvailable
    lateinit var currentTask: Pair<Int, Int>
    var timeToExpire:Int = settings.timeFrame

    fun getNewTask(){
        currentTask = when(type){
            GameTypesEnum.ADDITION ->{
                Pair( Random.nextInt(gameSettings.additionRange.first, gameSettings.additionRange.second),
                    Random.nextInt(gameSettings.additionRange.first, gameSettings.additionRange.second) )
            }
            GameTypesEnum.MULTIPLICATION ->{
                Pair( Random.nextInt(gameSettings.multiplicationRange.first, gameSettings.multiplicationRange.second),
                Random.nextInt(gameSettings.multiplicationRange.first, gameSettings.multiplicationRange.second) )
            }
            //TODO: handle eventually additional operators
            else -> {
                Pair( Random.nextInt(20, 50), Random.nextInt(20,50) )
            }
        }
    }
    fun taskSuccessful(){
        //TODO implement performance bias controlled by gameSettings.timeleftBias
         currentScore+=10
    }
    fun taskSuccessfulWithBonus(bonus:Int){
        currentScore+=10 +bonus
    }

    fun taskFailed() {
        Log.d("mathLog","Lives left before the fail: $livesLeft")
        if(--livesLeft <=0) isGameOver=true
        Log.d("mathLog","Lives left after the fail: $livesLeft")
    }

    fun evaluateResponse(response:Int, timeLeft:Int){
        val bonus = if (gameSettings.rewardPerformance) timeLeft/10 else 0
        when(type) {
            GameTypesEnum.ADDITION ->{
                Log.d("mathLog","${response} = ${currentTask.first} + ${currentTask.second} ?")
                if(response == currentTask.first+currentTask.second) taskSuccessfulWithBonus(bonus)
                else taskFailed()
            }
            GameTypesEnum.MULTIPLICATION ->{
                Log.d("mathLog","${response} = ${currentTask.first} * ${currentTask.second} ?")
                if(response == currentTask.first*currentTask.second) taskSuccessfulWithBonus(bonus)
                else taskFailed()}
        }
    }

}