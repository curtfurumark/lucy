package se.curtrune.lucy.screens.duration.composables

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.composables.top_app_bar.FlexibleTopBar
import se.curtrune.lucy.composables.top_app_bar.LucindaTopAppBar
import se.curtrune.lucy.modules.TopAppbarModule
import se.curtrune.lucy.screens.dev.composables.DurationControls
import se.curtrune.lucy.screens.duration.DurationEvent
import se.curtrune.lucy.screens.duration.DurationState
import se.curtrune.lucy.screens.duration.DurationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DurationScreen( onEvent: (DurationEvent)->Unit){
    val viewModel: DurationViewModel = viewModel()
    val state =   viewModel.state.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val topAppBarState = TopAppbarModule.topAppBarState.collectAsState()
    Scaffold(
        topBar = {
            FlexibleTopBar(
                scrollBehavior = scrollBehavior,
                content = {
                    LucindaTopAppBar(
                        state = topAppBarState.value,
                        onEvent = { appBarEvent ->
                            println("appBarEvent $appBarEvent")
                            //mainViewModel.onEvent(appBarEvent)
                        })
                }, onEvent = { event ->
                    println("onEvent $event")
                    //devViewModel.onEvent(event)
                }
            )
        }
    ) {padding->
        Column(modifier = Modifier.fillMaxWidth().padding(padding)) {
            DurationControls(state = state.value, onEvent = {
                viewModel.onEvent(it)
            })
            Spacer(modifier = Modifier.height(8.dp))
            DurationsStatisticsList(state = state.value)
        }
    }
}

@Composable
@Preview
fun PreviewDurationScreen(){
    DurationScreen {

    }

}