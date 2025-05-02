package se.curtrune.lucy.classes.calender

import kotlinx.serialization.Serializable
import java.util.jar.Attributes

@Serializable
data class Holiday(
    val date: String,
    val code: String,
    val name: Name
)
@Serializable
data class Name(
    val en: String,
    val sv: String
)
