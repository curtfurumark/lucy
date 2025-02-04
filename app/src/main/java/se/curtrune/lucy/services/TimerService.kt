package se.curtrune.lucy.services

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import se.curtrune.lucy.services.ServiceConstants.ACTION_START_STOPWATCH
import se.curtrune.lucy.services.TimeServiceConstants.ACTION_CANCEL_COUNTDOWN_TIMER
import se.curtrune.lucy.services.TimeServiceConstants.ACTION_CANCEL_STOPWATCH
import se.curtrune.lucy.services.TimeServiceConstants.ACTION_PAUSE_COUNTDOWN_TIMER
import se.curtrune.lucy.services.TimeServiceConstants.ACTION_PAUSE_STOPWATCH
import se.curtrune.lucy.services.TimeServiceConstants.ACTION_RESUME_COUNTDOWN_TIMER
import se.curtrune.lucy.services.TimeServiceConstants.ACTION_START_COUNTDOWN_TIMER
import se.curtrune.lucy.services.TimeServiceConstants.ACTION_START_OR_RESUME_STOPWATCH

class TimerService: LifecycleService() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent.let {
            when(it!!.action){
                ACTION_START_COUNTDOWN_TIMER->{
                    println("TimerService.start timer")
                }
                ACTION_PAUSE_COUNTDOWN_TIMER->{
                    println("TimerService pause timer")
                }
                ACTION_RESUME_COUNTDOWN_TIMER->{
                    println("TimerService resume service")
                }
                ACTION_CANCEL_COUNTDOWN_TIMER->{
                    println("TimerService cancel service")
                }
                ACTION_START_STOPWATCH->{
                    println("TimerService start stopwatch")
                }
                ACTION_START_OR_RESUME_STOPWATCH ->{
                    println("TimerService start or resume stopwatch")
                }
                ACTION_PAUSE_STOPWATCH ->{
                    println("TimerService pause stopwatch")
                }
                ACTION_CANCEL_STOPWATCH->{
                    println("TimerService cancel stopwatch")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
    companion object{
        val currentDuration = MutableLiveData<Long>(0)
        val timeRemaining = MutableLiveData<Long>(0)
    }

}