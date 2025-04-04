package se.curtrune.lucy.screens.my_day

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.screens.my_day.composables.MentalScreen
import java.time.LocalDate


class MyDayFragment(val date: LocalDate = LocalDate.now()) : Fragment() {
    private val myDayViewModel: MyDayViewModel by viewModels{
        MyDayViewModelFactory(date, this)
    }
    private enum class Mode {
        ESTIMATE, ACTUAL
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.mental_day_fragment, container, false)
        println("MentalDateFragment.onCreateView(...)")
        initContent(view)
        return view
    }

    private fun initContent(view: View){
        println("...initContent()")
        val composeView = view.findViewById<ComposeView>(R.id.mentalDateFragment_composeView)
        composeView!!.setContent {
            val state = myDayViewModel.state.collectAsState()
            LucyTheme {
                MentalScreen(state = state.value, onEvent = { event ->
                    myDayViewModel.onEvent(event)
                })
            }
       }
    }

    companion object {
        var VERBOSE: Boolean = false
    }
}
