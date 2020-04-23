package com.dj.ip.proxy

import android.app.Application
import com.dj.ip.proxy.network.NetworkDetector
import com.safframework.log.L
import com.utils.common.Utils

/**
 * description:
 * author:kyXiao
 * date:2020/3/23
 */
class ProxyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)

        L.init("Proxy_IP")
       // L.logLevel = if (BuildConfig.DEBUG) L.LogLevel.DEBUG else L.LogLevel.UNLOG

    }

    override fun onTerminate() {
        super.onTerminate()
    }

}