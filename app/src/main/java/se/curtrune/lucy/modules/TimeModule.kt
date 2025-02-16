package se.curtrune.lucy.modules

import android.app.Application
import android.content.Intent
import se.curtrune.lucy.services.TimerService
import se.curtrune.lucy.services.TimerService.Companion.ACTION_PAUSE_STOPWATCH
import se.curtrune.lucy.services.TimerService.Companion.ACTION_START_STOPWATCH

class TimeModule(val application: Application){
    fun startTimer(itemID: Long){
        sendCommand(ACTION_START_STOPWATCH)
    }
    fun pauseTimer(){
        sendCommand(ACTION_PAUSE_STOPWATCH)
    }

    fun sendCommand(command: String){
        println("...sendCommand($command)")
        Intent( application, TimerService::class.java).also {
            it.action = command
            application.startService(it)
        }
    }
}