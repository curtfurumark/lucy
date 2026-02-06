package se.curtrune.lucy.screens.timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.composables.add_item.AddItemBottomSheet
import se.curtrune.lucy.composables.add_item.DefaultItemSettings

@Composable
fun TimeLineScreen(modifier: Modifier = Modifier, navigate: (NavKey)-> Unit){
    val viewModel: TimeLineViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    var showAddItemDialog by remember {
        mutableStateOf(false)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddItemDialog = true
                }
                ){
                    Icon(imageVector = Icons.Default.Add, contentDescription = "add")
                }
        }

    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(it),
        ) {
            items(state.items){
                TimeLineCard(item = it, onClick = {
                    viewModel.onEvent(TimeLineEvent.OnClick(it))
                })
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
    if( showAddItemDialog){
        AddItemBottomSheet(
            defaultItemSettings = DefaultItemSettings(),
            onDismiss = {
                showAddItemDialog = false
            },
            onSave = {
                showAddItemDialog = false
                viewModel.onEvent(TimeLineEvent.InsertItem(it))
            }
        )
    }
    LaunchedEffect(viewModel) {
        viewModel.channel.collect {
            when(it){
                is TimeLineChannel.Navigate -> {
                    navigate(it.navKey)
                }
            }
        }
    }
}