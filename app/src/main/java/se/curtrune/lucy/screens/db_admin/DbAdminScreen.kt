package se.curtrune.lucy.screens.db_admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun DbAdminScreen(state: DbAdminState, onEvent: (DbAdminEvent)->Unit){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly)
    {
        Text(text = "this is database admin page", fontSize = 24.sp)
        Text(text = "db name: ${state.dbName}", fontSize = 24.sp)
    }
}

@Composable
fun DbInfo(){
    Card(){
    }
}