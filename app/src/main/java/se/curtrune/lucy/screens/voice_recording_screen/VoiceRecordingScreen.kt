package se.curtrune.lucy.screens.voice_recording_screen

import android.Manifest
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VoiceRecordingScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var recorder by remember { mutableStateOf<MediaRecorder?>(null) }
    var player by remember { mutableStateOf<MediaPlayer?>(null) }
    var audioFile by remember { mutableStateOf<File?>(null) }
    var isRecording by remember { mutableStateOf(false) }

    val permissionState = rememberPermissionState(
        permission = Manifest.permission.RECORD_AUDIO
    )

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly) {
        Text(text = "voice recording")

        Button(onClick = {
            if (permissionState.status.isGranted) {
                val file = File(context.cacheDir, "audio_record.mp3")
                audioFile = file
                recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    MediaRecorder(context)
                } else {
                    MediaRecorder()
                }.apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    setOutputFile(FileOutputStream(file).fd)
                    prepare()
                    start()
                }
                isRecording = true
            } else {
                permissionState.launchPermissionRequest()
            }
        }, enabled = !isRecording) {
            Text(text = if (permissionState.status.isGranted) "start recording" else "request permission")
        }

        Button(onClick = {
            recorder?.apply {
                stop()
                release()
            }
            recorder = null
            isRecording = false
        }, enabled = isRecording) {
            Text(text = "stop recording")
        }

        Button(onClick = {
            player = MediaPlayer().apply {
                setDataSource(audioFile?.absolutePath)
                prepare()
                start()
            }
        }, enabled = audioFile != null && !isRecording) {
            Text(text = "play recording")
        }

        Button(onClick = {
            // Logic to move file from cache to permanent storage
            audioFile?.let { file ->
                val newFile = File(context.filesDir, file.name)
                file.renameTo(newFile)
                audioFile = newFile
                println("file moved to ${newFile.absolutePath}")
                // Do something with the new file
            }
        }, enabled = audioFile != null) {
            Text(text = "save recording")
        }

        Button(onClick = {
            audioFile?.delete()
            audioFile = null
            player?.release()
            player = null
        }, enabled = audioFile != null) {
            Text(text = "delete recording")
        }
    }
}