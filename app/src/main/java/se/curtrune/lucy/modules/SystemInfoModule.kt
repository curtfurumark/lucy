package se.curtrune.lucy.modules

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import se.curtrune.lucy.persist.SqliteLocalDB
import se.curtrune.lucy.screens.dev.SystemInfo
import java.time.LocalDateTime
import java.time.ZoneOffset

class SystemInfoModule(val context: Application) {
    var systemInfo =  mutableListOf<SystemInfo>()
    init {
        println("init block of SystemInfoModule")
        initSysInfo()
    }
    private fun initSysInfo() {
        println("...initSysInfo")
        systemInfo.add(SystemInfo("SDK_INT", Build.VERSION.SDK_INT.toString()))
        systemInfo.add(SystemInfo("DEVICE", Build.DEVICE))
        systemInfo.add(SystemInfo("USER", Build.USER))
        systemInfo.add(SystemInfo("HARDWARE", Build.HARDWARE))
        systemInfo.add(SystemInfo("MODEL", Build.MODEL))
        systemInfo.add(SystemInfo("MANUFACTURER", Build.MANUFACTURER))
        systemInfo.add(SystemInfo("LOCAL_DB_VERSION", SqliteLocalDB.dbVersion.toString()))
        systemInfo.add(SystemInfo("DISPLAY", Build.DISPLAY))
        systemInfo.add(SystemInfo("BRAND", Build.BRAND))
        systemInfo.add(SystemInfo("PRODUCT", Build.PRODUCT))
        systemInfo.add(SystemInfo("BOARD", Build.BOARD))
        //lucindaInfoList.add(createLucindaInfo("BASE_OS",Build.VERSION.BASE_OS));
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            systemInfo.add(SystemInfo("versionName", pInfo.versionName))
            val installDateTime = LocalDateTime.ofEpochSecond(pInfo.firstInstallTime / 1000, 0, ZoneOffset.UTC)
            systemInfo.add(SystemInfo("installed", installDateTime.toString()))
            val updateDateTime = LocalDateTime.ofEpochSecond(pInfo.lastUpdateTime / 1000, 0, ZoneOffset.UTC)
            systemInfo.add(SystemInfo("last update", updateDateTime.toString()))
            systemInfo.add(SystemInfo("data directory", pInfo.applicationInfo?.dataDir ?: "no data dir, wtf"))
            systemInfo.add(SystemInfo("versionName", pInfo.versionName))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                systemInfo.add(SystemInfo("versionCode", pInfo.longVersionCode.toString()))
            }else{
                systemInfo.add(SystemInfo("versionCode", pInfo.versionCode.toString()))
            }

        } catch (e: PackageManager.NameNotFoundException) {
            println("NameNotFoundException:  ${e.message}")
            e.printStackTrace()
        } catch (e: Exception) {
            println("exception: ${e.message}")
            e.printStackTrace()
        }
    }
    fun getVersionCode():Long{
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return pInfo.longVersionCode
        }
        return pInfo.versionCode.toLong()
    }
}