package se.curtrune.lucy.util

import java.util.Locale

fun String.johanna(): String{
    return this.lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}