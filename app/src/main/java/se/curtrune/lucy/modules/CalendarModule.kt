package se.curtrune.lucy.modules

import android.app.Application

class CalendarModule(application: Application) {
    init{
        println("CalendarModule.init()")
        val cr = application.contentResolver
        if( cr != null){
            println("got cr")
        }else{
            println("cr is null")
        }
    }
}