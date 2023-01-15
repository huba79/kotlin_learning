package com.example.mathgame.game

import java.io.Serializable

class GameSettings(): Serializable {
     var livesAvailable = 3
     var timeFrame = 30
     lateinit var multiplicationRange:Pair<Int, Int>
     lateinit var additionRange:Pair<Int, Int>
     var rewardPerformance = false // if to add 10% of the time left to the score

    constructor(lives:Int, time:Int,multiRange:Pair<Int,Int>,addRange: Pair<Int, Int>, performanceReward:Boolean) : this() {
        livesAvailable=lives
        timeFrame = time
        multiplicationRange = multiRange
        additionRange=addRange
        this.rewardPerformance = performanceReward
    }


}