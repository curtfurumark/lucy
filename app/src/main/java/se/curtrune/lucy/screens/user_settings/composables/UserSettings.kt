package se.curtrune.lucy.screens.user_settings.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.activities.kotlin.composables.user_settings.Categories
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.composables.CategoryDialog
import se.curtrune.lucy.screens.user_settings.UserEvent
import se.curtrune.lucy.screens.user_settings.UserState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserSettings(state: UserState, onEvent: (UserEvent)->Unit){
    val scrollState = rememberScrollState()
    var showCategoryDialog by remember {
        mutableStateOf(false)
    }
    Column (modifier = Modifier.fillMaxSize()
        //.verticalScroll(state = scrollState)
        .padding(8.dp))
    {
        DarkModeSetting(state = state, onEvent = onEvent)
        Spacer(modifier = Modifier.height(8.dp))
        LanguageSetting(state = state, onEvent = onEvent)
        Spacer(modifier = Modifier.height(8.dp))
        MentalFlagSetting(state = state, onEvent = onEvent)
        Spacer(modifier = Modifier.height(8.dp))
        PanicButton(state = state, onEvent = onEvent)
        Spacer(modifier = Modifier.height(8.dp))
        ShowMentalStatusSetting(state = state,  onEvent = onEvent)
        Spacer(modifier = Modifier.height(8.dp))
        PassWordSetting(state = state, onEvent = onEvent)
        Spacer(modifier = Modifier.height(8.dp))
        Categories(state = state, onEvent = onEvent)
        Spacer(modifier = Modifier.height(8.dp))
        UserSettingSyncWithGoogleCalendar(state = state, onEvent = onEvent)
        Spacer(modifier = Modifier.height(8.dp))
        CalendarEventsList(state = state)
    }
    if( showCategoryDialog) {
        CategoryDialog(
            category = "",
            dismiss = {
                showCategoryDialog = false
            },
            onCategory = {
                onEvent(UserEvent.AddCategory(it))
                showCategoryDialog = false
            })
    }
}


@Composable
fun ColorsSetting(){
    Card(modifier = Modifier.fillMaxWidth()){
        Text("colorsetting")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@PreviewLightDark
fun PreviewUserSettings(){
    LucyTheme {
        UserSettings(state = UserState(), onEvent = {})

    }
}