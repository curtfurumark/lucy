package se.curtrune.lucy.screens.settings.screens.panic

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.PanicOption

@Composable
fun PanicOptionCard(option: PanicOption){
    Card(modifier = Modifier.fillMaxWidth()){
        Text(text = "current option")
        Spacer(modifier = Modifier.height(1.dp))
        Text(text = option.name)
    }

}