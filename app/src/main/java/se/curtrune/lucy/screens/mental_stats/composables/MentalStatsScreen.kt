package se.curtrune.lucy.screens.mental_stats.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.composables.top_app_bar.FlexibleTopBar
import se.curtrune.lucy.composables.top_app_bar.LucindaTopAppBar
import se.curtrune.lucy.modules.TopAppbarModule.topAppBarState
import se.curtrune.lucy.screens.mental_stats.MentalStatsEvent
import se.curtrune.lucy.screens.mental_stats.MentalStatsState
import se.curtrune.lucy.screens.mental_stats.MentalStatsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentalStatsScreen(){
    val viewModel: MentalStatsViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            FlexibleTopBar(
                scrollBehavior = scrollBehavior,
                content = {
                    LucindaTopAppBar(
                        state = topAppBarState.collectAsState().value,
                        onEvent = { appBarEvent ->
                            println("appBarEvent $appBarEvent")
                            //viewModel.onEvent(appBarEvent)
                        }
                    )
                },
                onEvent = {
                    println("onEvent $it")
                }
            )
        }
    ) {padding ->
        Column(modifier = Modifier.fillMaxWidth().padding(padding)) {
            Text(text = "mental stats screen")
            MentalControls(state = state, onEvent = viewModel::onEvent)
            MentalStatsViewer(state = state)
        }
    }
}