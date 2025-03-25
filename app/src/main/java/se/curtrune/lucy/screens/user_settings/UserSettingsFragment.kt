package se.curtrune.lucy.screens.user_settings

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.screens.daycalendar.DayChannel
import se.curtrune.lucy.screens.user_settings.composables.UserSettings

class UserSettingsFragment: Fragment() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private  var readPermission = false
    private var writePermission = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions->
            readPermission = permissions[android.Manifest.permission.READ_CALENDAR]?:readPermission
            writePermission = permissions[android.Manifest.permission.WRITE_CALENDAR]?: writePermission
            println("writePermissions granted $writePermission")
            println("readPermissions granted: $readPermission")
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                var showMessage by remember {
                    mutableStateOf(false)
                }
                val context = LocalContext.current
                LucyTheme {
                    val userViewModel = viewModel<UserViewModel>()
                    val state = userViewModel.state.collectAsState()
                    LaunchedEffect(userViewModel) {
                        userViewModel.eventFlow.collect{ event->
                            when(event){
                                UserChannel.ReadWriteCalendarPermissions -> {
                                    if( !checkPermissions()){
                                        requestPermissions()
                                        println("after requesting permissions")
                                    }
                                }
                                is UserChannel.ShowMessage -> TODO()
                            }
                        }
                    }
                    UserSettings(state = state.value, onEvent = { event->
                        userViewModel.onEvent(event)
                    })
                }
                if(showMessage){
                    Toast.makeText(context, "message", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun checkPermissions(): Boolean{
        println("checkPermissions()")
        Toast.makeText(context, "check permissions", Toast.LENGTH_SHORT).show()
        readPermission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
        writePermission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED
        return readPermission && writePermission
    }
    private fun requestPermissions(){
        println("requestPermissions")
        val permissionsArray: MutableList<String> = ArrayList()
        if( !readPermission){
            permissionsArray.add(android.Manifest.permission.READ_CALENDAR)
        }
        if(!writePermission){
            permissionsArray.add(android.Manifest.permission.WRITE_CALENDAR)
        }
        permissionLauncher.launch(permissionsArray.toTypedArray())
    }

}