package se.curtrune.lucy.screens.user_settings.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.activities.kotlin.composables.user_settings.Categories
import se.curtrune.lucy.screens.user_settings.UserEvent
import se.curtrune.lucy.screens.user_settings.UserState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserSettings(state: UserState, onEvent: (UserEvent)->Unit){
/*    var isDarkMode by remember {
        mutableStateOf(userSettingViewModel.isDarkMode)
    }
    var language by remember {
        mutableStateOf(userSettingViewModel.getLanguage())
    }
    var categories  by remember {
        mutableStateOf(userSettingViewModel.categories)
    }*/
    //val scrollState = rememberScrollState()
    Column(modifier = Modifier
        .fillMaxSize()
        //.verticalScroll(state = scrollState)
        .padding(8.dp)){
        UserSettingSyncWithGoogleCalendar(state = state, onEvent = onEvent)
        Spacer(modifier = Modifier.height(8.dp))
        CalendarEventsList(state = state)
        DarkModeSetting(state = state, onEvent = onEvent)
        Spacer(modifier = Modifier.height(8.dp))
        LanguageSetting(state = state, onEvent = onEvent)
        Spacer(modifier = Modifier.height(8.dp))
        Spacer(modifier = Modifier.height(8.dp))
        PanicButton()
        Spacer(modifier = Modifier.height(8.dp))
        //ColorsSetting()
        PassWordSetting(state = state, onEvent = onEvent)
        Spacer(modifier = Modifier.height(8.dp))
        Categories(state = state, onEvent = onEvent)
        }
    }
@Composable
fun PanicButton(){
    val radioButtons = listOf("url", "ice", "snake")
    var selectedOption by remember {
        mutableStateOf("url")
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "panic button", fontSize = 24.sp)
            radioButtons.forEach {
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = (it == selectedOption), onClick = {
                        println("...on radio button $it")
                        selectedOption = it
                    })
                    Text(text = it)
                }
            }
        }
    }
}
@Composable
fun LanguageSetting(state: UserState, onEvent: (UserEvent) -> Unit){
    var dropDownExpanded by remember{
        mutableStateOf(false)
    }
    var selectedLanguage by remember {
        mutableStateOf("swedish")
    }
    val languages = listOf("swedish", "english")
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Row(){
                Text(text = "language", fontSize = 24.sp)
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = selectedLanguage, fontSize = 20.sp, modifier = Modifier.clickable {
                    dropDownExpanded = true
                })
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "languages"
                )
            }
            DropdownMenu(expanded = dropDownExpanded, onDismissRequest = {
                dropDownExpanded = false;
            }) {
                languages.forEach(){
                    DropdownMenuItem(text = { Text(text = it) }, onClick = {
                        println("on language selected $it")
                        dropDownExpanded = false
                        /*                        onLanguageSelected = {
                                                    println("onLanguage selected")
                                                }*/
                        selectedLanguage = it

                    })
                }
            }
        }
    }
}

@Composable
fun ListSetting(heading: String, list: Array<String>){
    //var urls = listOf("http://curtfurumark.se", "www.google.se")
    Card(modifier = Modifier
        .fillMaxWidth(), shape = RoundedCornerShape(8.dp)
    ) {
        Row() {
            Text(text = heading, fontSize = 24.sp)
            Spacer(Modifier.weight(1f))
            Icon(imageVector = Icons.Default.Add, contentDescription = "add panic url")
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp) ) {
            list.forEach(){
                Text(text = it, modifier = Modifier.padding(start = 16.dp))
            }
        }
    }
}
@Composable
fun DarkModeSetting(state: UserState, onEvent: (UserEvent) -> Unit){
    var isDarkMode by remember {
        mutableStateOf(state.isDarkMode)
    }
    Card(modifier = Modifier
        .fillMaxWidth(), shape = RoundedCornerShape(8.dp)
    ){
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "dark mode")
            Spacer(Modifier.weight(1f))
            Switch(checked = isDarkMode, onCheckedChange = {
                isDarkMode = !isDarkMode
                onEvent(UserEvent.DarkMode(isDarkMode))
            })
        }
    }
}
@Composable
fun ColorsSetting(){
    Card(modifier = Modifier.fillMaxWidth()){
        Text("colorsetting")
    }

}