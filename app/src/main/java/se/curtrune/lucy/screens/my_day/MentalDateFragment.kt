package se.curtrune.lucy.screens.my_day

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.R
import se.curtrune.lucy.screens.my_day.composables.MentalScreen


class MentalDateFragment : Fragment() {

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
            val mentalDateViewModel = viewModel<MentalDateViewModel>()
            val state = mentalDateViewModel.state.collectAsState()
            MentalScreen(state = state.value, onEvent = { event->
                mentalDateViewModel.onEvent(event)
            })
       }
    }

    companion object {
        var VERBOSE: Boolean = false
    }
}
