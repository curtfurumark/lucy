package se.curtrune.lucy.screens.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
//import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.appointments.composables.AppointmentDetailsScreen
import se.curtrune.lucy.screens.appointments.composables.AppointmentsScreen
import se.curtrune.lucy.screens.daycalendar.composables.DayCalendarScreen
import se.curtrune.lucy.screens.dev.composables.DevScreen
import se.curtrune.lucy.screens.duration.composables.DurationScreen
import se.curtrune.lucy.screens.item_editor.composables.ItemEditorScreen
import se.curtrune.lucy.screens.medicine.composable.MedicineScreen
import se.curtrune.lucy.screens.message_board.composables.MessageBoardScreen
import se.curtrune.lucy.screens.monthcalendar.composables.MonthCalendarScreen
import se.curtrune.lucy.screens.projects.composables.ProjectsScreen
import se.curtrune.lucy.screens.projects.composables.TabbedProjectsScreen
import se.curtrune.lucy.screens.settings.composables.SettingsScreen
import se.curtrune.lucy.screens.webscreen.WebScreen
import se.curtrune.lucy.screens.week_calendar.composables.WeekCalendarScreen

@Serializable
data class DayCalendarNavKey(val date: String): NavKey

@Serializable
data object DevScreenNavKey: NavKey
@Serializable
data object DurationNavKey: NavKey

@Serializable
data class WeekCalendarNavKey(val date: String): NavKey

@Serializable
data object MedicineNavKey: NavKey

@Serializable
data object MessageBoardNavKey: NavKey
@Serializable
data class MonthCalendarNavKey(val date: String): NavKey

@Serializable
data class ItemEditorNavKey(val item: @Contextual Item): NavKey

@Serializable
data object SettingsScreenNavKey: NavKey

@Serializable
data object ProjectsScreenNavKey: NavKey

@Serializable
data class AppointmentDetailsScreenNavKey(val appointmentID: Long): NavKey

@Serializable
data object AppointmentsScreenNavKey: NavKey

@Serializable
data object WebScreenNavKey: NavKey
@Serializable
data object TabbedProjectsScreenNavKey: NavKey

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationRoot(modifier: Modifier = Modifier, backStack: NavBackStack<NavKey>) {
    println("NavigationRoot()")
    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ),
        entryProvider = { navKey->
            when(navKey) {
                is AppointmentDetailsScreenNavKey -> {
                    NavEntry(
                        key = navKey) { AppointmentDetailsScreen(navKey.appointmentID)
                    }
                }
                is AppointmentsScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        AppointmentsScreen(
                            onEdit = {item->
                                backStack.add(ItemEditorNavKey(item))
                            }
                            ,navigate = {
                                backStack.add(it)
                            }
                        )
                    }
                }
                is DayCalendarNavKey -> {
                    NavEntry(
                        key = navKey) {
                        println("DayCalendarNavKey.date = ${navKey.date}")
                        DayCalendarScreen(
                            date = navKey.date,
                            onEdit = {
                                backStack.add(ItemEditorNavKey(it))
                        })
                    }
                }
                is DevScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        DevScreen()
                    }
                }
                is DurationNavKey -> {
                    NavEntry(
                        key = navKey) {
                        DurationScreen(onEvent = {})
                    }
                }
                is MedicineNavKey -> {
                    NavEntry(
                        key = navKey) {
                        MedicineScreen()
                    }
                }
                is MessageBoardNavKey->{
                    NavEntry(
                        key = navKey) {
                        MessageBoardScreen()
                    }
                }
                is ProjectsScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        ProjectsScreen(onNavigate = {
                            backStack.add(it)
                        })
                    }
                }
                is TabbedProjectsScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        TabbedProjectsScreen()
                    }
                }
                is WebScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        WebScreen()
                    }
                }
                is WeekCalendarNavKey -> {
                    NavEntry(
                        key = navKey) {
                        WeekCalendarScreen(onPagerChange = {}, navigate = {
                            println("navigate to $it")
                            backStack.add(it)
                        })
                    }
                }
                is ItemEditorNavKey -> {
                    NavEntry(
                        key = navKey) {
                        ItemEditorScreen(navKey.item, onSave = {
                            backStack.removeLastOrNull()
                        })
                    }
                }
                is MonthCalendarNavKey -> {
                    NavEntry(
                        key = navKey
                    ) {
                        MonthCalendarScreen(navigate = {
                            backStack.add(it)
                        })
                    }
                }
                is SettingsScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                            SettingsScreen()
                    }
                }
                else -> {
                    throw IllegalArgumentException("Unknown key: $navKey")
                }
            }
        }
    )
}


@Composable
fun HelloNavScreen(){
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center){
        Text(text = "hello, i am hello  nav screen")
    }
}