package se.curtrune.lucy.screens.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import se.curtrune.lucy.classes.item.Item

sealed interface Route: NavKey{
    @Serializable
    data class AppointmentScreenNavKey(val appointment: Item): Route

    @Serializable
    data class AttachFileScreenNavKey(val item: Item): Route

    @Serializable
    data class AttachImageScreenNavKey(val item: Item): Route

    @Serializable
    data object AppointmentsScreenNavKey: Route
    @Serializable
    data object BulletListScreenNavKey: Route
    @Serializable
    data object CreateTemplateScreenNavKey: Route
    @Serializable
    data class DayCalendarNavKey(val date: String): Route
    @Serializable
    data object DurationNavKey: Route
    @Serializable
    data object DevScreenNavKey: Route
    @Serializable
    data class  EditListNavKey(val parent: Item): Route

    @Serializable
    data class EditTemplateScreenNavKey(val templateID: Long): Route

    @Serializable
    data class FileViewerScreenNavKey(val item: Item): Route

    @Serializable
    data class ItemEditorNavKey(val item: @Contextual Item): Route

    @Serializable
    data object MedicineNavKey: Route
    @Serializable
    data object MentalStatsScreenNavKey: Route

    @Serializable
    data object MessageBoardNavKey: Route

    @Serializable
    data class MonthCalendarNavKey(val date: String): Route

    @Serializable
    data object MyDayScreenNavKey: Route
    @Serializable
    data object MyManualScreenNavKey: Route
    @Serializable
    data object ProjectsScreenNavKey: Route


    @Serializable
    data object SettingsScreenNavKey: Route



    @Serializable
    data object TabbedProjectsScreenNavKey: Route



    @Serializable
    data object TemplatesScreenNavKey: NavKey

    @Serializable
    data object TimeLineScreen: Route
    @Serializable
    data object TodoScreenNavKey: Route

    @Serializable
    data object VoiceRecordingScreenNavKey: Route
    @Serializable
    data class WeekCalendarNavKey(val date: String): Route
    @Serializable
    data class WebScreenNavKey(val url: String): Route
}