package se.curtrune.lucy.screens.appointments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.adapters.AppointmentAdapter
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.dialogs.EventDialog
import se.curtrune.lucy.fragments.ItemSessionFragment
import se.curtrune.lucy.screens.appointments.composables.AppointmentsScreen
import se.curtrune.lucy.screens.main.LucindaViewModel
import se.curtrune.lucy.util.Logger

class AppointmentsFragment : Fragment() {
    private var recycler: RecyclerView? = null
    private var adapter: AppointmentAdapter? = null
    private var buttonAdd: FloatingActionButton? = null
    private var mainViewModel: LucindaViewModel? = null
    private var appointmentsViewModel: AppointmentsViewModel? = null
    private var itemTouchHelper: ItemTouchHelper? = null

    init {
        if (VERBOSE) Logger.log("AppointmentsFragment()")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Logger.log("AppointmentsFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)")
        val view = inflater.inflate(R.layout.appintments_fragment, container, false)
        //initViewModel()
        //initComponents(view)
        requireActivity().title = getString(R.string.appointments)
        //initSwipe()
        //initRecycler()
        //initListeners()
        initContent(view )
        return view
    }

    private fun addAppointment() {
        if (VERBOSE) Logger.log("...addAppointment()")
        val dialog = EventDialog()
        dialog.setCallback { item: Item? ->
            Logger.log("...onNewAppointment(Item item")
        }
        dialog.show(parentFragmentManager, "add appointment")
    }

    private fun initContent(view: View ){
        val composeView = view.findViewById<ComposeView>(R.id.appointmentsFragment_composeView)
        composeView.setContent {
            val appointmentsViewModel = viewModel<AppointmentsViewModel>()
            val state = appointmentsViewModel.state.collectAsState()
            LucyTheme {
                Text(text = "hello appointments")
                AppointmentsScreen(state = state.value, onEvent = { event->
                    appointmentsViewModel.onEvent(event)

                })
                if(state.value.editAppointment){
                    println("editAppointment: true")
                    state.value.currentAppointment?.let { editAppointment(it) }
                }
            }
        }
    }

    private fun initListeners() {
        if (VERBOSE) Logger.log("...initListeners()")
/*        buttonAdd!!.setOnClickListener { view: View? -> addAppointment() }
        mainViewModel!!.filter.observe(requireActivity()) { filter: String? ->
            Logger.log("...onFilter(String)", filter)
            appointmentsViewModel!!.filter(filter)
        }
        appointmentsViewModel!!.events.observe(
            requireActivity()
        ) { items: List<Item?>? ->
            Logger.log("...getEvents(List<Item>)")
            adapter!!.setList(items)
        }*/
    }

/*    private fun initRecycler() {
        if (VERBOSE) Logger.log("...initRecycler()")
        adapter = AppointmentAdapter(appointmentsViewModel!!.events.value, this)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recycler!!.layoutManager = layoutManager
        recycler!!.itemAnimator = DefaultItemAnimator()
        recycler!!.adapter = adapter
        itemTouchHelper!!.attachToRecyclerView(recycler)
    }*/

    private fun initSwipe() {
/*        Logger.log("...initSwipe()")
        itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Logger.log("...onSwiped(...)")
                val item = appointmentsViewModel!!.getItem(viewHolder.adapterPosition)
                if (direction == ItemTouchHelper.LEFT) {
                    showDeleteDialog(item)
                } else if (direction == ItemTouchHelper.RIGHT) {
                    showPostponeDialog(item)
                }
            }
        })*/
    }





    fun editAppointment(item: Item) {
        //if (VERBOSE) Logger.log("...onItemClick(Item)", item.heading)
        mainViewModel!!.updateFragment(ItemSessionFragment(item))
    }


/*
    override fun onCheckboxClicked(item: Item, checked: Boolean) {
        Logger.log("...onCheckBoxClicked(Item, boolean)", checked)
        item.state =
            if (checked) State.DONE else State.TODO
        appointmentsViewModel!!.update(item, context)
    }*/

/*    private fun showDeleteDialog(item: Item) {
        Logger.log("...showDeleteDialog(Item)", item.heading)
        val builder = AlertDialog.Builder(context)
        builder.setTitle("delete" + item.heading)
        builder.setMessage("are you sure? ")
        builder.setPositiveButton("delete") { dialog: DialogInterface?, which: Int ->
            Logger.log("...on positive button click")
            appointmentsViewModel!!.delete(item, context)
        }
        builder.setNegativeButton("cancel") { dialog: DialogInterface?, which: Int ->
            Logger.log("...on negative button click")
            adapter!!.notifyDataSetChanged()
        }
        val dialog = builder.create()
        dialog.show()
    }*/

    private fun showPostponeDialog(item: Item) {
        Logger.log("showPostponeDialog(Item)")
/*        val dialog = PostponeDialog()
        dialog.setCallback(object : PostponeDialog.Callback {
            override fun postpone(postpone: Postpone) {
                Logger.log("PostponeDialog.postpone(Postpone)", postpone.toString())
                appointmentsViewModel!!.postpone(item, postpone, context)
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