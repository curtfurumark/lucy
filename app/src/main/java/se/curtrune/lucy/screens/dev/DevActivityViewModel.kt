package se.curtrune.lucy.screens.dev

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.modules.SystemInfoModule
import se.curtrune.lucy.persist.LocalDB
import se.curtrune.lucy.screens.dev.test_cases.LocalDBTest
import se.curtrune.lucy.util.Logger
import java.time.LocalDateTime
import java.time.ZoneOffset

class DevActivityViewModel : ViewModel() {
    private var _state = MutableStateFlow(DevState())
    val state = _state.asStateFlow()

    private var lucindaInfoList: MutableList<SystemInfo>? = null

/*    private fun createLucindaInfo(key: String, value: String): SystemInfo {
        val lucindaInfo = SystemInfo()
        lucindaInfo.key = key
        lucindaInfo.value = value
        return lucindaInfo
    }

    private fun createLucindaInfo(key: String, value: Int): SystemInfo {
        val lucindaInfo = SystemInfo()
        lucindaInfo.key = key
        lucindaInfo.setValue(value)
        return lucindaInfo
    }*/

/*    val lucindaInfo: List<SystemInfo>?
        get() = lucindaInfoList*/

    init{
        println("....initBlock")
        _state.value.systemInfoList = LucindaApplication.systemInfoModule.systemInfo
        println("...number of sys infos: ${_state.value.systemInfoList.size}")
    }
/*    fun init(context: Context) {
        _state.value.systemInfoList = SystemInfoModule.sysInfo
    }*/
    private fun getSystemInfoList(): List<SystemInfo>{
        println("getSystemInfoList()")
/*        lucindaInfoList = ArrayList()
        (lucindaInfoList as ArrayList<SystemInfo>).add(createLucindaInfo("SDK_INT", Build.VERSION.SDK_INT))
        (lucindaInfoList as ArrayList<SystemInfo>).add(createLucindaInfo("DEVICE", Build.DEVICE))
        (lucindaInfoList as ArrayList<SystemInfo>).add(createLucindaInfo("USER", Build.USER))
        (lucindaInfoList as ArrayList<SystemInfo>).add(createLucindaInfo("HARDWARE", Build.HARDWARE))
        (lucindaInfoList as ArrayList<SystemInfo>).add(createLucindaInfo("MANUFACTURER", Build.MANUFACTURER))
        (lucindaInfoList as ArrayList<SystemInfo>).add(createLucindaInfo("MODEL", Build.MODEL))
        //lucindaInfoList.add(createLucindaInfo("BASE_OS",Build.VERSION.BASE_OS));
        (lucindaInfoList as ArrayList<SystemInfo>).add(createLucindaInfo("LOCAL_DB_VERSION", LocalDB.getDbVersion()))
        Logger.log("\tSDK_INT", Build.VERSION.SDK_INT)
        Logger.log("\tDEVICE", Build.DEVICE)
        Logger.log("\tUSER", Build.USER)
        Logger.log("\tHARDWARE", Build.HARDWARE)
        Logger.log("\tBRAND", Build.BRAND)
        Logger.log("\tMANUFACTURER", Build.MANUFACTURER)
        Logger.log("\tMODEL", Build.MODEL)*/
/*        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            //String version = pInfo.versionName;
            Logger.log("...versionName", pInfo.versionName)
            (lucindaInfoList as ArrayList<SystemInfo>).add(createLucindaInfo("versionName", pInfo.versionName))

            val installDateTime =
                LocalDateTime.ofEpochSecond(pInfo.firstInstallTime / 1000, 0, ZoneOffset.UTC)
            (lucindaInfoList as ArrayList<SystemInfo>).add(createLucindaInfo("first installed", installDateTime.toString()))
            val updateDateTime =
                LocalDateTime.ofEpochSecond(pInfo.lastUpdateTime / 1000, 0, ZoneOffset.UTC)
            (lucindaInfoList as ArrayList<SystemInfo>).add(createLucindaInfo("last updated", updateDateTime.toString()))
            (lucindaInfoList as ArrayList<SystemInfo>).add(createLucindaInfo("versionCode", pInfo.versionCode))
            Logger.log("...packageName", pInfo.packageName)
            (lucindaInfoList as ArrayList<SystemInfo>).add(createLucindaInfo("packageName", pInfo.packageName))
            val applicationInfo = pInfo.applicationInfo
            Logger.log("...dataDir", applicationInfo.dataDir)
            (lucindaInfoList as ArrayList<SystemInfo>).add(createLucindaInfo("dataDir", applicationInfo.dataDir))
        } catch (e: PackageManager.NameNotFoundException) {
            Logger.log("NameNotFoundException", e.message)
            e.printStackTrace()
        } catch (e: Exception) {
            Logger.log("Exception e", e.message)
            e.printStackTrace()
        }*/
        return lucindaInfoList as ArrayList<SystemInfo>
    }

    fun listColumns(context: Context?) {
        Logger.log("DevActivity.listColumns(Context)")
        LocalDB(context).use { db ->
            db.getColumns("items")
        }
    }
    fun onEvent(event: DevEvent){
        println("...onEvent(${event.toString()})")
        when(event){
            DevEvent.CreateItemTree -> LocalDBTest().createTreeToDelete()
        }

    }
}
