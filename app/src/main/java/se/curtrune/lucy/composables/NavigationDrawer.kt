package se.curtrune.lucy.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.composables.top_app_bar.TopAppBarEvent

@Composable
fun NavigationDrawer(onEvent: (TopAppBarEvent)->Unit){
    Surface(modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier.padding(16.dp)) {
            Text(
                text = "lucinda",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.clickable {
                    onEvent(TopAppBarEvent.DayClicked)
                }
                    .padding(16.dp)
            )
            HorizontalDivider()
            Text(text = "calendars", style = MaterialTheme.typography.titleMedium)
            NavigationDrawerItem(
                label = { Text("day") },
                selected = false,
                onClick = {
                    onEvent(TopAppBarEvent.DayClicked)
                }
            )
            NavigationDrawerItem(
                label = {Text("week")},
                onClick = {onEvent(TopAppBarEvent.WeekClicked)},
                selected = false
            )
            NavigationDrawerItem(
                label = {Text("month")},
                onClick = {onEvent(TopAppBarEvent.MonthClicked)},
                selected = false
            )
            NavigationDrawerItem(
                label = {Text("medicines")},
                onClick = {onEvent(TopAppBarEvent.MedicinesClicked)},
                selected = false
            )
            NavigationDrawerItem(
                label = {Text("settings")},
                onClick = {onEvent(TopAppBarEvent.SettingsClicked)},
                selected = false
            )
        }
    }
}