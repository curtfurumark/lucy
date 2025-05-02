package se.curtrune.lucy.screens.user_settings.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.google_calendar.GoogleCalendar
import se.curtrune.lucy.screens.user_settings.UserEvent
import se.curtrune.lucy.screens.user_settings.UserState


@Composable
fun UserSettingSyncWithGoogleCalendar(state: UserState, onEvent: (UserEvent)->Unit){
    var syncWithGoogle by remember {
        mutableStateOf(state.syncWithGoogle)
    }
    var calendarID by remember {
        mutableStateOf(state.googleCalendarID)
    }
    var showCalendars by remember {
        mutableStateOf(false)
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.sync_with_google_calendar))
                Checkbox(checked = syncWithGoogle, onCheckedChange = {
                    syncWithGoogle = !syncWithGoogle
                    onEvent(UserEvent.SyncWithGoogle(syncWithGoogle))
                })
            }
            AnimatedVisibility(visible = syncWithGoogle) {
                Column() {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = stringResource(R.string.choose_a_calendar))
                    }
                    Row(modifier = Modifier.fillMaxWidth()){
                        Text(text = "sync with calendar id: ${state.googleCalendarID}")
                    }
                    Row(modifier = Modifier.fillMaxWidth()){
                        Button(onClick = {
                            onEvent(UserEvent.GetEvents(state.googleCalendarID))
                        }){
                            Text(text = "get events")
                        }
                        Button(onClick = {
                            onEvent(UserEvent.ImportEvents(state.googleCalendarID))
                            showCalendars = !showCalendars
                        }){
                            Text(text = "import google events")
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        val calendars = LucindaApplication.appModule.googleCalendarModule.getCalendars()
                        Column(modifier = Modifier.fillMaxWidth()) {
                            calendars.forEach { calendar ->
                                GoogleCalendarComposable(calendar = calendar, onEvent = onEvent)
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun GoogleCalendarComposable(calendar: GoogleCalendar, onEvent: (UserEvent) -> Unit){
    Row(modifier = Modifier.fillMaxWidth()){
        Text(
            modifier = Modifier.clickable {
                onEvent(UserEvent.GoogleCalendar(calendar.id))
            },
            text = "id: ${calendar.id}, name: ${calendar.displayName}"
        )
    }
}
@Preview
@Composable
fun PreviewSyncWithGoogleEtc(){
    LucyTheme {
        val state = UserState()
        state.syncWithGoogle = true
        UserSettingSyncWithGoogleCalendar(state = state, onEvent = {})
    }
}