package se.curtrune.lucy.screens.index20

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.screens.economy.EconomyActivity
import se.curtrune.lucy.screens.item_editor.ItemEditorActivity
import se.curtrune.lucy.screens.repeat.RepeatActivity
import se.curtrune.lucy.screens.db_admin.DbAdminActivity
import se.curtrune.lucy.screens.medicine.MedicineFragment
import se.curtrune.lucy.screens.week_calendar.WeekFragment
import se.curtrune.lucy.screens.dev.DevActivity

@Composable
fun IndexScreen20(){
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "week calendar", fontSize = 24.sp, modifier = Modifier.clickable {
            context.startActivity(Intent(context, WeekFragment::class.java))
        })
        Text(text = "repeats", fontSize = 24.sp, modifier = Modifier.clickable {
            context.startActivity(Intent(context, RepeatActivity::class.java))

        })
        Text(text = "medicin", fontSize = 24.sp, modifier = Modifier.clickable {
            context.startActivity(Intent(context, MedicineFragment::class.java))

        })

        Text(text = "item editor", fontSize = 24.sp, modifier = Modifier.clickable {
            context.startActivity(Intent(context, ItemEditorActivity::class.java))
        })
        Text(text = "db admin", fontSize = 24.sp, modifier = Modifier.clickable {
            context.startActivity(Intent(context, DbAdminActivity::class.java))
        })
        Text(text = "dev stuff", fontSize = 24.sp, modifier = Modifier.clickable {
            context.startActivity(Intent(context, DevActivity::class.java))
        })
        Text(text = "economy", fontSize = 24.sp, modifier = Modifier.clickable {
            context.startActivity(Intent(context, EconomyActivity::class.java))
        })
    }
}