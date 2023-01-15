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
        //TODO implement performance bias controlled by gameSettings.timeleftBias
        currentScore+=10 +bonus
    }

    fun taskFailed() {
        Log.d("mathLog","Lives left before the fail: $livesLeft")
        if(--livesLeft <=0) isGameOver=true
        Log.d("mathLog","Lives left after the fail: $livesLeft")
    }

    fun evaluate(response:Int){
        when(type) {
            GameTypesEnum.ADDITION ->{
                Log.d("mathLog","${response} = ${currentTask.first} + ${currentTask.second} ?")
                if(response == currentTask.first+currentTask.second) taskSuccessful()
                else taskFailed()
            }
            GameTypesEnum.MULTIPLICATION ->{
                Log.d("mathLog","${response} = ${currentTask.first} * ${currentTask.second} ?")
                if(response == currentTask.first*currentTask.second) taskSuccessful()
                else taskFailed()}
        }
    }
    fun evaluateWithBonus(response:Int, timeLeft:Int){
        when(type) {
            GameTypesEnum.ADDITION ->{
                Log.d("mathLog","${response} = ${currentTask.first} + ${currentTask.second} ?")
                if(response == currentTask.first+currentTask.second) taskSuccessfulWithBonus(if (gameSettings.rewardPerformance) timeLeft/10 else 0)
                else taskFailed()
            }
            GameTypesEnum.MULTIPLICATION ->{
                Log.d("mathLog","${response} = ${currentTask.first} * ${currentTask.second} ?")
                if(response == currentTask.first*currentTask.second) taskSuccessfulWithBonus(if (gameSettings.rewardPerformance) timeLeft/10 else 0)
                else taskFailed()}
        }
    }

}