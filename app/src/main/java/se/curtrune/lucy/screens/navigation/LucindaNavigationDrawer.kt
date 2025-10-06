package se.curtrune.lucy.screens.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.ui.theme.LucyTheme
import se.curtrune.lucy.app.LucindaApplication
import java.time.LocalDate



@Composable
fun LucindaNavigationDrawer(onClick: (NavKey)->Unit, state: NavigationDrawerState = NavigationDrawerState()){
    println("LucindaNavigationDrawer(onClick, state)")
    var calendarsVisible by remember{
        mutableStateOf(true)
    }
    var showDuration by remember {
        mutableStateOf(true)
    }
    var showMedicine by remember {
        mutableStateOf(true)
    }
    val settings = LucindaApplication.appModule.userSettings
    ModalDrawerSheet {
        //Text(text = "new navigation drawer")
        Spacer(modifier = Modifier.height(32.dp))

        AnimatedVisibility(visible = calendarsVisible) {
            Column(modifier = Modifier.fillMaxWidth()) {
                NavigationDrawerItem(
                    label = { Text(text = stringResource(R.string.day_calendar)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "day"
                        )
                    },
                    selected = false,
                    onClick = {
                        println("day calendar on click")
                        onClick(DayCalendarNavKey(LocalDate.now().toString()))
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = stringResource(R.string.week)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CalendarViewWeek,
                            contentDescription = "week"
                        )
                    },
                    selected = false,
                    onClick = {
                        println("day calendar on click")
                        onClick(WeekCalendarNavKey(LocalDate.now().toString()))
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = stringResource(R.string.month)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "month"
                        )
                    },
                    selected = false,
                    onClick = {
                        onClick(MonthCalendarNavKey(LocalDate.now().toString()))
                    }
                )
                if( state.showAppointmentsLink) {
                    NavigationDrawerItem(
                        label = { Text(text = stringResource(R.string.appointments)) },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Event,
                                contentDescription = "home"
                            )
                        },
                        onClick = {
                            println("appointments")
                            onClick(AppointmentsScreenNavKey)
                        }
                    )
                }
            }
        }


        if( state.showProjectsLink) {
            NavigationDrawerItem(
                label = { Text(text = stringResource(R.string.projects)) },
                selected = false,
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.List,
                        contentDescription = "projects"
                    )
                },
                onClick = {
                    println("projects on click")
                    onClick(ProjectsScreenNavKey)
                }
            )
            NavigationDrawerItem(
                label = { Text(text = stringResource(R.string.lists))},
                selected = false,
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.List,
                        contentDescription = "listor")
                       },
                onClick = {
                    onClick(TabbedProjectsScreenNavKey)
                }
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        if( false) {
            NavigationDrawerItem(
                label = { Text(text = "matisse") },
                selected = false,
                onClick = {
                    println("settings on click")
                    onClick(WebScreenNavKey)

                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "settings"
                    )
                },
            )
        }
/*        Text(
            text = stringResource(R.string.health),
            modifier = Modifier.clickable{
                healthVisible = !healthVisible
            })*/
        //AnimatedVisibility(visible = healthVisible) {
        if( state.showMedicineLink) {
            Column(modifier = Modifier.fillMaxWidth()) {
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.medicines)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Medication,
                            contentDescription = "medicines"
                        )
                    },
                    selected = false,
                    onClick = {
                        onClick(MedicineNavKey)
                    }
                )
            }
        }
        NavigationDrawerItem(
            label = { Text(text = "dev screen") },
            selected = false,
            icon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "settings"
                )
            },
            onClick = {
                onClick(DevScreenNavKey)
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        NavigationDrawerItem(
            label = { Text(text = stringResource(R.string.settings)) },
            selected = false,
            icon = {Icon(imageVector = Icons.Default.Settings, contentDescription = "settings")},
            onClick = {
                println("settings on click")
                onClick(SettingsScreenNavKey)
            }
        )
        if( state.showDurationLink) {
            Spacer(modifier = Modifier.height(32.dp))
            NavigationDrawerItem(
                label = { Text(text = stringResource(R.string.duration)) },
                selected = false,
                onClick = {
                    onClick(DurationNavKey)
                }
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        NavigationDrawerItem(
            label = { Text(text = stringResource(R.string.message_board)) },
            selected = false,
            icon = {Icon(imageVector = Icons.Default.MeetingRoom, contentDescription = "message board")},
            onClick = {
                onClick(MessageBoardNavKey)
            }
        )
    }
}

@Composable
@PreviewLightDark
fun PreviewNavigationDrawer(){
    LucyTheme {
        LucindaNavigationDrawer(onClick = {})
    }
}