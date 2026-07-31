package se.curtrune.lucy.screens.navigation

//import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
//import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
//import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
//import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.appoinment.composables.AppointmentScreen
import se.curtrune.lucy.screens.appointments.composables.AppointmentsScreen
import se.curtrune.lucy.screens.attach_image.AttachImageScreen
import se.curtrune.lucy.screens.bullet_list.BulletListScreen
import se.curtrune.lucy.screens.daycalendar.composables.DayCalendarScreen
import se.curtrune.lucy.screens.dev.composables.DevScreen
import se.curtrune.lucy.screens.duration.composables.DurationScreen
import se.curtrune.lucy.screens.file_attach_screen.FileAttachScreen
import se.curtrune.lucy.screens.file_viewer.FileViewerScreen
import se.curtrune.lucy.screens.item_editor.composables.ItemEditorScreen
import se.curtrune.lucy.screens.lists.composables.EditableBulletListScreen
import se.curtrune.lucy.screens.medicine.composable.MedicineScreen
import se.curtrune.lucy.screens.mental_stats.composables.MentalStatsScreen
import se.curtrune.lucy.screens.message_board.composables.MessageBoardScreen
import se.curtrune.lucy.screens.monthcalendar.composables.MonthCalendarScreen
import se.curtrune.lucy.screens.my_day.composables.MyDayScreen
import se.curtrune.lucy.screens.my_manual.MyManualScreen
import se.curtrune.lucy.screens.projects.composables.ProjectsScreen
import se.curtrune.lucy.screens.tabbed.TabbedProjectsScreen
import se.curtrune.lucy.screens.settings.composables.SettingsScreen
import se.curtrune.lucy.screens.templates.create.CreateTemplateScreen
import se.curtrune.lucy.screens.templates.edit.EditTemplateScreen
import se.curtrune.lucy.screens.templates.templates.TemplatesScreen
import se.curtrune.lucy.screens.timeline.composables.TimeLineScreen
import se.curtrune.lucy.screens.todo.composables.TodoScreen
import se.curtrune.lucy.screens.voice_recording_screen.VoiceRecordingScreen
import se.curtrune.lucy.screens.webscreen.WebScreen
import se.curtrune.lucy.screens.week_calendar.composables.WeekCalendarScreen


sealed interface Route: NavKey{
    @Serializable
    data class AppointmentScreenNavKey(val appointment: Item): Route

    @Serializable
    data class AttachFileScreenNavKey(val item: Item): Route{
    }
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
    data class FileViewerScreenNavKey(val item: Item): Route

    @Serializable
    data object MedicineNavKey: Route
    @Serializable
    data object MentalStatsScreenNavKey: Route
    @Serializable
    data object MessageBoardNavKey: Route

   @Serializable
   data object MyDayScreenNavKey: Route
    @Serializable
    data object MyManualScreenNavKey: Route
    @Serializable
    data object TimeLineScreen: Route
    @Serializable
    data object TodoScreenNavKey: Route

    @Serializable
    data object VoiceRecordingScreenNavKey: Route
    @Serializable
    data class WeekCalendarNavKey(val date: String): Route
}

@Serializable
data class  EditListNavKey(val parent: Item): NavKey

@Serializable
data class EditTemplateScreenNavKey(val templateID: Long): NavKey

@Serializable
data class MonthCalendarNavKey(val date: String): NavKey

@Serializable
data class ItemEditorNavKey(val item: @Contextual Item): NavKey

@Serializable
data object SettingsScreenNavKey: NavKey

@Serializable
data object ProjectsScreenNavKey: NavKey

@Serializable
data object MentalStatsScreenNavKey: NavKey
@Serializable
data object TabbedProjectsScreenNavKey: NavKey

@Serializable
data object TemplatesScreenNavKey: NavKey

@Serializable
data object WebScreenNavKey: NavKey


