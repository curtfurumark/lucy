package se.curtrune.lucy.classes

import kotlinx.serialization.Serializable

@Serializable
data class ItemDuration(
    var type:  Type = Type.SECONDS
){
    var seconds: Long = 0
    enum class Type{
        SECONDS, DAY, WEEK, MONTH, YEAR
    }
    init {
        type = Type.SECONDS
    }
    fun setHours(hour: Int){
        seconds += hour * 3600
    }
    fun setMinutes(minutes: Int){
        seconds += minutes * 60
    }
    fun setSeconds(seconds: Int){
        this.seconds += seconds
    }
}
