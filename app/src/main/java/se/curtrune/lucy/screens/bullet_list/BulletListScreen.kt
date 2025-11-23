package se.curtrune.lucy.screens.bullet_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BulletListScreen(){
    val viewModel: BulletListViewModel = viewModel()
    val state by  viewModel.state.collectAsState()
    BulletList(state, viewModel::onEvent)
}