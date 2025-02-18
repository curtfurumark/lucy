package se.curtrune.lucy.modules

import android.app.Application
import android.content.Intent
import se.curtrune.lucy.services.TimerService
import se.curtrune.lucy.services.TimerService.Companion.ACTION_CANCEL_STOPWATCH
import se.curtrune.lucy.services.TimerService.Companion.ACTION_PAUSE_STOPWATCH
import se.curtrune.lucy.services.TimerService.Companion.ACTION_START_STOPWATCH

class TimeModule(private val application: Application){
    fun cancelTimer(){
        sendCommand(ACTION_CANCEL_STOPWATCH)
    }
    fun startTimer(itemID: Long){
        sendCommand(ACTION_START_STOPWATCH)
    }
    fun pauseTimer(){
        sendCommand(ACTION_PAUSE_STOPWATCH)
    }
    fun resumeTimer(){
        sendCommand(ACTION_START_STOPWATCH)
    }

    fun sendCommand(command: String){
        println("...sendCommand($command)")
        Intent( application, TimerService::class.java).also {
            it.action = command
            application.startService(it)
        }
    }
}