package se.curtrune.lucy.screens.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.item_editor.ItemEditorFragment
import se.curtrune.lucy.screens.main.MainViewModel
import se.curtrune.lucy.screens.projects.composables.ProjectsScreen
import se.curtrune.lucy.util.Logger

class ProjectsFragment : Fragment(){
    //private var recycler: RecyclerView? = null

    //private var buttonAddItem: FloatingActionButton? = null
    //private var adapter: ItemAdapter? = null
    //private var projectsViewModel: ProjectsViewModel? = null
    //private var currentParent: Item? = null
    //private var items: List<Item> = ArrayList()
    //private var items: MutableList<Item> = ArrayList()
    //private var mainViewModel: MainViewModel? = null

    init {
        if (VERBOSE) Logger.log("ProjectsFragment()")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.log("ProjectsFragment.onCreate(Bundle of joy))")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setContent {
                LucyTheme {
                    val projectsViewModel = viewModel<ProjectsViewModel>()
                    val state = projectsViewModel.state.collectAsState()
                    LaunchedEffect(projectsViewModel) {
                        projectsViewModel.eventFlow.collect{ event->
                            when(event){
                                is ProjectsChannel.Edit -> {
                                    editItem(event.item)
                                }
                            }
                        }
                    }
                    ProjectsScreen(state = state.value, onEvent = {event->
                        projectsViewModel.onEvent(event)
                    })
                }
            }
        }
    }

    private fun editItem(item: Item) {
        println("...editItem(Item: ${item.heading})")
        val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.updateFragment(ItemEditorFragment(item))
    }


    private fun showAddItemDialog() {
        if (VERBOSE) Logger.log("...showAddItemDialog()")
/*        val dialog = AddItemDialog(currentParent, false)
        dialog.setCallback { item: Item? ->
            var item = item
            Logger.log("...AddItemDialog.onAddItem(Item)", item!!.heading)
            item = repository.insert(item)
            if (VERBOSE) Logger.log(item)
            items.add(item)
            items.sort(Comparator.comparingLong { obj: Item -> obj.compare() })
            adapter!!.notifyDataSetChanged()
        }
        dialog.show(childFragmentManager, "add item")*/
    }



    private fun showDeleteDialog(item: Item) {
        checkNotNull(item)
/*        Logger.log("...showDeleteDialog(Item)", item.heading)
        val builder = AlertDialog.Builder(context)
        val stringDialogTitle =
            String.format(Locale.getDefault(), "%s %s?", getString(R.string.delete), item.heading)
        builder.setTitle(stringDialogTitle)
        builder.setMessage(R.string.are_you_sure)
        builder.setPositiveButton(getString(R.string.delete)) { dialog: DialogInterface?, which: Int ->
            Logger.log("\t\ton positive button click")
            deleteItem(item)
        }
        builder.setNegativeButton(getString(R.string.dismiss)) { dialog: DialogInterface?, which: Int ->
            Logger.log("\t\ton negative button click")
            adapter!!.notifyDataSetChanged()
        }
        val dialog = builder.create()
        dialog.show()*/
    }



    private fun startSequence() {
/*        Logger.log("...startSequence() currentParent", currentParent!!.heading)
        if (currentParent == null) {
            Logger.log("WARNING, current parent is null, surrender")
            Toast.makeText(context, "current parent is null", Toast.LENGTH_LONG).show()
            return
        }
        mainViewModel!!.updateFragment(SequenceFragment(currentParent))*/
    }

    companion object {
        var VERBOSE: Boolean = false
    }
}