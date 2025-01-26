package se.curtrune.lucy.screens.mental.composables

import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import se.curtrune.lucy.screens.mental.MentalEvent

/**
 * "all day" is index 0, current is index 1
 * find a better solution
 */
@Composable
fun SelectAllDayOrCurrent(isAllDay: Boolean, onEvent: (MentalEvent)->Unit){
    var selectedIndex by remember {
        mutableIntStateOf(if (isAllDay) 0 else 1)
    }
    val options = listOf("all day", "current")
    SingleChoiceSegmentedButtonRow{
        options.forEachIndexed{index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = {
                    selectedIndex = index
                    onEvent(MentalEvent.AllDay(selectedIndex == 0))
                          },
                selected = selectedIndex == index
            ){
                Text(label)
            }
        }
    }
}
enum class Period{
    ALL_DAY, CURRENT
}

@Composable
@Preview
fun Preview(){
    SelectAllDayOrCurrent(true, onEvent = {})
}