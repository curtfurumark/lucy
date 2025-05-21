package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.persist.DBAdmin

@Composable
fun BackupDataBase(onEventCopy: ()->Unit){
    val context = LucindaApplication.contextModule.application
    Card(modifier = Modifier.fillMaxWidth()) {
        Column() {
            Text(text = "copy db to external", fontSize = 24.sp)
            Button(onClick = {
                DBAdmin.backupDataBase(context)
            }){
                Text("back up data")
            }
        }
    }
}