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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        return ComposeView(requireActivity()).apply {
            setContent {
                val state = myDayViewModel.state.collectAsState()
                LucyTheme {
                    MentalScreen(state = state.value, onEvent = { event ->
                        myDayViewModel.onEvent(event)
                    })
                }
            }
        }
    }
}
