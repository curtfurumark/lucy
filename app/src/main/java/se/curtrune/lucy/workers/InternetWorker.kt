package se.curtrune.lucy.workers

import android.app.Application
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat
import se.curtrune.lucy.util.Logger

class InternetWorker(val application: Application){
    var VERBOSE: Boolean = false
    fun isConnected(): Boolean {
        if (VERBOSE) Logger.log("InternetWorker.isConnected()")
        val connectivityManager = ContextCompat.getSystemService(
            application.applicationContext,
            ConnectivityManager::class.java
        )
        if (connectivityManager == null) {
            println("...unable to get connectivityManager, assuming not connected")
            return false
        }
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo == null) {
            println("...networkInfo == null, assuming not connected")
            return false
        }
        return connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting
    }
}
