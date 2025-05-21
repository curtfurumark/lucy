package se.curtrune.lucy.services

import se.curtrune.lucy.app.LucindaApplication

object UpdaterRepeats {
    fun updateRepeats(){
        val repository = LucindaApplication.appModule.repository
        val repeats = repository.selectRepeats()
        val infiniteRepeats = repeats.filter { repeat-> repeat.isInfinite }

    }
}