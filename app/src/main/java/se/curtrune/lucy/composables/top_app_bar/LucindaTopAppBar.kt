package se.curtrune.lucy.composables.top_app_bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.composables.MentalMeter
//import se.curtrune.lucy.screens.common.MentalMeter
import se.curtrune.lucy.screens.main.MainState
import se.curtrune.lucy.screens.main.TopAppBarState
import se.curtrune.lucy.screens.medicine.composable.DropdownItem

@Composable
fun LucindaTopAppBar(
    state: TopAppBarState,
    onEvent: (TopAppBarEvent)->Unit){
    val topAppBarModule = LucindaApplication.appModule
    Column(modifier = Modifier.fillMaxWidth()){
        var showSearchField by remember{
            mutableStateOf(false)
        }
        var searchFilter by remember {
            mutableStateOf("")
        }
        var searchEverywhere by remember {
            mutableStateOf(true)
        }
        var  showActionMenu by remember {
            mutableStateOf(false)
        }
        Row(modifier = Modifier.fillMaxWidth()
            .padding(4.dp)){
            Icon(imageVector = Icons.Default.Menu, contentDescription = "main menu",
                modifier = Modifier.clickable {
                    onEvent(TopAppBarEvent.DrawerMenu)
                })
            if(state.showMental) {
                Spacer(modifier = Modifier.width(8.dp))
                MentalMeter(mental = state.mental)
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.Search, contentDescription = "search",
                modifier = Modifier.clickable {
                    showSearchField = !showSearchField
                    searchFilter = ""
                    onEvent(TopAppBarEvent.OnSearch(searchFilter, searchEverywhere))

                })
            Spacer(modifier = Modifier.width(8.dp))
            Icon(imageVector = Icons.Default.DateRange, contentDescription = "day calendar",
                modifier =  Modifier.clickable {
                    onEvent(TopAppBarEvent.DayCalendar)

                })
            Spacer(modifier = Modifier.width(8.dp))
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "action menu",
                modifier = Modifier.clickable {
                    //onEvent(TopAppBarEvent.ActionMenu)
                    showActionMenu = !showActionMenu
                }
            )
            DropdownMenu(expanded = showActionMenu, onDismissRequest = {
                showActionMenu = false
            }){
                DropdownItem("dev activity") {
                    showActionMenu = false
                    onEvent(TopAppBarEvent.DevActivity)
                }
                DropdownItem("check for update") {
                    showActionMenu = false
                    onEvent(TopAppBarEvent.CheckForUpdate)
                }
            }
        }
        AnimatedVisibility(visible = showSearchField) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Checkbox(
                    checked = searchEverywhere,
                    onCheckedChange = {searchEverywhere = !searchEverywhere}
                    //colors = CheckboxColors()
                )
                TextField(
                    value = searchFilter,
                    onValueChange = {
                        searchFilter = it
                        onEvent(TopAppBarEvent.OnSearch(searchFilter, searchEverywhere))},
                    label = { Text(stringResource(R.string.search)) },
                    modifier = Modifier.height(56.dp)
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth()){
            LucindaControls(state = state, onEvent = onEvent)
        }
    }
}

@Composable
@Preview(showBackground = true)
@PreviewLightDark
fun PreviewTopAppBar(){
    LucyTheme {
        val state = TopAppBarState()
        state.title = "preview"
        LucindaTopAppBar(state = state, onEvent = {})

        //LucindaTopAppBar(title = "dev", onEvent = {})
    }
}