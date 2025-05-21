package se.curtrune.lucy.services

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.workers.MentalWorker
import java.time.LocalDate
enum class Action{
    UPDATE, DATE
}

class StatisticsService: LifecycleService() {
    private val repository = LucindaApplication.appModule.repository
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("...onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }
    private fun updateStats(){
        println("...updateService()")
        val items  = repository.selectItems(LocalDate.now())
        currentMental.value = MentalWorker.getCurrentMental(LocalDate.now(), this)
    }
    companion object{
        val currentMental = MutableLiveData<Mental>()
    }
}