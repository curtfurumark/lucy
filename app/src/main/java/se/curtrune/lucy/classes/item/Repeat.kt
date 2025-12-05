package se.curtrune.lucy.classes.item

import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
@Serializable
data class Repeat(
    var id: Long = -1, 
    var templateID: Long = 0,
    var qualifier: Int = 1,
    var unit: Unit = Unit.DAY,
    val weekDays: List<DayOfWeek> = emptyList(),
    @Serializable(with = DateSerializer::class)
    var firstDate: LocalDate = LocalDate.now(),
    //var firstDateEpoch: Long = 0,
    @Serializable(with = DateSerializer::class)
    var lastDate: LocalDate? = null,
    @Serializable(with = DateSerializer::class)
    var lastActualDate: LocalDate? = null,
    var isInfinite: Boolean = true
){
    enum class Unit{
        DAY, WEEK, MONTH, YEAR
    }
    override fun toString(): String {
        return String.format(Locale.getDefault(), "repeat every %d %s", qualifier, unit.name)
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalDate::class)
object DateSerializer : KSerializer<LocalDate> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString(), formatter)
    }
}