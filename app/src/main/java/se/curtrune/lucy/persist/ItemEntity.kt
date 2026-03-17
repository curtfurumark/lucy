package se.curtrune.lucy.persist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var parentId: Long = 0,
    val heading: String = "",
    val duration: Long = 0,
    val anxiety: Int = 0,
    val energy: Int = 0,
    var mood: Int = 0,
    var stress : Int = 0,
    val comment: String = "",
    val category: String = ""
    )
