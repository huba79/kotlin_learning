package com.example.mathgame
import android.util.Log
import java.io.Serializable
import kotlin.random.Random

class Game(val type:GamesEnum) : Serializable {

    var isGameOver = false
    var livesLeft = 3
    var currentScore = 0
    lateinit var currentTask: Pair<Int, Int>

    fun getNewTask(){
        val limit = if (type.equals(GamesEnum.ADDITION)) 50 else  20
        val operand1 =  Random.nextInt(1, limit)
        val operand2 =  Random.nextInt(1, limit)
        currentTask = Pair(operand1, operand2)
    }
    fun taskSuccessful(){
         currentScore+=10
//         getNewTask()
    }

    fun taskFailed() {
        Log.d("resultCheck","Lives left before the fail: $livesLeft")
        if(--livesLeft <=0) isGameOver=true
        Log.d("resultCheck","Lives left after the fail: $livesLeft")
    }

    fun evaluate(response:Int){
        when(type) {
            GamesEnum.ADDITION ->{
                Log.d("resultCheck","${response} = ${currentTask.first} + ${currentTask.second} ?")
                if(response == currentTask.first+currentTask.second) taskSuccessful()
                else taskFailed()
            }
            GamesEnum.MULTIPLICATION ->{
                Log.d("resultCheck","${response} = ${currentTask.first} * ${currentTask.second} ?")
                if(response == currentTask.first*currentTask.second) taskSuccessful()
                else taskFailed()}
        }
    }

}