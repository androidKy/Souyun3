package com.dj.ip.proxy

import android.app.Application
import android.os.Debug
import android.os.Environment
import com.dj.ip.proxy.network.NetworkDetector
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.safframework.log.L
import com.utils.common.Utils

/**
 * description:
 * author:kyXiao
 * date:2020/3/23
 */
class ProxyApplication : Application() {
    companion object {
        const val DJ_WIDE_LOG = "DJ_WIDE_LOG"
    }

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)

        L.init(DJ_WIDE_LOG)
        // L.logLevel = if (BuildConfig.DEBUG) L.LogLevel.DEBUG else L.LogLevel.UNLOG
        // L.logLevel = L.LogLevel.DEBUG
        //val fileNamePath = Environment.getExternalStorageDirectory()?.absolutePath+"/methodTrace.trace"
        // L.i("fileNamePath: $fileNamePath")
        //Debug.startMethodTracing(fileNamePath)

        val formatStrategy = PrettyFormatStrategy.newBuilder()
            //.showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
            //.methodCount(0)         // (Optional) How many method line to show. Default 2
            //.methodOffset(0)        // (Optional) Hides internal method calls up to offset. Default 5
            .tag(DJ_WIDE_LOG)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    override fun onTerminate() {
        super.onTerminate()
    }

}