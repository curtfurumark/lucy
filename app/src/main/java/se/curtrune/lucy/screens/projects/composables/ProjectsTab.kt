package se.curtrune.lucy.screens.projects.composables

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.composables.add_item.AddItemBottomSheet
import se.curtrune.lucy.composables.top_app_bar.FlexibleTopBar
import se.curtrune.lucy.composables.top_app_bar.LucindaTopAppBar
import se.curtrune.lucy.modules.TopAppbarModule
import se.curtrune.lucy.screens.navigation.ItemEditorNavKey
import se.curtrune.lucy.screens.projects.ProjectsChannel
import se.curtrune.lucy.screens.projects.ProjectsEvent
import se.curtrune.lucy.screens.projects.ProjectsViewModel


/**
 * same as ProjectsScreen but without top app bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsTab(onNavigate: (NavKey)->Unit){
    val viewModel: ProjectsViewModel = viewModel{
        ProjectsViewModel(LucindaApplication.appModule.repository)
    }
    val state = viewModel.state.collectAsState()
    var showAddItemBottomSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Scaffold(
        floatingActionButton = {
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





