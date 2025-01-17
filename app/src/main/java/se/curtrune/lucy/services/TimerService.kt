package se.curtrune.lucy.services

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import se.curtrune.lucy.services.TimeServiceConstants.ACTION_CANCEL_COUNTDOWN_TIMER
import se.curtrune.lucy.services.TimeServiceConstants.ACTION_PAUSE_COUNTDOWN_TIMER
import se.curtrune.lucy.services.TimeServiceConstants.ACTION_RESUME_COUNTDOWN_TIMER
import se.curtrune.lucy.services.TimeServiceConstants.ACTION_START_COUNTDOWN_TIMER

class TimerService: LifecycleService() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent.let {
            when(it!!.action){
                ACTION_START_COUNTDOWN_TIMER->{
                    println("start timer")
                }
                ACTION_PAUSE_COUNTDOWN_TIMER->{
                    println("pause timer")
                }
                ACTION_RESUME_COUNTDOWN_TIMER->{
                    println("resume service")
                }
                ACTION_CANCEL_COUNTDOWN_TIMER->{
                    println("cancel service")
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