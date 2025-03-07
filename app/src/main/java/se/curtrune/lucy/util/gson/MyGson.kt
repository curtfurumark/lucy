package se.curtrune.lucy.util.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import se.curtrune.lucy.classes.ItemDuration
import java.time.LocalDate

object MyGson {
    fun getMyGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(ItemDuration.Type::class.java,
            ItemDurationSerialize()
        )
        gsonBuilder.registerTypeAdapter(LocalDate::class.java, GsonDate())
        return gsonBuilder.create()
    }
}