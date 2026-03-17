package se.curtrune.lucy.screens.item_editor.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark

@Composable
fun RepeatCard(){
    Card(modifier = Modifier.fillMaxWidth()){
        Text(text = "repeat")
    }
}


@Composable
@PreviewLightDark
fun PreviewRepeatCard(){
    RepeatCard()
}