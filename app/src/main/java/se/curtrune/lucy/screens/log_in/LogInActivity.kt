package se.curtrune.lucy.screens.log_in

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import se.curtrune.lucy.screens.log_in.ui.theme.LucyTheme

class LogInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LucyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LogInScreen(modifier = Modifier.padding(innerPadding)) {
                        println("Logged in")
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LucyTheme {
        LogInScreen {  }
    }
}