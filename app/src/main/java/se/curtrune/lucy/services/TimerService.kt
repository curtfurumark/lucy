package se.curtrune.lucy.services

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import se.curtrune.lucy.services.ServiceConstants.ACTION_START_STOPWATCH
import se.curtrune.lucy.services.TimeServiceConstants.ACTION_CANCEL_COUNTDOWN_TIMER
import se.curtrune.lucy.services.TimeServiceConstants.ACTION_CANCEL_STOPWATCH
import se.curtrune.lucy.services.TimeServiceConstants.ACTION_PAUSE_COUNTDOWN_TIMER
import se.curtrune.lucy.services.TimeServiceConstants.ACTION_PAUSE_STOPWATCH
import se.curtrune.lucy.services.TimeServiceConstants.ACTION_RESUME_COUNTDOWN_TIMER
import se.curtrune.lucy.services.TimeServiceConstants.ACTION_START_COUNTDOWN_TIMER
import se.curtrune.lucy.services.TimeServiceConstants.ACTION_START_OR_RESUME_STOPWATCH

class TimerService: LifecycleService() {
    private var stopwatchRunning = false
    private var countdownRunning = false
    private var seconds: Long = 0
    private var secondsRemaining = 0L
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent.let {
            when(it!!.action){
                ACTION_START_COUNTDOWN_TIMER->{
                    println("TimerService.ACTION_START_COUNTDOWN_TIMER")
                    countdownDone.value = false
                    countdownRunning = true
                    secondsRemaining = it.getLongExtra("COUNTDOWN_DURATION", 0)
                    println("seconds remaining $secondsRemaining")
                    startCountDown()
                }
                ACTION_PAUSE_COUNTDOWN_TIMER->{
                    println("TimerService.ACTION_PAUSE_COUNTDOWN_TIMER")
                    stopwatchRunning = false
                }
                ACTION_RESUME_COUNTDOWN_TIMER->{
                    println("TimerService.ACTION_RESUME_COUNTDOWN_TIMER")
                    startCountDown()
                }
                ACTION_CANCEL_COUNTDOWN_TIMER->{
                    println("TimerService.ACTION_CANCEL_COUNTDOWN_TIMER")
                    countdownRunning = false
                    secondsRemaining = 0

                }
                ACTION_START_STOPWATCH->{
                    println("TimerService.ACTION_START_STOPWATCH")
                    stopwatchRunning = true
                    startStopWatch()
                }
                ACTION_START_OR_RESUME_STOPWATCH ->{
                    println("TimerService start or resume stopwatch")
                }
                ACTION_PAUSE_STOPWATCH ->{
                    stopwatchRunning = false
                    println("TimerService pause stopwatch")
                }
                ACTION_CANCEL_STOPWATCH->{
                    stopwatchRunning = false
                    seconds = 0
                    currentDuration.value = 0
                    println("TimerService cancel stopwatch")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
    private fun startStopWatch(){
        println("StopWatchService.startTimer()")
        CoroutineScope(Dispatchers.Main).launch {
            while(stopwatchRunning){
                seconds++
                currentDuration.value = seconds
                delay(1000)
                println("StopWatchService.seconds $seconds")
            }
        }
    }
    private fun startCountDown(){
        println("TimerService.startCountDown()")
        CoroutineScope(Dispatchers.Main).launch {
            while(countdownRunning){
                secondsRemaining--
                timeRemaining.value = secondsRemaining
                delay(1000)
                println("TimerService.seconds remaining  $secondsRemaining")
                if( secondsRemaining <= 0){
                    countdownDone.value = true
                    countdownRunning = false
                }
            }
        }

    }
    companion object{
        val currentDuration = MutableLiveData<Long>(0)
        val timeRemaining = MutableLiveData<Long>(0)
        val countdownDone = MutableLiveData<Boolean>(false)
    }

}