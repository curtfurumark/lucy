package se.curtrune.lucy.screens.todo

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.adapters.ItemAdapter
import se.curtrune.lucy.app.Settings
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.dialogs.AddItemDialog
import se.curtrune.lucy.persist.ItemsWorker
import se.curtrune.lucy.screens.appointments.UIEvent
import se.curtrune.lucy.screens.item_editor.ItemEditorFragment
import se.curtrune.lucy.screens.main.MainViewModel
import se.curtrune.lucy.screens.todo.composables.TodoScreen
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.workers.NotificationsWorker

class TodoFragment : Fragment() {
    private var buttonAdd: FloatingActionButton? = null
    private var recycler: RecyclerView? = null
    private var adapter: ItemAdapter? = null
    //private val currentParent: Item? = null
    private var mainViewModel: MainViewModel? = null
    private var todoFragmentViewModel: TodoViewModel? = null
    private var itemTouchHelper: ItemTouchHelper? = null

    init {
        if (VERBOSE) Logger.log("ToDoFragment()")
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Logger.log("ToDoFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)")
        val view = inflater.inflate(R.layout.todo_fragment, container, false)
        requireActivity().title = "todo"
        initContent(view )
        return view
    }

    private fun addItemDialog() {
        if (VERBOSE) Logger.log("...addItemDialog()")
        val dialog = AddItemDialog(
            ItemsWorker.getRootItem(
                Settings.Root.TODO,
                context
            ), false
        )
        dialog.setCallback { item: Item ->
            Logger.log("...onAddItem(Item item)")
            //log(item);
            todoFragmentViewModel!!.insert(item)
            if (item.hasNotification()) {
                Logger.log("...item has notification, will set notification")
                NotificationsWorker.setNotification(item, context)
            }
        }
        dialog.show(childFragmentManager, "add item")
    }


    private fun initContent(view: View){
        val composeView = view.findViewById<ComposeView>(R.id.todoFragment_composeView)
        composeView.setContent {
            val todoViewModel = viewModel<TodoViewModel>()
            val state = todoViewModel.state.collectAsState()
            var showProgressBar by remember{
                 mutableStateOf(false)
            }
            LaunchedEffect(todoViewModel) {
                todoViewModel.eventFlow.collect{ event->
                    when(event){
                        is ChannelEvent.Edit -> {
                            navigateToEditor(event.item)
                        }
                        is ChannelEvent.ShowMessage ->{
                            println(event.message)
                            Toast.makeText(requireContext(), event.message, Toast.LENGTH_LONG).show()
                        }

                        is ChannelEvent.ShowProgressBar -> {
                            showProgressBar = event.show
                            println("...showProgress bar: ${event.show}")
                        }
                    }
                }
            }
            LucyTheme {
                TodoScreen(state = state.value, onEvent = { event->
                    todoViewModel.onEvent(event)
                })
                if(showProgressBar){
                    CircularProgressIndicator()
                }
            }
        }
    }

    private fun initListeners() {
        if (VERBOSE) Logger.log("...initListeners()")

    }

    private fun navigateToEditor(item: Item) {
        println("...navigateToEditor(${item.heading})")
        val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.updateFragment(
            ItemEditorFragment(
                item
            )
        )
    }

    private fun showDeleteDialog(item: Item) {
        Logger.log("...showDeleteDialog(Item)", item.heading)
        val builder = AlertDialog.Builder(context)
        builder.setTitle("delete" + item.heading)
        builder.setMessage("are you sure? ")
        builder.setPositiveButton("delete") { dialog: DialogInterface?, which: Int ->
            Logger.log("...on positive button click")
            //todoFragmentViewModel!!.delete(item)
        }
        builder.setNegativeButton("cancel") { dialog: DialogInterface?, which: Int ->
            Logger.log("...on negative button click")
            //adapter!!.notifyDataSetChanged()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showPostponeDialog(item: Item) {
        Logger.log("showPostponeDialog(Item)")
/*        val dialog = PostponeDialog()
        dialog.setCallback(object : PostponeDialog.Callback {
            override fun postpone(postpone: Postpone) {
                Logger.log("PostponeDialog.postpone(Postpone)", postpone.toString())
                todoFragmentViewModel!!.postpone(item, postpone, context)
            }

            override fun dismiss() {
                Logger.log("...dismiss()")
                adapter!!.notifyDataSetChanged()
            }
        })
        dialog.show(childFragmentManager, "postpone")*/
    }

    companion object {
        var VERBOSE: Boolean = false
    }
}