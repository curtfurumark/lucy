package se.curtrune.lucy.modules

import android.app.Application
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.workers.MentalWorker
import java.time.LocalDate

class MentalModule(private val application: Application) {
    private var date: LocalDate = LocalDate.now()
    private var _current = MutableStateFlow(Mental())
    var current = _current.asStateFlow()
    private var _estimated = MutableStateFlow(Mental())
    var estimated = _estimated.asStateFlow()
    init{
        println("MentalModule.init")
    }
    fun update(){
        println("updated mental state")
    }
    fun getMentalState(): Mental {
        println("MentalModule.getMentalState")
        val mental = MentalWorker.getMental(date, application)
        MentalWorker.getCurrentMental(date, application)
        println("mental: ${mental.toString()}")
        return mental
    }
    fun getCurrentMental(): Mental{
        println("MentalModule.getCurrentMental()")
        return  MentalWorker.getCurrentMental(date, application)
    }
}