package se.curtrune.lucy.screens.projects.composables

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.composables.top_app_bar.FlexibleTopBar
import se.curtrune.lucy.composables.top_app_bar.LucindaTopAppBar
import se.curtrune.lucy.modules.TopAppbarModule
import se.curtrune.lucy.screens.lists.composables.EditableBulletList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabbedProjectsScreen(){
    var state by remember { mutableIntStateOf(0) }
    val titles = listOf("empty", "lista",  "fri")
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
            PrimaryTabRow(selectedTabIndex = state) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = state == index,
                        onClick = { state = index },
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
            when (state) {
                0 -> EmptyTab()
                1 -> EditableBulletList()
                2 -> FreeFormatTab()
                3 -> ProjectsTab{
                    println("navigate $it")
                }
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
fun BulletList(){
    var heading by remember { mutableStateOf("heading") }
    var currentFocus by remember {
        mutableIntStateOf(0) }
    val items = remember { mutableStateListOf(BulletItem("", index = 0), BulletItem("", index = 1)) }
    val focusRequester = remember { FocusRequester() }
    Column(modifier = Modifier
        .fillMaxSize()
        .imePadding()) {
        Text(text = "bullet list with ${items.size} items")
        Text(text = "current focus $currentFocus")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            //items.add(BulletItem(""))
            //println("onClick change focus")
            currentFocus = ++currentFocus % items.size
            println("current focus $currentFocus")
        }){
            Text(text = "change focus")
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = heading,
            onValueChange = { heading = it }
        )
        items.forEachIndexed { index, item ->
            UnOrderedBulletItemComposable(
                //var color by remember { mutableStateOf(Color.Transparent) }
                modifier = Modifier
                    .padding(start = 8.dp),
                item = item,
                onEdit = {
                    println("onEdit $it")
                    if(it.text.endsWith("\n")){
                        println("add new item after ${it.index}")
                        items[index] = item.copy(text = item.text.removeSuffix("\n"))
                        items.add(BulletItem("", index = index+1 ))
                        // Request focus for the new item if it's the last one
                        // This requires a bit more complex logic to associate FocusRequesters with items
                    }
                }
            )
        }
    }
}

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