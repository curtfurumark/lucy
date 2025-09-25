package se.curtrune.lucy.activities.share

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import se.curtrune.lucy.activities.share.composables.ImageViewer
import se.curtrune.lucy.activities.share.composables.ShareScreen
import se.curtrune.lucy.activities.share.ui.theme.LucyTheme
import se.curtrune.lucy.composables.top_app_bar.FlexibleTopBar
import se.curtrune.lucy.composables.top_app_bar.LucindaTopAppBar
import se.curtrune.lucy.modules.TopAppbarModule.topAppBarState

class ShareActivity : ComponentActivity() {
    private var sharedText: String? = null
    private var imageUri: Uri? = null
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("ShareActivity.onCreate()")
        if(intent.action == Intent.ACTION_SEND ){
            when(intent.type){
                "text/plain" -> {
                    println("text/plain")
                    sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
                    println("sharedText: $sharedText")
                }
                "image/*" -> {
                    println("image/*")
                    imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM)
                    println("imageUri: $imageUri")
                }
            }

        }
        if( intent?.action == Intent.ACTION_SEND && intent.type == "text/plain"   ){
            sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            println("sharedText: $sharedText")
        }
        //enableEdgeToEdge()
        setContent {
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            LucyTheme {
                Scaffold(
                    topBar = {
                        FlexibleTopBar(
                            scrollBehavior = scrollBehavior,
                            content = {
                                LucindaTopAppBar(
                                    state = topAppBarState.collectAsState().value,
                                    onEvent = { appBarEvent ->
                                        println("appBarEvent $appBarEvent")
                                    }
                                )
                            },
                            onEvent = {
                                println("onEvent $it")
                            }
                        )
                        //
                    },
                    modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShareScreen(modifier = Modifier.padding(innerPadding), sharedText = sharedText)
                    //ImageViewer(uri = imageUri!!, modifier = Modifier.padding(innerPadding), contentDescription = "image")
                }
            }
        }
    }
    private fun handleSendImage(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            // Update UI to reflect image being shared
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}