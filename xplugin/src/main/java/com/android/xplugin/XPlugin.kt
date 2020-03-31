package com.android.xplugin

import android.qq.v4.app.callbacks.ActivitysLoadPackage
import com.android.xplugin.util.XSPUtils

/**
 * description:
 * author:kyXiao
 * date:2020/3/31
 */
abstract class XPlugin(
   XSPUtils: XSPUtils,
    lpparam: ActivitysLoadPackage.LoadPackageParam
) {
    abstract fun init()
    abstract fun login()
}