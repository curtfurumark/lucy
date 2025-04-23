package se.curtrune.lucy.screens.create_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.create_list.composables.CreateListScreen


class CreateListFragment(private val parentItem: Item) : Fragment() {
    private val createListViewModel: CreateListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        return ComposeView(requireActivity()).apply {
            createListViewModel.setParent(parentItem)
            setContent {
                val state by createListViewModel.state.collectAsState()
                LucyTheme {
                    CreateListScreen(state = state, onEvent = createListViewModel::onEvent)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(parent: Item) =
            CreateListFragment(parent)
    }
}