package se.curtrune.lucy.screens.main

import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.app.Lucinda
import se.curtrune.lucy.app.Settings.PanicAction
import se.curtrune.lucy.app.User
import se.curtrune.lucy.classes.Affirmation
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.screens.affirmations.AffirmationWorker
import se.curtrune.lucy.screens.affirmations.AffirmationWorker.RequestAffirmationCallback
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.web.CheckForUpdateThread
import se.curtrune.lucy.web.VersionInfo
import se.curtrune.lucy.workers.InternetWorker
import se.curtrune.lucy.workers.MentalWorker
import java.time.LocalDate

class LucindaViewModel : ViewModel() {
    private val mentalModule = LucindaApplication.mentalModule
    private val  _state =  MutableStateFlow(MainState())
    val state = _state.asStateFlow()
    private val mutableVersionInfo = MutableLiveData<VersionInfo>()
    private val mutableMessage = MutableLiveData<String>()
    //private val mutableEnergy = MutableLiveData<Int>()
    //private val mutableAnxiety = MutableLiveData<Int>()
    //private val mutableStress = MutableLiveData<Int>()
    //private val mutableMood = MutableLiveData<Int>()
    private val mutableAffirmation = MutableLiveData<Affirmation>()
    private val mutableFilter = MutableLiveData<String>()
    private val _mental = MutableLiveData<Mental>()
    var mental: LiveData<Mental> = _mental
    private var currentMental: Mental? = null
    private var updatedAvailableChecked = false
    private var date: LocalDate? = null
/*    val energy: LiveData<Int>
        get() = mutableEnergy
    val anxiety: LiveData<Int>
        get() = mutableAnxiety
    val stress: LiveData<Int>
        get() = mutableStress
    val mood: LiveData<Int>
        get() = mutableMood*/

    init {
        println("MainViewModel.init{}")
        _state.update { it.copy(
            mental =  mentalModule.current.value
        ) }
/*        if (InternetWorker.isConnected(context) && !updatedAvailableChecked) {
            checkIfUpdateAvailable(context)
            updatedAvailableChecked = true
        } else {
            Logger.log("not connected")
        }*/
    }


    fun checkIfUpdateAvailable(context: Context) {
        Logger.log("LucindaViewModel.checkIfUpdateAvailable(Context)")
        val thread = CheckForUpdateThread { versionInfo, res ->
            Logger.log("...onRequestComplete(VersionInfo, boolean)")
            Logger.log(versionInfo)
            val packageInfo = Lucinda.getPackageInfo(context)
            if (packageInfo != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    if (packageInfo.longVersionCode < versionInfo.versionCode) {
                        mutableVersionInfo.value = versionInfo
                    } /*else{
                                mutableMessage.setValue("lucinda is up to date");
                            }*/
                } else {
                    if (packageInfo.versionCode < versionInfo.versionCode) {
                        mutableVersionInfo.setValue(versionInfo)
                    } else {
                        mutableMessage.setValue("lucinda is up to date")
                    }
                }
            }
        }
        thread.start()
    }
    fun onEvent(event: MainEvent){
        when(event){
            is MainEvent.ShowBoost -> {
                println("show boost")
                requestAffirmation()
            }
            is MainEvent.ShowPanic -> {
                println("show panic")
            }
        }
    }

    private fun toggleRecyclerMode() {
        Logger.log("....toggleRecyclerMode()")
        var rm = recyclerMode.value
        rm = if (rm == RecyclerMode.DEFAULT) RecyclerMode.MENTAL_COLOURS else RecyclerMode.DEFAULT
        Logger.log("...new recyclerMode ", rm.toString())
        recyclerMode.value = rm
    }
    fun filter(query: String) {
        Logger.log("LucindaViewModel.filter(query)", query)
        mutableFilter.value = query
    }

    fun getPanicAction(context: Context?): PanicAction {
        Logger.log("LucindaViewModel.getPanicAction()")
        return User.getPanicAction(context)
    }

    val message: LiveData<String>
        get() = mutableMessage

    private fun requestAffirmation() {
        Logger.log("LucindaViewModel.requestAffirmation()")
        AffirmationWorker.requestAffirmation(object : RequestAffirmationCallback {
            override fun onRequest(affirmation: Affirmation) {
                Logger.log("...onRequest(Affirmation)")
                mutableAffirmation.value = affirmation
            }

            override fun onError(message: String) {
                //Toast.makeText( MainActivity.this, message, Toast.LENGTH_LONG).setMentalType();
            }
        })
    }
    val affirmation: LiveData<Affirmation>
        get() = mutableAffirmation
    val filter: LiveData<String>
        get() = mutableFilter

    //private final MutableLiveData<Integer> energy = new MutableLiveData<>();
    enum class RecyclerMode {
        DEFAULT, MENTAL_COLOURS
    }

    @JvmField
    val recyclerMode: MutableLiveData<RecyclerMode?> = MutableLiveData<RecyclerMode?>()
    private val currentFragment = MutableLiveData<Fragment>()

    fun updateFragment(fragment: Fragment) {
        Logger.log("LucindaViewModel.updateFragment(Fragment)")
        currentFragment.value = fragment
    }

    val fragment: LiveData<Fragment>
        get() = currentFragment



    /**
     * whether to show mental colours or not
     * @param recyclerMode, RecyclerMode, default or colours or something
     */
    fun setRecyclerMode(recyclerMode: RecyclerMode?) {
        Logger.log("ViewModel.setRecyclerMode()")
        this.recyclerMode.value = recyclerMode
    }

    fun updateAvailable(): LiveData<VersionInfo> {
        Logger.log("LucindaViewModel.updateAvailable()")
        return mutableVersionInfo
    }
}
