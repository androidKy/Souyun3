package com.task.cn.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.task.cn.jbean.AppInfo
import com.utils.common.Utils
import io.realm.Realm
import android.content.pm.ApplicationInfo
import android.content.pm.ApplicationInfo.FLAG_SYSTEM


/**
 * Description:
 * Created by Quinin on 2020-03-04.
 **/
class AppUtils {
    companion object {
        fun init(context: Context) {
            Utils.init(context)
            Realm.init(context)
        }

        /**
         * 获取用户应用程序
         */
        fun getUserApps(): List<AppInfo> {
            val appInfoList = ArrayList<AppInfo>()
            val flag =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) PackageManager.MATCH_UNINSTALLED_PACKAGES
                else PackageManager.GET_UNINSTALLED_PACKAGES

            val installedPackages = Utils.getApp().packageManager.getInstalledPackages(flag)
            for (pkgInfo in installedPackages) {
                AppInfo().apply {
                    if ((pkgInfo.applicationInfo.flags and FLAG_SYSTEM) <= 0) {
                        appName =
                            pkgInfo.applicationInfo.loadLabel(Utils.getApp().packageManager)
                                .toString()
                        pkgName = pkgInfo.packageName
                        versionName = pkgInfo.versionName
                        versionCode =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) pkgInfo.longVersionCode.toString()
                            else pkgInfo.versionCode.toString()

                        iconDrawable =
                            pkgInfo.applicationInfo.loadIcon(Utils.getApp().packageManager)

                        appInfoList.add(this)
                    }
                }
            }

            return appInfoList
        }
    }
}