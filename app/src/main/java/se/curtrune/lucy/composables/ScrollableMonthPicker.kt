package se.curtrune.lucy.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import java.time.Year
import java.time.YearMonth


@Composable
fun ScrollableMonthPicker(onYearMonth:(YearMonth)->Unit){
    val yearMonthList = getList()
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(yearMonthList){
            when(it){
                is YearMonth -> Month(it, onYearMonth = onYearMonth)
                is Year -> Year(it)
            }
        }
    }
}
@Composable
fun Month(yearMonth: YearMonth, onYearMonth: (YearMonth) -> Unit){
    Box(
        modifier = Modifier.padding(2.dp)
            .clickable {
                onYearMonth(yearMonth)
            }){
        Text(text = yearMonth.month.name)
    }
}
@Composable
fun Year(year: Year){
    Box(modifier = Modifier.padding(2.dp)){
        Text(text = year.toString())
    }
}
fun getList(): List<Any>{
    var yearMonthCurrent = YearMonth.now().minusYears(2)
    val yearMonthLast = YearMonth.now().plusYears(2)
    val yearMonthList = mutableListOf<Any>()
    while(yearMonthCurrent.isBefore(yearMonthLast)){
        yearMonthList.add(yearMonthCurrent)
        yearMonthCurrent = yearMonthCurrent.plusMonths(1)
        if( yearMonthCurrent.month == java.time.Month.JANUARY){
            yearMonthList.add(yearMonthCurrent.year)
        }
    }
    return yearMonthList
}
@Composable
@Preview(showBackground = true)
fun PreviewMonthList(){
    LucyTheme {
        ScrollableMonthPicker(onYearMonth = {

        })
    }

}