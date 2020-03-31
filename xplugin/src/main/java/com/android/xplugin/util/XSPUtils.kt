package com.android.xplugin.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.xplugin.PLUGIN_TAG
import com.crossbowffs.remotepreferences.RemotePreferences
import java.lang.ref.WeakReference

/**
 * description:
 * author:kyXiao
 * date:2020/3/31
 */
class XSPUtils private constructor() {

    private var mSpWR: WeakReference<SharedPreferences> = WeakReference<SharedPreferences>(null)
    private var mContext: Context? = null


    companion object {
        const val DATA_HOOK_SP = "data_hook_sp"

        const val ACCOUNT_KEY = "account"
        const val PSW_KEY = "psw"
        //wechat
        const val LOGIN_WECHAT_KEY = "login_wechat"
        //tiktok
        const val LOGIN_TIKTOK_SWITCH_KEY = "login_tiktok_switch"
        //kuaishou
        const val LOGIN_KS_SWTICH_KEY = "login_ks_switch"

        const val SP_AUTHORITY = "com.android.xplugin.preferences"

        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            XSPUtils()
        }

    }

    fun setContext(context: Context?): XSPUtils {
        mContext = context

        return this
    }

    private fun getSP(): SharedPreferences? {
        if (mContext == null)
            return null

        Log.d(PLUGIN_TAG, "sp pkgName:${mContext?.packageName}")

        return RemotePreferences(mContext, SP_AUTHORITY, DATA_HOOK_SP)
    }

    fun getBoolean(key: String?, defValue: Boolean = false): Boolean {
        return getSP()?.getBoolean(key, defValue) ?: defValue
    }

    fun getLong(key: String?, defValue: Long = 0L): Long {
        return getSP()?.getLong(key, defValue) ?: defValue
    }

    fun getFloat(key: String?, defValue: Float = 0.0F): Float {
        return getSP()?.getFloat(key, defValue) ?: defValue
    }

    fun getString(key: String?, defValues: String = ""): String {
        return getSP()?.getString(key, defValues) ?: defValues
    }

}