@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationRoot(modifier: Modifier = Modifier, backStack: NavBackStack<NavKey>) {
    println("NavigationRoot()")
    NavDisplay(
        backStack = backStack,
/*        entryDecorators = listOf(
            //rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
            //rememberSceneSetupNavEntryDecorator()
        ),*/
        entryProvider = { navKey->
            when(navKey) {

                is Route.AppointmentScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        println("AppointmentScreenNavKey ${navKey.appointment.heading}")
                        AppointmentScreen(navKey.appointment, modifier = modifier, {
                            backStack.removeLastOrNull()
                        }, navigate = {
                            backStack.add(it)
                        })
                    }
                }
                is Route.AppointmentsScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        AppointmentsScreen(
                             onEdit = {item->
                                backStack.add(ItemEditorNavKey(item))
                            }
                            ,navigate = {
                                backStack.add(it)
                            },
                            modifier = modifier
                        )
                    }
                }
                is Route.AttachFileScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        FileAttachScreen(
                            modifier = modifier,
                            item = navKey.item,
                            navigate = {
                                backStack.add(it)
                            },
                            onBack = {
                                backStack.removeLastOrNull()
                            }
                        )
                    }
                }
                is Route.AttachImageScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        AttachImageScreen(
                            modifier = modifier,
                            item = navKey.item,
                            navigate = {
                                backStack.add(it)
                            }
                        )
                    }
                }
                is Route.BulletListScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                            BulletListScreen()
                    }
                }
                is Route.CreateTemplateScreenNavKey ->{
                    NavEntry(
                        key = navKey) { CreateTemplateScreen()
                    }
                }

                is Route.DayCalendarNavKey -> {
                    NavEntry(
                        key = navKey) {
                        DayCalendarScreen(
                            date = navKey.date,
                            navigate = {
                                backStack.add(it)
                            },
                            modifier = modifier
                        )
                    }
                }
                is Route.FileViewerScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        FileViewerScreen(navKey.item, onBack = {
                            backStack.removeLastOrNull()
                        })
                    }
                }

                is Route.MentalStatsScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        MentalStatsScreen()
                    }
                }
                is Route.MyDayScreenNavKey->{
                    NavEntry(
                        key = navKey
                    ){
                        MyDayScreen()
                    }

                }
                is Route.MyManualScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        MyManualScreen(modifier = modifier)
                    }
                }
                is Route.DevScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        DevScreen()
                    }
                }
                is Route.DurationNavKey -> {
                    NavEntry(
                        key = navKey) {
                        DurationScreen(onEvent = {})
                    }
                }
                is Route.VoiceRecordingScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        VoiceRecordingScreen(modifier = modifier)
                    }
                }
                is EditListNavKey -> {
                    NavEntry(
                        key = navKey) {
                        EditableBulletListScreen(item = navKey.parent, modifier = modifier)
                    }
                }
                is EditTemplateScreenNavKey -> {
                    NavEntry(key = navKey)
                    {
                            EditTemplateScreen(navKey.templateID, navigate = {
                                backStack.removeLastOrNull()
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
                is Route.MedicineNavKey -> {
                    NavEntry(
                        key = navKey) {
                        MedicineScreen(modifier = modifier,
                            navigate = {
                                backStack.add(it)
                            })
                    }
                }
                is MentalStatsScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        MentalStatsScreen()
                    }
                }
                is Route.MessageBoardNavKey->{
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
                is Route.TimeLineScreen -> {
                    NavEntry(
                        key = navKey) {
                        TimeLineScreen(modifier = modifier, navigate = {
                            backStack.add(it)
                        })
                    }
                }
                is TabbedProjectsScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        TabbedProjectsScreen(navigate = {
                            backStack.add(it)
                        })
                    }
                }
                is TemplatesScreenNavKey -> {
                    NavEntry(key = navKey){
                        TemplatesScreen(
                            navigate ={
                                backStack.add(it)
                            }, modifier = modifier
                        )
                    }
                }

                is Route.TodoScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        TodoScreen(navigate = {
                            backStack.add(it)},
                            modifier = modifier)
                    }
                }

                is WebScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        WebScreen()
                    }
                }
                is Route.WeekCalendarNavKey -> {
                    NavEntry(
                        key = navKey) {
                        WeekCalendarScreen(
                            onPagerChange = {},
                            navigate = {
                                println("navigate to $it")
                                backStack.add(it)
                            },
                            modifier = modifier
                        )
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
        },
        transitionSpec = {
            //fadeIn() togetherWith  fadeOut()
            slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(durationMillis = 1000)) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it })
                    //slideOutVertically(targetOffsetY = { -it })

        }
        , popTransitionSpec = {
            fadeIn() togetherWith  fadeOut()
        }
    )
}


