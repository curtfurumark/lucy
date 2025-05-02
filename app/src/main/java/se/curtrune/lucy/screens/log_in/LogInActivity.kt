package se.curtrune.lucy.screens.log_in

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import se.curtrune.lucy.app.InitialScreen
import se.curtrune.lucy.screens.log_in.ui.theme.LucyTheme
import se.curtrune.lucy.screens.main.MainActivity
import se.curtrune.lucy.util.Constants
import se.curtrune.lucy.util.Logger.Companion.log

class LogInActivity : ComponentActivity() {
    private val logInViewModel: LogInViewModel by viewModels()
    init {
        println("LogInActivity.init")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val state =  logInViewModel.state.collectAsState()
            LaunchedEffect(logInViewModel) {
                logInViewModel.eventChannel.collect(){ event->
                    when(event){
                        is LogInChannel.navigate -> {
                            startUserActivity(event.initialScreen)
                        }

                        is LogInChannel.showMessage -> {
                            Toast.makeText(applicationContext,event.message, Toast.LENGTH_LONG ).show()
                        }
                    }
                }
            }
            LucyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LogInScreen(state = state.value, modifier = Modifier.padding(innerPadding), onEvent = {event->
                        logInViewModel.onEvent(event)
                    })
                }
            }
        }
    }
    private fun startUserActivity(initialScreen: InitialScreen) {
        log("...startUserActivity()")
        val intent=Intent(applicationContext,MainActivity::class.java)
        when(initialScreen){
            InitialScreen.TODO_FRAGMENT-> {intent.putExtra(Constants.INITIAL_SCREEN, initialScreen.name)}
            InitialScreen.CALENDER_DATE -> {intent.putExtra(Constants.INITIAL_SCREEN,initialScreen.name)}
            InitialScreen.CALENDER_WEEK -> {intent.putExtra(Constants.INITIAL_SCREEN,initialScreen.name)}
            InitialScreen.CALENDER_MONTH -> {intent.putExtra(Constants.INITIAL_SCREEN,initialScreen.name)}
            InitialScreen.CALENDER_APPOINTMENTS ->{intent.putExtra(Constants.INITIAL_SCREEN,initialScreen.name)}
        }
        startActivity(intent)
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LucyTheme {
        LogInScreen(state = LogInState()) {  }
    }
}