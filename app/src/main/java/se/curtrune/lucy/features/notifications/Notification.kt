package se.curtrune.lucy.features.notifications

import se.curtrune.lucy.util.Converter
import se.curtrune.lucy.util.Logger.Companion.log
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale

@kotlinx.serialization.Serializable
class Notification {
    enum class Type {
        PENDING, ALARM, NOTIFICATION
    }

    @JvmField
    var type: Type
    private var date: Long = 0
    private var time = 0
    @JvmField
    var title: String? = null
    @JvmField
    var content: String? = null

    init {
        if (VERBOSE) log("Notification()")
        type = Type.NOTIFICATION
    }

    fun getDate(): LocalDate {
        return LocalDate.ofEpochDay(date)
    }

    fun getTime(): LocalTime {
        return LocalTime.ofSecondOfDay(time.toLong())
    }

    fun setDate(date: LocalDate) {
        this.date = date.toEpochDay()
    }

    fun setDate(string: String?) {
        date = LocalDate.parse(string).toEpochDay()
    }


    fun setTime(time: LocalTime) {
        this.time = time.toSecondOfDay()
    }

    fun setTime(string: String?) {
        time = LocalTime.parse(string).toSecondOfDay()
    }


    override fun toString(): String {
        return String.format(
            Locale.getDefault(),
            "%s %s %s",
            type.toString(),
            getDate().toString(),
            Converter.format(getTime())
        )
    }

    companion object {
        var VERBOSE: Boolean = false
    }
}
