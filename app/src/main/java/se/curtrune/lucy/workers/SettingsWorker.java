package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import android.app.UiModeManager;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

public class SettingsWorker {
    public static void toggleDarkMode(Context context){
        log("...toggleDarkMode()");
        if(context == null){
            log("...called with Context == null");
            return;
        }
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        //uiModeManager.getCurrentModeType();
        int currentMode = uiModeManager.getNightMode();
        log("...currentMode");
        switch (currentMode) {
            case UiModeManager.MODE_NIGHT_NO:
                log("MODE_NIGHT_NO");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case UiModeManager.MODE_NIGHT_YES:
                log("MODE_NIGHT_YES");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case UiModeManager.MODE_NIGHT_AUTO:
                log("MODE_NIGHT_AUTO");
                break;
            case UiModeManager.MODE_NIGHT_CUSTOM:
                log("MODE_NIGHT_CUSTOM");
                break;

        }
    }
    public static void setDarkMode(){
        log("SettingsWorker.setDarkMode()");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        //UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);

    }
}
