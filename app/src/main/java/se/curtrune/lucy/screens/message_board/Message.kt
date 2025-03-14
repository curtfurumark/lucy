package se.curtrune.lucy.screens.message_board

import java.time.LocalDateTime
import java.time.ZoneOffset

import android.os.Build
import androidx.annotation.RequiresApi
//import kotlinx.serialization.Serializable
//@Serializable
data class Message(
    @JvmField
    var subject: String = "",
    @JvmField
    var content: String = "",
    @JvmField
    var category: String? = null,
    @JvmField
    var user: String = "",
    @JvmField
    var id: Long = 0,
    @JvmField
    var created: Long = 0

)
{

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCreated(): LocalDateTime {
        return LocalDateTime.ofEpochSecond(created, 0, ZoneOffset.UTC)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setCreated(created: LocalDateTime) {
        this.created= created.toEpochSecond(ZoneOffset.UTC)
    }
}
