package com.android.xplugin.wechat

import android.qq.v4.app.ActivitysHelpers
import android.qq.v4.app.ActivitysMethod
import android.qq.v4.app.callbacks.ActivitysLoadPackage
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.android.xplugin.PLUGIN_TAG
import com.android.xplugin.util.XSPUtils
import com.android.xplugin.util.XSPUtils.Companion.LOGIN_WECHAT_KEY
import com.android.xplugin.wechat.ClassConstants.Companion.LoginUI_Activity
import com.android.xplugin.wechat.ClassConstants.Companion.MOBILEINPUTUI_ACTIVITY
import com.android.xplugin.wechat.ClassConstants.Companion.WELCOME_VIEW

/**
 * Description:
 * Created by Quinin on 2020-03-21.
 **/
class WechatPlugin(
    private val XSPUtils: XSPUtils,
    private val lpparam: ActivitysLoadPackage.LoadPackageParam
) {

    private var mIsLogining = false
    private val WX_TAG = PLUGIN_TAG

    private var mIsAutoLogin = false


    fun login() {
        mIsAutoLogin = XSPUtils.getBoolean(LOGIN_WECHAT_KEY)
        Log.d(PLUGIN_TAG,"mIsAutoLogin:$mIsAutoLogin")
        val welcomeActivityClass =
            ActivitysHelpers.findClassIfExists(ClassConstants.WELCOME_ACTIVITY, lpparam.classLoader)

        ActivitysHelpers.findAndHookMethod(welcomeActivityClass, MethodConstants.ONRESUME,
            object : ActivitysMethod() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    if (mIsLogining || !mIsAutoLogin) {
                        return
                    }
                    log("WelcomeActivity onResume: $mIsLogining")
                    val welcomeActivityObj = param.thisObject
                    val welcomeViewObj =
                        ActivitysHelpers.findFieldIfExists(welcomeActivityClass, "ilR")
                            .get(welcomeActivityObj)

                    val welcomeViewClass =
                        ActivitysHelpers.findClassIfExists(WELCOME_VIEW, lpparam.classLoader)
                    val loginBtn = ActivitysHelpers.findFieldIfExists(welcomeViewClass, "ilT")
                        .get(welcomeViewObj) as Button

                    log("ilT: ${loginBtn.text}")
                    if (loginBtn.text == "登录") {
                        mIsLogining = true

                        loginBtn.postDelayed({
                            loginBtn.performClick()
                        }, 500)
                    }
                }
            })

        val mobileInputUIClass =
            ActivitysHelpers.findClassIfExists(MOBILEINPUTUI_ACTIVITY, lpparam.classLoader)
        ActivitysHelpers.findAndHookMethod(mobileInputUIClass, MethodConstants.ONRESUME,
            object : ActivitysMethod() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    if (!mIsAutoLogin)
                        return

                    val ifCbtn = ActivitysHelpers.findFieldIfExists(mobileInputUIClass, "ifC")
                        .get(param.thisObject) as Button
                    val ifDbtn = ActivitysHelpers.findFieldIfExists(mobileInputUIClass, "ifD")
                        .get(param.thisObject) as Button
                    val ifFbtn = ActivitysHelpers.findFieldIfExists(mobileInputUIClass, "ifF")
                        .get(param.thisObject) as Button
                    val ifUbtn = ActivitysHelpers.findFieldIfExists(mobileInputUIClass, "ifU")
                        .get(param.thisObject) as Button
                    val ihpBtn = ActivitysHelpers.findFieldIfExists(mobileInputUIClass, "ihp")
                        .get(param.thisObject) as Button
                    val ihtBtn = ActivitysHelpers.findFieldIfExists(mobileInputUIClass, "iht")
                        .get(param.thisObject) as Button

                    log(
                        "ifc:${ifCbtn.text} ifd:${ifDbtn.text} ifF:${ifFbtn.text}\n" +
                                "ifU:${ifUbtn.text} ihp:${ihpBtn.text} iht:${ihtBtn.text}"
                    )
                    findAccountLoginBtn(ifCbtn, ifDbtn, ifFbtn, ifUbtn, ihpBtn, ihtBtn)?.apply {
                        this.postDelayed({
                            performClick()
                        }, 500)
                    }
                }
            })

        val loginUIClass = ActivitysHelpers.findClassIfExists(LoginUI_Activity, lpparam.classLoader)
        ActivitysHelpers.findAndHookMethod(loginUIClass, "initView",
            object : ActivitysMethod() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    if (!mIsAutoLogin)
                        return
                    val loginUIObj = param.thisObject
                    val igBbtn = ActivitysHelpers.findFieldIfExists(loginUIClass, "igB")
                        .get(loginUIObj) as Button
                    log("igB:${igBbtn.text}")
                    val igXedit =
                        ActivitysHelpers.getObjectField(loginUIObj, "igx") as EditText
                    val igYedit =
                        ActivitysHelpers.getObjectField(loginUIObj, "igy") as EditText

                    ActivitysHelpers.callMethod(igXedit, "setText", "13825110563")
                    ActivitysHelpers.callMethod(igYedit, "setText", "lqy12020119")

                    /*   val fObj = ActivitysHelpers.getObjectField(param.thisObject, "ifd")
                       ActivitysHelpers.setObjectField(fObj, "account", "13825110563")
                       ActivitysHelpers.setObjectField(fObj, "hXD", "lqy12020119")

                       log("account: ${ActivitysHelpers.getObjectField(fObj, "account") as String}")
                       log("psw: ${ActivitysHelpers.getObjectField(fObj, "hXD") as String}")*/

                    //ActivitysHelpers.callMethod(param.thisObject, "aIH")

                    igBbtn.postDelayed({
                        igBbtn.isEnabled = true
                        igBbtn.performClick()
                        // ActivitysHelpers.callStaticMethod(loginUIClass, "l", param.thisObject)
                    }, 1000)
                }
            })
    }

    /**
     * 查找账号登录按钮
     */
    private fun findAccountLoginBtn(
        btn0: Button,
        btn1: Button,
        btn2: Button,
        btn3: Button,
        btn4: Button,
        btn5: Button
    ): Button? {
        val accountContent = "用微信号"
        if (btn0.text.contains(accountContent)) {
            return btn0
        }
        if (btn1.text.contains(accountContent)) {
            return btn1
        }
        if (btn2.text.contains(accountContent)) {
            return btn2
        }
        if (btn3.text.contains(accountContent)) {
            return btn3
        }
        if (btn4.text.contains(accountContent)) {
            return btn4
        }
        if (btn5.text.contains(accountContent)) {
            return btn5
        }
        return null
    }

    fun log(msg: String) {
        Log.d(WX_TAG, msg)
        //ActivitysBridge.log(msg)
    }
}

