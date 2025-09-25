package se.curtrune.lucy.screens.projects.composables

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.composables.add_item.AddItemBottomSheet
import se.curtrune.lucy.composables.top_app_bar.FlexibleTopBar
import se.curtrune.lucy.composables.top_app_bar.LucindaTopAppBar
import se.curtrune.lucy.modules.TopAppbarModule
import se.curtrune.lucy.modules.TopAppbarModule.topAppBarState
import se.curtrune.lucy.screens.main.navigation.ItemEditorNavKey
import se.curtrune.lucy.screens.projects.ProjectsChannel
import se.curtrune.lucy.screens.projects.ProjectsEvent
import se.curtrune.lucy.screens.projects.ProjectsState
import se.curtrune.lucy.screens.projects.ProjectsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(onNavigate: (NavKey)->Unit){
    val viewModel: ProjectsViewModel = viewModel{
        ProjectsViewModel(LucindaApplication.appModule.repository)
    }
    val state = viewModel.state.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val topAppBarState = TopAppbarModule.topAppBarState.collectAsState()
    var showAddItemBottomSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
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
        }, floatingActionButton = {
            AddItemFab(onAddClick = {
                viewModel.onEvent(ProjectsEvent.ShowAddItemDialog)
            })
        }

    ) {padding->
        ProjectsList(state = state.value, onEvent = viewModel::onEvent, modifier = Modifier.padding(padding))
    }
    LaunchedEffect(viewModel) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is ProjectsChannel.Edit -> {
                    //editItem(event.item)
                    onNavigate(ItemEditorNavKey(event.item))
                    //println("editItem(Item: ${event.item.heading})")
                }

                ProjectsChannel.ShowAddItemDialog -> {
                    showAddItemBottomSheet = true
                }
                is ProjectsChannel.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    if( showAddItemBottomSheet){
        AddItemBottomSheet(
            defaultItemSettings = state.value.defaultItemSettings,
            onDismiss = {
                showAddItemBottomSheet = false
            },
            onSave = {
                viewModel.onEvent(ProjectsEvent.InsertItem(it))
                showAddItemBottomSheet = false
            }
        )
    }
}





