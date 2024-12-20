package se.curtrune.lucy.activities.kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.viewmodel.UserSettingsViewModel

class UserSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LucyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserSettings()
                }
            }
        }
    }
}

@Composable
fun UserSettings(){
    val viewModel = viewModel<UserSettingsViewModel>()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)){
        DarkModeSetting(heading = "dark mode", viewModel.isDarkMode(LocalContext.current))
        Spacer(modifier = Modifier.height(8.dp))
        LanguageSetting(viewModel.getLanguage(LocalContext.current))
        Spacer(modifier = Modifier.height(8.dp))

        ListSetting(heading = "panic urls", viewModel.getPanicUrls(LocalContext.current))
        Spacer(modifier = Modifier.height(8.dp))
        PanicButton()
    }
}
@Composable
fun PanicButton(){
    val radioButtons = listOf("url", "ice", "snake")
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "panic button", fontSize = 24.sp)
            radioButtons.forEach {
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = false, onClick = { /*TODO*/ })
                    Text(text = it)
                }
            }
        }
    }
}
@Composable
fun LanguageSetting(language: String){
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
                    DropdownMenuItem(text = { Text(text = it)}, onClick = {
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
        .fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
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
fun DarkModeSetting(heading: String, isDarkMode: Boolean){
    Card(modifier = Modifier
        .fillMaxWidth(), shape = RoundedCornerShape(8.dp)){
        Text(text = heading, fontSize = 24.sp)
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = heading)
            Spacer(Modifier.weight(1f))
            Switch(checked = isDarkMode, onCheckedChange = {
                println("is dark mode $it")
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview5() {
    LucyTheme {
        //SwitchSetting(heading = "dark mode")
        PanicButton()
    }
}