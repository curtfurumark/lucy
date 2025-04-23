package se.curtrune.lucy.modules

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.workers.MentalWorker
import java.time.LocalDate

class MentalModule(private val application: Application) {
    private var date: LocalDate = LocalDate.now()
    private var _current = MutableStateFlow(Mental())
    val current = _current.asStateFlow()
    private var _estimated = MutableStateFlow(Mental())
    var estimated = _estimated.asStateFlow()
    init{
        println("MentalModule.init")
        //_current.value = MentalWorker.getMental(date, application)
        //currentMental.value = MentalWorker.getMental(date, application)
    }
    companion object{
        val currentMental = MutableLiveData<Mental>()
    }
    fun update(){
        println("MentalModule.update")
        //_current =
        val mental = MentalWorker.getMental(date, application)
        //Logger.log(mental)
        _current.value = MentalWorker.getMental(date, application)
        currentMental.value = MentalWorker.getMental(date, application)
        //Logger.log(current.value)
/*        _current.update {
            current = MentalWorker.getMental(date, application)
        }*/
    }
    fun getMental(date: LocalDate): Mental {
        println("MentalModule.getMentalState")
        val mental = MentalWorker.getMental(date, application)
        println("mental: ${mental.toString()}")
        return mental
    }
    fun getCurrentMental(): Mental{
        println("MentalModule.getCurrentMental()")
        return  MentalWorker.getCurrentMental(date, application)
    }
}