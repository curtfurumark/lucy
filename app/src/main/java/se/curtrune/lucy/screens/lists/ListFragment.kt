package se.curtrune.lucy.screens.lists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.create_list.CreateListState
import se.curtrune.lucy.screens.create_list.composables.CreateListScreen
import se.curtrune.lucy.screens.lists.composables.EditableBulletList
import se.curtrune.lucy.screens.lists.composables.EditableBulletList2
import se.curtrune.lucy.screens.lists.composables.ListScreen


class ListFragment : Fragment() {
    private val viewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        return ComposeView(requireActivity()).apply {
            setContent {
                val state by viewModel.state.collectAsState()
                LaunchedEffect(viewModel) {
                    viewModel.channel.collect { event->
                        when (event) {
                            is ListChannel.ShowMessage -> {
                                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                LucyTheme {
                    ListScreen(state = state, onEvent = viewModel::onEvent)
                }
            }
        }
    }

}