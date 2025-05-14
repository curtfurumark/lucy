package se.curtrune.lucy.classes.calender

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import kotlinx.serialization.Serializable
import java.util.jar.Attributes

@Entity
@Serializable
data class Holiday(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,
    val code: String,
    @Embedded
    val name: Name // Embedded Name object
)

@Serializable
data class Name(
    val en: String,
    val sv: String
)

