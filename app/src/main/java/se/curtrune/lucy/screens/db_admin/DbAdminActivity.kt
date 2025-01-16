package se.curtrune.lucy.screens.db_admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.dev.ui.theme.LucyTheme
import se.curtrune.lucy.persist.LocalDB

class DbAdminActivity : ComponentActivity() {
    private fun getDbPath():String{
        val file = getDatabasePath(LocalDB.getDbName())
        println("db path ${file.absolutePath}")
        return file.absolutePath
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("DBAdminActivity.onCreate()")
        println(getDbPath())
        enableEdgeToEdge()
        setContent {
            val viewModel = viewModel<DbAdminViewModel>()
            val state = viewModel.state.collectAsState()
            LucyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                        DbAdminScreen(state.value, onEvent = {
                            println("on event")
                        })
                    }
                }
            }
        }
    }
}

