package se.curtrune.lucy.viewmodel;

import static se.curtrune.lucy.util.Logger.log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import se.curtrune.lucy.web.CheckForUpdateThread;
import se.curtrune.lucy.web.VersionInfo;

public class UpdateLucindaViewModel extends ViewModel {
    private MutableLiveData<VersionInfo> mutableVersionInfo = new MutableLiveData<>();
    public void checkForNewVersion(){
        log("...checkForNewVersion()");
        CheckForUpdateThread thread = new CheckForUpdateThread(new CheckForUpdateThread.Callback() {
            @Override
            public void onRequestComplete(VersionInfo versionInfo, boolean res) {
                mutableVersionInfo.setValue(versionInfo);
            }
        });
        thread.start();
    }

    public LiveData<VersionInfo> getVersionInfo() {
        return mutableVersionInfo;
    }
}
