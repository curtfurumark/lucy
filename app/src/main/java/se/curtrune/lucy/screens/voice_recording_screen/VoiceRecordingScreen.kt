package se.curtrune.lucy.screens.voice_recording_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VoiceRecordingScreen(modifier: Modifier = Modifier){
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "voice recording")
        Button(onClick = {}){
            Text(text = "start recording")
        }
        Button(onClick = {}) {
            Text(text = "stop recording")
        }
        Button(onClick = {}){
            Text(text = "play recording")
        }
        Button(onClick = {}){
            Text(text = "save recording")
        }
        Button(onClick = {}){
            Text(text = "delete recording")
        }
    }

}