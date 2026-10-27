package se.curtrune.lucy.classes

import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.util.Logger.Companion.log
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.Locale

class Mental:  Serializable {
    @JvmField
    var anxiety: Int
    @JvmField
    var stress: Int
    @JvmField
    var mood: Int
    @JvmField
    var energy: Int

    constructor(anxiety: Int, energy: Int, mood: Int, stress: Int) {
        if (VERBOSE) log("Mental(int, int, int, int)")
        this.anxiety = anxiety
        this.energy = energy
        this.mood = mood
        this.stress = stress
    }

    enum class Type {
        ENERGY, ANXIETY, STRESS, MOOD
    }

    constructor() {
        anxiety = 0
        energy = anxiety
        mood = energy
        stress = mood
    }
    override fun toString(): String {
        return String.format(
            Locale.getDefault(),
            "Mental{e=%d, s=%d, a=%d, m=%d}",
            energy,
            stress,
            anxiety,
            mood,
        )
    }
    companion object {
        var VERBOSE: Boolean = false
    }
}


