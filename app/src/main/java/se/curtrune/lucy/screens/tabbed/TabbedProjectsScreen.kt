package se.curtrune.lucy.screens.tabbed

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.composables.top_app_bar.FlexibleTopBar
import se.curtrune.lucy.composables.top_app_bar.LucindaTopAppBar
import se.curtrune.lucy.modules.TopAppbarModule
import se.curtrune.lucy.screens.lists.editable.EditableBulletList
import se.curtrune.lucy.screens.notes.NoteScreen
import se.curtrune.lucy.screens.templates.templates.TemplatesScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabbedProjectsScreen(navigate: (NavKey)->Unit){
    var currentIndex by remember { mutableIntStateOf(0) }
    val titles = listOf("note", "lista", "mallar")
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val topAppBarState = TopAppbarModule.topAppBarState.collectAsState()
    Scaffold(
        topBar = {
            FlexibleTopBar(
                scrollBehavior = scrollBehavior,
                content = {
                    LucindaTopAppBar(
                        state = topAppBarState.value,
                        onEvent = { appBarEvent ->
                            println("appBarEvent $appBarEvent")
                        })
                }, onEvent = { event ->
                    println("onEvent $event")
                    //devViewModel.onEvent(event)
                }
            )
        }

    ) {padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            PrimaryTabRow(selectedTabIndex = currentIndex) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = currentIndex == index,
                        onClick = { currentIndex = index },
                        text = {
                            Text(
                                text = title,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
            when (currentIndex) {
                0 -> NoteScreen()
                1 -> EditableBulletList()
                2 -> TemplatesScreen(navigate = {
                    println("onEdit $it")
                    navigate(it)
                })
                //3-> CreateTemplateScreen()
/*                3 -> FreeFormatTab()
                4 -> ProjectsTab{
                    println("navigate $it")
                }*/
            }
        }
    }
}

@Composable
fun FreeFormatTab(){
    Column(
        modifier = Modifier.fillMaxSize()
    , horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Text(text = "free format")
    }
}
@Composable
fun EmptyTab(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Text(text = "emptiness")
    }
}
data class BulletItem(
    var text:String,
    var index:Int = 0
)

@Composable
fun UnOrderedBulletItemComposable(modifier: Modifier = Modifier, item: BulletItem, onEdit: (BulletItem)->Unit){
    var text by remember { mutableStateOf(item.text) }
    val focusRequester = remember { FocusRequester() }
    TextField(
        modifier = modifier.fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged {}
            .focusable(),
        value = "${item.index} $text",
        singleLine = false, // Allow multi-line for enter key
        onValueChange = {
            if( it.endsWith("\n")){
                //it.removeSuffix()
                println("ends with new line")
                onEdit(item.copy(text = it))
            }else{
                text = it
            }

        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Circle, contentDescription = "add")
        }
    )
}

@Composable
@Preview
fun UnorderedBulletListPreview(){
    UnOrderedBulletItemComposable(item = BulletItem("item 1"), onEdit = {})
}


@Composable
fun ProjectsList(){
    Column(modifier = Modifier.fillMaxSize()){
        Text(text = "projects")
    }
}