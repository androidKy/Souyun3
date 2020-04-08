package com.android.xplugin

import android.app.Application
import android.qq.v4.app.ActivitysHelpers
import android.qq.v4.app.ActivitysMethod
import android.qq.v4.app.IActivitysLoadPackage
import android.qq.v4.app.callbacks.ActivitysLoadPackage
import com.android.xplugin.tiktok.TiktokPlugin
import com.android.xplugin.util.XSPUtils
import com.android.xplugin.wechat.WechatPlugin
import com.swift.sandhook.xposedcompat.utils.ApplicationUtils

/**
 * description:
 * author:kyXiao
 * date:2020/3/31
 */
class ActivityMain : IActivitysLoadPackage {
    override fun handleLoadPackage(packageParam: ActivitysLoadPackage.LoadPackageParam) {
        val pkgName = packageParam.packageName

        ActivitysHelpers.findAndHookMethod(
            Application::class.java,
            "onCreate",
            object : ActivitysMethod() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    val context = ApplicationUtils.currentApplication()
                    if (context != null && context.packageName == PKG_WECHAT) {
                        val spUtils = XSPUtils.instance.setContext(context)
                        WechatPlugin(spUtils, packageParam).login()
                    } else if (context != null && context.packageName == PKG_TIKTOK) {
                       // TiktokPlugin(XSPUtils.instance.setContext(context), packageParam).init()
                    }
                }
            })

    }

}