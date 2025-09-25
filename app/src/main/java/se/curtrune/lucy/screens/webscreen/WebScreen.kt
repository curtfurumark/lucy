package se.curtrune.lucy.screens.webscreen

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebScreen(modifier: Modifier = Modifier, url: String = "https://en.wikipedia.org/wiki/Henri_Matisse") {
    Column() {
        Text(text = url)
        AndroidView(
            modifier = modifier,
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = WebViewClient()
                    loadUrl(url)
                }
            }, update = {
                it.loadUrl(url)
            })
    }
}