package se.curtrune.lucy.services

import se.curtrune.lucy.LucindaApplication

object UpdaterRepeats {
    fun updateRepeats(){
        val repository = LucindaApplication.repository
        val repeats = repository.selectRepeats()
        val infiniteRepeats = repeats.filter { repeat-> repeat.isInfinite }

    }
}