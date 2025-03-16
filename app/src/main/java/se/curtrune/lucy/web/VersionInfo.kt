package se.curtrune.lucy.web

import kotlinx.serialization.Serializable

@Serializable
data class VersionInfo(
    @JvmField
    val versionName: String? = null,
    @JvmField
    val versionCode: Int = 0,
    @JvmField
    val versionInfo: String? = null,
    @JvmField
    val url: String? = null
)
