package se.curtrune.lucy.screens.index

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.app.InitialScreen
import se.curtrune.lucy.screens.dev.DevActivity
import se.curtrune.lucy.screens.index.composables.IndexScreen20
import se.curtrune.lucy.screens.main.MainActivity
import se.curtrune.lucy.screens.main.MainActivity2
import se.curtrune.lucy.util.Constants

class IndexActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.index_activity)
        println("IndexActivity.onCreate(Bundle of joy)")
        initContent()
    }
    private fun initContent(){
        println("...initContent")
        val composeView = findViewById<ComposeView>(R.id.indexActivity_composeView)
        composeView!!.setContent {
            LucyTheme {
                IndexScreen20(onEvent = {
                    startActivity(it)
                } )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.index_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.indexActivity_jetpackCompose) {
            startActivity(Intent(this, DevActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startActivity(firstPage: InitialScreen) {
        println("...startActivity(InitialScreen: ${firstPage.name})")
        if(firstPage == InitialScreen.NEW_DAY_CALENDER){
            Intent(this, MainActivity2::class.java).also {
                intent ->
                intent.putExtra(Constants.INITIAL_SCREEN, firstPage.toString())
                startActivity(intent)
            }
        }else {
            val intent = Intent(
                this,
                MainActivity::class.java
            )
            intent.putExtra(Constants.INITIAL_SCREEN, firstPage.toString())
            startActivity(intent)
        }
    }
}