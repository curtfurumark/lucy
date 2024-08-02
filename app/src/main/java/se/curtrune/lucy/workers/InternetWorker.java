package se.curtrune.lucy.workers;

import static androidx.core.content.ContextCompat.getSystemService;
import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

public class InternetWorker {
    public static boolean VERBOSE = false;
    public static boolean isConnected(Context context){
        if( VERBOSE) log("InternetWorker.isConnected()");
        ConnectivityManager connectivityManager  = getSystemService(context, ConnectivityManager.class);
        //log( connectivityManager != null ? "...got connectivityManager !": "no connectivity manager");
        if (connectivityManager == null){
            log("...unable to get connectivityManager, assuming not connected");
            return false;
        }
        NetworkInfo networkInfo =  connectivityManager.getActiveNetworkInfo();
        if( networkInfo == null){
            log("...networkInfo == null, assuming not connected");
            return false;
        }
        return connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
