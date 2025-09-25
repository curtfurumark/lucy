package se.curtrune.lucy.screens.settings.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.composables.top_app_bar.FlexibleTopBar
import se.curtrune.lucy.composables.top_app_bar.LucindaTopAppBar
import se.curtrune.lucy.modules.TopAppbarModule
import se.curtrune.lucy.screens.settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen(){
    val viewModel: SettingsViewModel = viewModel()
    val state by viewModel.state.collectAsState()
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
                            //viewModel.onEvent(appBarEvent)
                        }
                    )
                },
                onEvent = {
                    println("onEvent $it")
                }
            )
        }

    ) {padding->
        UserSettings(modifier = Modifier.padding(padding),state = state, onEvent = viewModel::onEvent)
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@PreviewLightDark
fun PreviewSettingsScreen(){
    LucyTheme {
        SettingsScreen()
    }
}