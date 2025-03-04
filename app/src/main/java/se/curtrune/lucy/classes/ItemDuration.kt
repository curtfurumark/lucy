package se.curtrune.lucy.classes

data class ItemDuration(
    val type:  Type
){
    enum class Type{
        DAY, WEEK, MONTH, YEAR
    }
}
