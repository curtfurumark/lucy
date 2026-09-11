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
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import se.curtrune.lucy.screens.appoinment.composables.AppointmentScreen
import se.curtrune.lucy.screens.appointments.composables.AppointmentsScreen
import se.curtrune.lucy.screens.attach_image.AttachImageScreen
import se.curtrune.lucy.screens.bullet_list.BulletListScreen
import se.curtrune.lucy.screens.daycalendar.composables.DayCalendarScreen
import se.curtrune.lucy.screens.dev.composables.DevScreen
import se.curtrune.lucy.screens.duration.composables.DurationScreen
import se.curtrune.lucy.screens.file_attach_screen.FileAttachScreen
import se.curtrune.lucy.screens.file_viewer.FileViewerScreen
import se.curtrune.lucy.screens.edit.composables.ItemEditorScreen
import se.curtrune.lucy.screens.lists.composables.EditableBulletListScreen
import se.curtrune.lucy.screens.medicine.composable.MedicineScreen
import se.curtrune.lucy.screens.mental_stats.composables.MentalStatsScreen
import se.curtrune.lucy.screens.message_board.composables.MessageBoardScreen
import se.curtrune.lucy.screens.monthcalendar.composables.MonthCalendarScreen
import se.curtrune.lucy.screens.my_day.composables.MyDayScreen
import se.curtrune.lucy.screens.my_manual.MyManualScreen
import se.curtrune.lucy.screens.projects.composables.ProjectsScreen
import se.curtrune.lucy.screens.settings.composables.SettingsScreen
import se.curtrune.lucy.screens.tabbed.TabbedProjectsScreen
import se.curtrune.lucy.screens.templates.create.CreateTemplateScreen
import se.curtrune.lucy.screens.templates.edit.EditTemplateScreen
import se.curtrune.lucy.screens.templates.templates.TemplatesScreen
import se.curtrune.lucy.screens.timeline.composables.TimeLineScreen
import se.curtrune.lucy.screens.todo.composables.TodoScreen
import se.curtrune.lucy.screens.voice_recording_screen.VoiceRecordingScreen
import se.curtrune.lucy.screens.webscreen.WebScreen
import se.curtrune.lucy.screens.week_calendar.composables.WeekCalendarScreen

@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationRoot(modifier: Modifier = Modifier, backStack: NavBackStack<NavKey>) {
    println("NavigationRoot()")
    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
            //rememberSceneSetupNavEntryDecorator()
        ),
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
                                backStack.add(Route.ItemEditorNavKey(item))
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
                is Route.EditListNavKey -> {
                    NavEntry(
                        key = navKey) {
                        EditableBulletListScreen(
                            parent = navKey.parent,
                            modifier = modifier,
                            onBack = {
                                backStack.removeLastOrNull()
                            }
                        )
                    }
                }
                is Route.EditTemplateScreenNavKey -> {
                    NavEntry(key = navKey)
                    {
                            EditTemplateScreen(navKey.templateID, navigate = {
                                backStack.removeLastOrNull()
                            })
                    }
                }
                is Route.ItemEditorNavKey -> {
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

                is Route.MessageBoardNavKey->{
                    NavEntry(
                        key = navKey) {
                        MessageBoardScreen()
                    }
                }
                is Route.ProjectsScreenNavKey -> {
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
                is Route.TabbedProjectsScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        TabbedProjectsScreen(navigate = {
                            backStack.add(it)
                        })
                    }
                }
                is Route.TemplatesScreenNavKey -> {
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

                is Route.WebScreenNavKey -> {
                    NavEntry(
                        key = navKey) {
                        WebScreen(url = navKey.url)
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

                is Route.MonthCalendarNavKey -> {
                    NavEntry(
                        key = navKey
                    ) {
                        MonthCalendarScreen(navigate = {
                            backStack.add(it)
                        })
                    }
                }
                is Route.SettingsScreenNavKey -> {
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


