package se.curtrune.lucy.screens.index.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.app.InitialScreen

@Composable
fun IndexScreen20(onEvent: (InitialScreen)->Unit){
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(
            text = stringResource(R.string.today),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                onEvent(InitialScreen.CALENDER_DATE)
        })
        Text(text = stringResource(R.string.week),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                onEvent(InitialScreen.CALENDER_WEEK)
        })

        Text(
            text = stringResource(R.string.month),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                onEvent(InitialScreen.CALENDER_MONTH)
        })
    }
}

@Composable
@Preview
@PreviewLightDark
fun PreviewIndexScreen(){
    LucyTheme {
        IndexScreen20(onEvent = {})

    }

}