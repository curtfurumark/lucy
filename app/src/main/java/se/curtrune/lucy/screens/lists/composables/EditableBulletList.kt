package se.curtrune.lucy.screens.lists.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme

@Composable
fun EditableBulletList() {
    var items by remember { mutableStateOf(listOf("")) }
    var heading by remember { mutableStateOf("") }
    LazyColumn {
        item{
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = heading,
                onValueChange = {heading = it}
            , label = { Text("name") })
        }
        itemsIndexed(items) { index, text ->
            BulletTextField(
                value = text,
                onValueChange = { newText ->
                    val updatedItems = items.toMutableList()
                    // Check for newline character
                    if (newText.contains("\n")) {
                        // Split at newline, take first as current, rest as new bullets
                        val parts = newText.split("\n", limit = 2)
                        updatedItems[index] = parts[0]
                        updatedItems.add(index + 1, parts.getOrNull(1) ?: "")
                    } else {
                        updatedItems[index] = newText
                    }
                    items = updatedItems
                }
            )
        }
    }
}

@Composable
fun BulletBasicTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp, horizontal = 8.dp)) {

        Text("• ", modifier = Modifier.padding(end = 4.dp), color = Color.Red)

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = LocalTextStyle.current.copy(
                color = Color.White,
                fontSize = 24.sp),
            modifier = Modifier
                .fillMaxWidth(),
            //textColor = Color.Blue // Example: Change TextField text color
        )
    }
}
@Composable
fun BulletTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp, horizontal = 8.dp)) {

        Text("• ", modifier = Modifier.padding(end = 4.dp), color = Color.White)

        TextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = LocalTextStyle.current.copy(
                color = Color.White,
                fontSize = 24.sp),
            modifier = Modifier
                .fillMaxWidth(),
            //textColor = Color.Blue // Example: Change TextField text color
        )
    }
}

@Composable
@PreviewLightDark
fun PreviewEditableBulletList(){
    LucyTheme {
        EditableBulletList()

    }
}