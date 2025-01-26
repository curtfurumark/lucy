package se.curtrune.lucy.composables

import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Mental

@Composable
fun MentalMeter(mental: Mental){
    val pagerState = rememberPagerState(pageCount = {
        4
    })
    var mentalText by remember{
        mutableStateOf("energy")
    }
    HorizontalPager(state = pagerState) { page->
        println("...page: $page")
        mentalText = when(page){
            0 -> "energy: ${mental.energy}"
            1 -> "anxiety: ${mental.anxiety}"
            2-> "mood: ${mental.mood}"
            3 -> "stress: ${mental.stress}"
            else -> {"unknown"}
        }
        Text(text = mentalText, fontSize = 24.sp)
    }
}