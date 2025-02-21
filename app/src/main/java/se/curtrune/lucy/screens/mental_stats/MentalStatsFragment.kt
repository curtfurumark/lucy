package se.curtrune.lucy.screens.mental_stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.screens.mental_stats.composables.MentalStatsScreen


/**
 * A simple [Fragment] subclass.
 * Use the [MentalStatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MentalStatsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return ComposeView(requireActivity()).apply {
            setContent {
                val mentalViewModel = viewModel<MentalStatsViewModel>()
                //val
                LucyTheme {
                    MentalStatsScreen(state = MentalStatsState(), onEvent = { event->
                        mentalViewModel.onEvent(event)
                    })
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MentalStats.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MentalStatsFragment()
    }
}