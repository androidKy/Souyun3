package com.dj.ip.proxy.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import com.dj.ip.proxy.Constants
import com.dj.ip.proxy.bean.IpBean
import com.dj.ip.proxy.notification.NotificationStarter
import com.dj.ip.proxy.proxy.ProxyController
import com.dj.ip.proxy.proxy.ProxyRequestListener
import com.dj.ip.proxy.receiver.IpActionReceiver
import com.utils.common.SPUtils

/**
 * description:
 * author:kyXiao
 * date:2020/5/22
 */
class IpBgService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val psw = it.getStringExtra(IpActionReceiver.LOGIN_PSW) ?: ""
            val cityCode = it.getStringExtra(IpActionReceiver.CITY_NAME) ?: "0"
            val keepAliveTime = it.getIntExtra(IpActionReceiver.KEEP_ALIVE, 1080)
            val ipIsp = it.getIntExtra(IpActionReceiver.IP_ISP, 0)

            SPUtils.getInstance(Constants.IP_PROXY_SP).put(Constants.PSW_KEY, psw)

            ProxyController()
                .setData(psw, "", cityCode, keepAliveTime, ipIsp)
                .setProxyRequestListener(object : ProxyRequestListener {
                    override fun onIpResult(result: Boolean, ipBean: IpBean?) {
                        NotificationStarter.startNotification(if (result) "连接成功" else "连接失败")
                    }
                }).startProxy()
        }
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        /*Intent(this,IpBgService::class.java).let { serviceIntent ->
            serviceIntent.setClass(this, IpBgService::class.java)
            serviceIntent.putExtras(Bundle().apply {
                putString(IpActionReceiver.LOGIN_PSW, psw)
                putString(IpActionReceiver.CITY_NAME, cityName)
                putString(IpActionReceiver.KEEP_ALIVE, keepAliveTime)
                putString(IpActionReceiver.IP_ISP, ipIsp)
            })
            startService(serviceIntent)
        }*/
    }
}