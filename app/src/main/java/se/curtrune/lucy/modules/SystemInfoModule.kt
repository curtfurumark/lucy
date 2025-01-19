package se.curtrune.lucy.modules

import android.content.Context
import android.os.Build
import se.curtrune.lucy.persist.LocalDB
import se.curtrune.lucy.screens.dev.SystemInfo

class SystemInfoModule(val context: Context) {
    companion object{
        var sysInfo: MutableList<SystemInfo> = mutableListOf()
    }
    init {
        initSysInfo()
    }
    private fun initSysInfo() {
        sysInfo.add(SystemInfo("SDK_INT", Build.VERSION.SDK_INT.toString()))
        sysInfo.add(SystemInfo("DEVICE", Build.DEVICE))
        sysInfo.add(SystemInfo("USER", Build.USER))
        sysInfo.add(SystemInfo("HARDWARE", Build.HARDWARE))
        sysInfo.add(SystemInfo("MODEL", Build.MODEL))
        sysInfo.add(SystemInfo("MANUFACTURER", Build.MANUFACTURER))
        sysInfo.add(SystemInfo("LOCAL_DB_VERSION", LocalDB.getDbVersion().toString()))
        sysInfo.add(SystemInfo("DISPLAY", Build.DISPLAY))
        sysInfo.add(SystemInfo("BRAND", Build.BRAND))
        sysInfo.add(SystemInfo("PRODUCT", Build.PRODUCT))
        sysInfo.add(SystemInfo("BOARD", Build.BOARD))

        //lucindaInfoList.add(createLucindaInfo("BASE_OS",Build.VERSION.BASE_OS));


    }
}