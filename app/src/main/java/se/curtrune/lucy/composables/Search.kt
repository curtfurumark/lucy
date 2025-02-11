package se.curtrune.lucy.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun Search(onSearch: (String, Boolean)->Unit){
    var searchEverywhere by remember {
        mutableStateOf(false)
    }
    var query by remember{
        mutableStateOf("")
    }
    var textFieldVisible by remember {
        mutableStateOf(false)
    }
    Row( modifier = Modifier.fillMaxWidth()
    , horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically){
        Icon(
            modifier = Modifier.clickable {
                textFieldVisible = !textFieldVisible
            },
            imageVector = Icons.Default.Search,
            contentDescription = "search for item"
        )
        AnimatedVisibility(visible = textFieldVisible) {
            Checkbox(checked = false, onCheckedChange = {
                searchEverywhere = !searchEverywhere
            })
            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    onSearch(query, searchEverywhere)
                },
                modifier = Modifier
                    .weight(1f),
                label = { Text("search") }
            )
        }


    }
}
@Composable
@Preview(showBackground = true)
fun SearchPreview(){
    Search(onSearch = { filter, everywhere->

    })
}