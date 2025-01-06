package se.curtrune.lucy.services

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData

class TimerService: LifecycleService() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent.let {
            when(it!!.action){
                "start"->{}
                "pause"->{}
                "resume"->{}
                "cancel"->{}
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
    companion object{
        val currentDuration = MutableLiveData<Long>(0)
        val timeRemaining = MutableLiveData<Long>(0)
    }

}