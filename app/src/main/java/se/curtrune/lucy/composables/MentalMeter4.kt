package se.curtrune.lucy.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.R
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.util.DateTImeFormatter
import kotlin.math.roundToInt

@Composable
fun MentalMeter4(
    item: Item,
    field: Field,
    onLevelChange: (Int)->Unit) {
    var mentalLevel by remember {
        mutableIntStateOf(0)
    }
    mentalLevel = when(field){
        Field.ENERGY -> item.energy + 5
        Field.ANXIETY -> item.anxiety + 5
        Field.STRESS -> item.stress + 5
        Field.MOOD -> item.mood + 5
        Field.DURATION -> item.duration.toInt()
    }
    val mentalRed = colorResource(R.color.mental_red)
    val mentalGreen = colorResource(R.color.mental_green)
    var percent by remember {
        mutableFloatStateOf((mentalLevel * 0.1).toFloat())
    }
    var boxHeight by remember {
        mutableFloatStateOf(0f)
    }
    var boxWidth by remember {
        mutableFloatStateOf(0f)
    }
    var tapX by remember {
        mutableFloatStateOf(0f)
    }
    fun xToLevel (x: Float): Int{
        val decimal = x / boxWidth
        val l = (decimal * 10).roundToInt()
        println("xToLevel $l")
        return l -5
    }
    Box(
        modifier = Modifier.fillMaxWidth()
            .clickable { mentalLevel++ }
            .pointerInput(true){
                detectTapGestures {
                    println("tap $it.x")
                    tapX = it.x
                    percent = tapX / size.width
                    val tmpLevel = myRound(percent) * 10
                    println("level: $tmpLevel")
                    //onLevelChange((myRound(percent) * 10).toInt())
                    onLevelChange(xToLevel(tapX))
                }
            }
            .pointerInput(true){
                detectDragGestures { change, dragAmount ->
                    println("change x ${change.position.x}")
                    tapX = change.position.x
                    percent = tapX / size.width
                    println("dragAmount $dragAmount")
                    onLevelChange(xToLevel(tapX))
                }
            }
        .drawBehind {
            boxWidth = size.width
            boxHeight = size.height
            drawRect(color = mentalGreen)
            drawRect( color = mentalRed, size = Size((size.width * myRound(percent)), size.height ))
        }){
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "${item.heading} ${DateTImeFormatter.format(item.targetTime)}")
            Text(text = "${field.name } ${
                when(field){
                    Field.ENERGY ->item.energy
                    Field.ANXIETY -> item.anxiety
                    Field.STRESS -> item.stress
                    Field.MOOD -> item.mood
                    Field.DURATION -> item.duration.toString()
                }
            }", fontSize = 20.sp)
            //Text(text = "height: $boxHeight")
            //Text(text = "width: $boxWidth")
            //Text(text = "tap x: $tapX")
            //Text(text = "percent: $percent")
            //Text(text = "percent rounded: ${myRound(percent)}")
        }
        }
    }

fun myRound(f:  Float): Float{
    return (f * 10).roundToInt() * 0.1f
}