package se.curtrune.lucy.screens.lists.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun EditableBulletList2() {
    var items by remember {
        mutableStateOf(listOf(TextFieldValue("")))
    }
    LazyColumn {
        itemsIndexed(items) { index, textFieldValue ->
            BulletTextField2(
                value = textFieldValue,
                onValueChange = { newValue ->
                    val updatedItems = items.toMutableList()

                    if (newValue.text.contains("\n")) {
                        val parts = newValue.text.split("\n", limit = 2)
                        val currentText = parts[0]
                        val newBulletText = parts.getOrNull(1) ?: ""

                        updatedItems[index] = TextFieldValue(currentText)

                        // Add new bullet with cursor at the beginning
                        updatedItems.add(
                            index + 1,
                            TextFieldValue(
                                text = newBulletText,
                                selection = TextRange(0)
                            )
                        )
                    } else {
                        updatedItems[index] = newValue
                    }

                    items = updatedItems
                }
            )
        }
    }
}

@Composable
fun BulletTextField2(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Text("• ", modifier = Modifier.padding(end = 4.dp), color = Color.Red)

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth()
        )
    }
}