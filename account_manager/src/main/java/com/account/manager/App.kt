package com.account.manager

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.safframework.log.L
import com.task.cn.util.AppUtils

/**
 * Description:
 * Created by Quinin on 2020-03-12.
 **/
class App:Application() {
    override fun onCreate() {
        super.onCreate()

        L.init("AccountManager")
        AppUtils.init(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}