package se.curtrune.lucy.persist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var contentType: String,
    var contentUri: String
)
@Entity
data class ItemContent(
    val itemId: Long,
    val contentId: Long
)