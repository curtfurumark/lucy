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
    var iD: Long = 0

    @JvmField
    var anxiety: Int

    //private long itemID;
    @JvmField
    var stress: Int
    @JvmField
    var mood: Int
    @JvmField
    var energy: Int
    var dateEpoch: Long = 0
        private set
    var category: String? = null
    private var time = 0
    var createdEpoch: Long = 0
        private set

    //private long updated;
    var isTemplate: Boolean = false
    var isDone: Boolean = false
        private set

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
        createdEpoch = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        dateEpoch = LocalDate.now().toEpochDay()
        time = LocalTime.now().toSecondOfDay()
        isTemplate = false
        isDone = false
        anxiety = 0
        energy = anxiety
        mood = energy
        stress = mood
    }

    constructor(item: Item) : this() {
        //this.heading = item.heading
        //this.category = item.category
        //this.itemID = item.getID();
        //this.isTemplate = item.isTemplate();
        //this.isDone = item.getState().equals(State.DONE);
    }




    fun getTime(): LocalTime {
        return LocalTime.ofSecondOfDay(time.toLong())
    }


    fun isCategory(category: String?): Boolean {
        //log("MentalType.isCategory(String) ", category);
        if (this.category == null) {
            log("this.category == null returning false")
            return false
        }
        return this.category.equals(category, ignoreCase = true)
    }

    fun setDate(date: LocalDate) {
        this.dateEpoch = date.toEpochDay()
    }

    fun setCreated(created: Long) {
        this.createdEpoch = created
    }





    fun isDone(isDone: Boolean) {
        this.isDone = isDone
    }



    override fun toString(): String {
        return String.format(
            Locale.getDefault(),
            "Mental{e=%d, s=%d, a=%d, m=%d date: %s, time %s, done: %b}",
            energy,
            stress,
            anxiety,
            mood,
            LocalDate.ofEpochDay(
                dateEpoch
            ).toString(),
            LocalTime.ofSecondOfDay(time.toLong()).toString(),
            isDone
        )
    }

    fun setCreated(created: LocalDateTime) {
        this.createdEpoch = created.toEpochSecond(ZoneOffset.UTC)
    }

    companion object {
        var VERBOSE: Boolean = false
    }
}


