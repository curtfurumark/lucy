package se.curtrune.lucy.screens.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.composables.DialogSettings
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.composables.add_item.AddItemBottomSheet
import se.curtrune.lucy.composables.add_item.DefaultItemSettings
import se.curtrune.lucy.composables.top_app_bar.TopAppBarEvent
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.screens.dev.DevEvent
import se.curtrune.lucy.screens.item_editor.ItemEditorFragment
import se.curtrune.lucy.screens.main.MainViewModel
import se.curtrune.lucy.screens.projects.composables.ProjectsScreen
import se.curtrune.lucy.util.Logger

class ProjectsFragment : Fragment(){
    private val projectsViewModel : ProjectsViewModel by viewModels{ProjectsViewModel.Factory(LucindaApplication.appModule.repository)}
    private lateinit var mainViewModel: MainViewModel

    init {
        if (VERBOSE) println("ProjectsFragment()")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setContent {
                LucyTheme {
                    val state by  projectsViewModel.state.collectAsState()
                    val filter = mainViewModel.filter.collectAsState()
                    LaunchedEffect(filter.value) {
                        projectsViewModel.onEvent(TopAppBarEvent.OnSearch(filter.value, false))
                    }
                    var showAddItemBottomSheet by  remember { mutableStateOf(false) }
                    LaunchedEffect(projectsViewModel) {
                        projectsViewModel.eventFlow.collect { event ->
                            when (event) {
                                is ProjectsChannel.Edit -> {
                                    editItem(event.item)
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
                    Scaffold(
                        floatingActionButton = {
                            AddItemFab {
                                projectsViewModel.onEvent(ProjectsEvent.ShowAddItemDialog)
                            }
                        }
                    ) { padding ->
                        ProjectsScreen(state = state, onEvent = { event ->
                            projectsViewModel.onEvent(event)
                        }, modifier = Modifier.padding(padding))
                    }
                    if (showAddItemBottomSheet) {
                        AddItemBottomSheet(
                            defaultItemSettings = state.defaultItemSettings,
                            onDismiss = {
                                println("...onDismiss")
                                showAddItemBottomSheet = false
                            }, onSave = { item ->
                                println("...onSave: ${item.heading}")
                                projectsViewModel.onEvent(ProjectsEvent.InsertItem(item))
                                showAddItemBottomSheet = false
                            }
                        )
                    }
                }
            }
        }
    }

    private fun editItem(item: Item) {
        println("...editItem(Item: ${item.heading})")
        mainViewModel.updateFragment(ItemEditorFragment(item))
    }

    private fun showDeleteDialog(item: Item) {
        checkNotNull(item)

    }
    companion object {
        var VERBOSE: Boolean = false
    }
}