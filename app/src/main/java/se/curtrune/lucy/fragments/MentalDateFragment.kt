package se.curtrune.lucy.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Switch
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.composables.EditItemCard
import se.curtrune.lucy.activities.kotlin.composables.Field
import se.curtrune.lucy.activities.kotlin.composables.ItemFieldChooser
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.composables.AllDaySwitch
import se.curtrune.lucy.composables.EditItemsList
import se.curtrune.lucy.composables.MyDatePicker
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.viewmodel.LucindaViewModel
import se.curtrune.lucy.viewmodel.MentalDateViewModel
import java.time.LocalDate


class MentalDateFragment : Fragment {

    private var lucindaViewModel: LucindaViewModel? = null

    private var currentDate: LocalDate

    private var composeView: ComposeView? = null


    private enum class Mode {
        ESTIMATE, ACTUAL
    }

    private var currentMode = Mode.ESTIMATE
    //private var mentalDateViewModel: MentalDateViewModel? = null

    constructor() {
        currentDate = LocalDate.now()
        currentMode = Mode.ESTIMATE
    }

    constructor(date: LocalDate, actual: Boolean) {
        currentDate = date
        currentMode = if (actual) Mode.ACTUAL else Mode.ESTIMATE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.mental_day_fragment, container, false)
        Logger.log("MentalDateFragment.onCreateView(...)")
        initDefaults()
        initComponents(view)
        initViewModel()
        initItemList(requireContext())
        return view
    }


    private fun initComponents(view: View) {
        if (VERBOSE) Logger.log("...initComponents(View)")
        composeView = view.findViewById(R.id.mentalDateFragment_composeView)
    }

    private fun initDefaults() {
        Logger.log("...initDefaults()")
        currentDate = LocalDate.now()
    }
    private fun initItemList(context: Context){
        println("...initItemList()")
        composeView!!.setContent {
            val mentalDateViewModel = viewModel<MentalDateViewModel>(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return MentalDateViewModel(context) as T
                    }
                }
            )
            val liveData = mentalDateViewModel.items
            val data = liveData.value
            var currentField by remember{
                mutableStateOf(Field.ENERGY)
            }
            var items by remember{
                mutableStateOf(mentalDateViewModel.mutableItems.value)
            }
            mentalDateViewModel.mutableItems.observe(viewLifecycleOwner){
                println("changed observed")
                //items = it
            }
            var isAllDay by remember {
                mutableStateOf(mentalDateViewModel.mutableAllDay.value)
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                MyDatePicker(onDate = {

                })
                AllDaySwitch(allDay = isAllDay!!, onEstimate = {
                    println("all day  $it")
                    mentalDateViewModel.setAllDay(it)
                })
                ItemFieldChooser(onFieldChosen = {
                    println("field chosen, ${it.name}")
                    currentField = it
                })
                //EditItemsList() { }
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    println("lazy column")
                    items(items!!.size) {
                        EditItemCard(items!![it], onItemEdit = { item->
                            println("item edited, ${item.heading}")
                            mentalDateViewModel!!.updateItem(item )
                        }, itemField = currentField)
                    }
                }
            }
        }
    }

    private fun initViewModel() {
        if (VERBOSE) println("...initViewModel()")
        lucindaViewModel = ViewModelProvider(requireActivity())[LucindaViewModel::class.java]
    }

    private fun showDateDialog() {
        if (VERBOSE) Logger.log("...showDateDialog()")
        val datePickerDialog = DatePickerDialog(requireContext())

        datePickerDialog.setOnDateSetListener { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
            currentDate = LocalDate.of(year, month + 1, dayOfMonth)
            Logger.log("...onDateSet()", currentDate.toString())
            //mentalDateViewModel!!.setDate(currentDate, context)
        }
        datePickerDialog.show()
    }






    companion object {
        var VERBOSE: Boolean = false
    }
}
