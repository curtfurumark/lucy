package se.curtrune.lucy.screens.duration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.R
import se.curtrune.lucy.screens.duration.composables.DurationScreen
import se.curtrune.lucy.util.Logger

class DurationFragment : Fragment() {
    init {
        println("DurationFragment().init")
        // Required empty public constructor
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Logger.log("DurationFragment.onCreateView(...)")
        val view = inflater.inflate(R.layout.duration_fragment, container, false)
        initContent(view )
        return view
    }

    private fun initContent(view: View){
        println("...initContent()")
        val composeView = view.findViewById<ComposeView>(R.id.durationFragment_composeView)
        composeView!!.setContent {
            MaterialTheme {
                val durationViewModel = viewModel<DurationViewModel>()
                val state = durationViewModel.state//.collectAsState()
                val context = LocalContext.current
                DurationScreen(state = state.value, onEvent = { event ->
                    durationViewModel.onEvent(event)
                })
                if (state.value.showProgressBar){
                    println("show progress bar, please")
                    Toast.makeText(context, "PROGRESSBAR", Toast.LENGTH_SHORT).show()
                }
                if( state.value.message.isNotBlank()){
                    Toast.makeText(context, state.value.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

/*    override fun onItemClick(item: Listable) {
        Logger.log("...onItemClick(Listable)")
        when (item) {
            is DateListable -> {
                adapter!!.setList(item.listableItems)
            }

            is CategoryListable -> {
                adapter!!.setList(item.listableItems)
            }

            is Item -> {
                Logger.log("....you clicked an item")
                Toast.makeText(context, "you clicked an item", Toast.LENGTH_LONG).show()
            }
        }
    */}

