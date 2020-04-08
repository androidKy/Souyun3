package com.android.xplugin.tiktok

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.qq.v4.app.ActivitysHelpers
import android.qq.v4.app.ActivitysMethod
import android.qq.v4.app.callbacks.ActivitysLoadPackage
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.android.xplugin.PLUGIN_TAG
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
        if (!isAutoLogin)
            return

        val mainPageFragmentClass =
            ActivitysHelpers.findClassIfExists(TiktokClass.MainPageFragment, lpparam.classLoader)
        ActivitysHelpers.findAndHookMethod(
            mainPageFragmentClass,
            TiktokMethod.onViewCreated,
            View::class.java,
            Bundle::class.java,
            object : ActivitysMethod() {
                override fun afterHookedMethod(param: MethodHookParam) {

                    hideDialog()

                    val mMainBottomTabView = ActivitysHelpers.getObjectField(
                        param.thisObject,
                        TiktokClass.mMainBottomTabView
                    )
                    val userButton =
                        ActivitysHelpers.getObjectField(mMainBottomTabView, "j") as FrameLayout
                    userButton.postDelayed({
                        userButton.performClick()
                    }, 3000)
                }
            })
    }

    private fun hideDialog() {
        ActivitysHelpers.findAndHookMethod(Dialog::class.java,
            "show",
            Context::class.java,
            Int::class.java,
            Boolean::class.java,
            object : ActivitysMethod() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    Log.d(PLUGIN_TAG, "dialog show")

                    ActivitysHelpers.findAndHookMethod(TextView::class.java, "setText",
                        CharSequence::class.java, object : ActivitysMethod() {
                            override fun afterHookedMethod(param: MethodHookParam) {
                                val textView = (param.thisObject as TextView)
                                val isProtocoal = textView.text.toString() == "好的"
                                if (isProtocoal) {
                                    textView.performClick()
                                }
                            }
                        })
                }
            })
    }

}