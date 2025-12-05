package se.curtrune.lucy.classes

import com.google.gson.Gson
import java.io.Serializable

@kotlinx.serialization.Serializable
class Reward {
    private val message: String? = null

    enum class Type {
        AFFIRMATION, USER_DEFINED, CONFETTI
    }

    var type: Type? = null

    fun toJson(): String? {
        return Gson().toJson(this)
    }
}
