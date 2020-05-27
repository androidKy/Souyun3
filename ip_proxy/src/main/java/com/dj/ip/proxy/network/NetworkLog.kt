package com.dj.ip.proxy.network

import android.os.Handler
import android.os.HandlerThread
import com.dj.ip.proxy.bean.IpBean
import com.dj.ip.proxy.proxy.IpListener
import com.dj.ip.proxy.proxy.PingManager
import com.safframework.log.L
import com.utils.common.NetworkUtils
import java.util.*

/**
 * description:
 * author:kyXiao
 * date:2020/5/25
 */
object NetworkLog {
    const val MSG_NETWORK_LOG = 0x0001

    private val mHandlerThread by lazy {
        HandlerThread("network-log")
    }

    private val mHandler by lazy {
        Handler(mHandlerThread.looper) {
            if (it.what == MSG_NETWORK_LOG) {
                logNetworkStatus(::writeLog2sdcard)
            }
            false
        }
    }

    fun startLog() {
        mHandlerThread.start()
        mHandler.sendEmptyMessage(MSG_NETWORK_LOG)
    }

    fun stopLog() {
        mHandlerThread.quitSafely()
        mHandler.removeCallbacksAndMessages(null)
    }

    private fun logNetworkStatus(writeLog: (netState: Boolean, ip: String?, cityName: String?) -> Unit) {
        val netState = NetworkUtils.isAvailable()
        if (!netState) {
            writeLog(netState, "", "")
            return
        }

        PingManager.getNetIP(object : IpListener {
            override fun onIpResult(result: Boolean, ipBean: IpBean?) {
                //writeLog2sdcard(true, ipBean?.cip, ipBean?.cname)
                writeLog(netState,ipBean?.cip,ipBean?.cname)
            }
        })
    }

    private fun writeLog2sdcard(netState: Boolean, ip: String? = "", cityName: String? = "") {
        val netStateStr = String.format(
            Locale.CHINESE, "网络状态:%s IP:%s 城市:%s",
            if (netState) "已连接" else "已断开", ip, cityName
        )
        L.d(netStateStr)

        mHandler.sendEmptyMessageDelayed(MSG_NETWORK_LOG, 30 * 1000)
    }
}