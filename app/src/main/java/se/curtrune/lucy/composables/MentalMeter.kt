package se.curtrune.lucy.composables

import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.screens.main.MainState

@Composable
fun MentalMeter(state: MainState){
    println("MentalMeter recomposition")
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        val pagerState = rememberPagerState(pageCount = {
            4
        })
        var mentalText by remember {
            mutableStateOf("energy")
        }
        Text(text = "boost")
        HorizontalPager(state = pagerState) { page ->
            println("...page: $page")
            mentalText = when (page) {
                0 -> "energy: ${state.mental.energy}"
                1 -> "anxiety: ${state.mental.anxiety}"
                2 -> "mood: ${state.mental.mood}"
                3 -> "stress: ${state.mental.stress}"
                else -> {
                    "unknown"
                }
            }
            Text(text = mentalText, fontSize = 24.sp, color = Color.White)
        }
        Text(text = "panic")
    }
}
@Composable
fun MentalMeter(mental: Mental){
    println("MentalMeter recomposition")
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        val pagerState = rememberPagerState(pageCount = {
            4
        })
        var mentalText by remember {
            mutableStateOf("energy")
        }
        HorizontalPager(state = pagerState,
            modifier = Modifier.width(150.dp)
        ) { page ->
            println("...page: $page")
            mentalText = when (page) {
                0 -> "energy: ${mental.energy}"
                1 -> "anxiety: ${mental.anxiety}"
                2 -> "mood: ${mental.mood}"
                3 -> "stress: ${mental.stress}"
                else -> {
                    "unknown"
                }
            }
            Text(text = mentalText, fontSize = 24.sp, color = Color.White)
        }
        //Text(text = "panic")
    }
}