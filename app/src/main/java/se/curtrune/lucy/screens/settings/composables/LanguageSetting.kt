package se.curtrune.lucy.screens.settings.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.screens.settings.UserEvent
import se.curtrune.lucy.screens.settings.UserState

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
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically  )
            {
                Text(text = stringResource(R.string.language), fontSize =MaterialTheme.typography.titleMedium.fontSize)
                Spacer(Modifier.weight(1f))
                Text(text = selectedLanguage, fontSize = 20.sp, modifier = Modifier.clickable {
                    dropDownExpanded = true
                })
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "languages"
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.weight(1f))
                DropdownMenu(expanded = dropDownExpanded, onDismissRequest = {
                    dropDownExpanded = false;
                }) {
                    languages.forEach() {
                        DropdownMenuItem(text = { Text(text = it) }, onClick = {
                            //println("on language selected $it")
                            dropDownExpanded = false
                            selectedLanguage = it
                            onEvent(UserEvent.Language(it))

                        })
                    }
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
fun PreviewLanguageSetting(){
    LucyTheme {
        LanguageSetting(state = UserState(), onEvent = {})
    }
}