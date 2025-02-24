package se.curtrune.lucy.composables.top_app_bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.dev.ui.theme.LucyTheme


@Composable
fun CategoryDialog(dismiss: ()->Unit){
    Dialog(onDismissRequest = {

    }){
        Card(modifier = Modifier.fillMaxWidth()){
            Column(modifier = Modifier.fillMaxWidth()
                .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ){
                Text(text = "category", fontSize = 24.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Button(onClick = {
                        dismiss()
                    }){
                        Text(text = stringResource(R.string.dismiss))
                    }
                    Button(onClick = {
                        dismiss()
                    }){
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewCategoryDialog(){
    LucyTheme {
        CategoryDialog(dismiss = {})
    }
}