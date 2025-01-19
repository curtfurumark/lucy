package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import se.curtrune.lucy.screens.dev.DevState
import se.curtrune.lucy.screens.dev.SystemInfo

@Composable
fun SystemInfoList(state: DevState){
    LazyColumn {
        items(state.systemInfoList){ systemInfo->
            SystemInfo(systemInfo)
        }
    }
}

@Composable
fun SystemInfo(info: SystemInfo){
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = info.key)
        Text(text = info.value)
    }
}