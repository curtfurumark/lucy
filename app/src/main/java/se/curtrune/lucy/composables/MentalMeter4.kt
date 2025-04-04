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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.R
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.my_day.MyDayEvent
import se.curtrune.lucy.screens.my_day.MyDayState
import se.curtrune.lucy.util.DateTImeConverter
import kotlin.math.roundToInt

@Composable
fun MentalMeter4(
    item: Item,
    state: MyDayState,
    onEvent: (MyDayEvent)->Unit) {
    //0 - 10
    var mentalSliderLevel by remember {
        mutableIntStateOf(0)
    }
    var percent by remember {
        mutableFloatStateOf(0F)
    }
/*    var level by remember {
        mutableIntStateOf(0)
    }*/
    var mutableItem by remember {
        mutableStateOf(item)
    }
    mentalSliderLevel = when(state.currentField){
        Field.ENERGY -> item.energy + 5
        Field.ANXIETY -> item.anxiety + 5
        Field.STRESS -> item.stress + 5
        Field.MOOD -> item.mood + 5
        Field.DURATION -> item.duration.toInt()
    }
    val mentalRed = colorResource(R.color.mental_red)
    val mentalGreen = colorResource(R.color.mental_green)

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
    fun setLevel(x: Float) {
        println("...setLevel: $x")
        val fraction = x / boxWidth
        println("fraction: $fraction")
        val level = myRound(fraction) * 10 -5
        println("level: $level")
        when(state.currentField){
            Field.ENERGY -> {
                println("setting energy level: ${level.toInt()}")
                mentalSliderLevel = level.toInt()
                mutableItem.energy = level.toInt()
                item.energy = level.toInt() }
            Field.ANXIETY -> {item.anxiety = level.toInt()}
            Field.STRESS -> {item.stress = level.toInt()}
            Field.MOOD -> {item.mood = level.toInt()}
            Field.DURATION -> TODO()
        }
        mentalSliderLevel = level.toInt() -5
        onEvent(MyDayEvent.UpdateItem(item))
    }
    Box(
        modifier = Modifier.fillMaxWidth()
            //.clickable { mentalSliderLevel++ }
            .pointerInput(true){
                detectTapGestures {
                    println("tap $it.x")
                    tapX = it.x
                    percent = tapX / size.width
                    val tmpLevel = myRound(percent) * 10
                    println("level: $tmpLevel")
                    setLevel(it.x)
                    //onLevelChange((myRound(percent) * 10).toInt())
                    //onLevelChange(xToLevel(tapX))
                }
            }
            .pointerInput(true){
                detectDragGestures { change, dragAmount ->
                    println("change x ${change.position.x}")
                    tapX = change.position.x
                    change.consume()
                    //change.scrollDelta
                    percent = tapX / size.width
                    println("dragAmount: x  ${dragAmount.x}")
                    setLevel(tapX)
                    //onLevelChange(xToLevel(tapX))
                }
            }
        .drawBehind {
            boxWidth = size.width
            boxHeight = size.height
            drawRect(color = mentalGreen)
            println("mental slider level: $mentalSliderLevel")
            drawRect( color = mentalRed, size = Size((size.width * myRound(mentalSliderLevel * 0.1f)), size.height ))
        }){
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "${item.heading} ${DateTImeConverter.format(item.targetTime)}")
            Text(text = "${state.currentField} ${mentalSliderLevel - 5}")
/*            Text(text = "${state.currentField.name } ${
               when(state.currentField){
                    Field.ENERGY ->mutableItem.energy
                    Field.ANXIETY -> mutableItem.anxiety
                    Field.STRESS -> mutableItem.stress
                    Field.MOOD -> mutableItem.mood
                    Field.DURATION -> item.duration.toString()
                }
            }")*/
            //}", fontSize = 20.sp)
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
@Composable
@Preview
fun PreviewMentalMeter() {
    val item = Item().also {
        it.energy = -5
        it.anxiety = 5
        it.stress = 5
        it.mood = -3
        it.heading ="dev"
    }
    val myDayState = MyDayState().also {
        it.currentField = Field.MOOD
    }
    MentalMeter4(item = item, state = myDayState, onEvent = {})
}

