package se.curtrune.lucy.screens.affirmations

import kotlinx.serialization.Serializable

@Serializable
data class Quote(
    val q: String = "",
    val a: String = "",
    val h: String = ""
)
