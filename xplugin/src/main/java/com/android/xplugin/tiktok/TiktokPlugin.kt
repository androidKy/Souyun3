package com.android.xplugin.tiktok

import android.os.Bundle
import android.qq.v4.app.ActivitysHelpers
import android.qq.v4.app.ActivitysMethod
import android.qq.v4.app.callbacks.ActivitysLoadPackage
import android.view.View
import android.widget.FrameLayout
import com.android.xplugin.XPlugin
import com.android.xplugin.util.XSPUtils

/**
 * description:
 * author:kyXiao
 * date:2020/3/31
 */
class TiktokPlugin(
    private val xspUtils: XSPUtils,
    private val lpparam: ActivitysLoadPackage.LoadPackageParam
) : XPlugin(xspUtils, lpparam) {
    override fun init() {
        login()
    }

    override fun login() {
        val isAutoLogin = xspUtils.getBoolean(XSPUtils.LOGIN_TIKTOK_SWITCH_KEY)

        val mainPageFragmentClass =
            ActivitysHelpers.findClassIfExists(TiktokClass.MainPageFragment, lpparam.classLoader)
        ActivitysHelpers.findAndHookMethod(
            mainPageFragmentClass,
            TiktokMethod.onViewCreated,
            View::class.java,
            Bundle::class.java,
            object : ActivitysMethod() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    if (!isAutoLogin)
                        return
                    val mMainBottomTabView = ActivitysHelpers.getObjectField(
                        param.thisObject,
                        TiktokClass.mMainBottomTabView
                    )
                    val userButton =
                        ActivitysHelpers.getObjectField(mMainBottomTabView, "j") as FrameLayout
                    userButton.postDelayed({
                        userButton.performClick()
                    }, 1000)
                }
            })
    }

}