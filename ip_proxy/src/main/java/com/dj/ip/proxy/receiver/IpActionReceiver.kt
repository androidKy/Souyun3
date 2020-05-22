package com.dj.ip.proxy.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.dj.ip.proxy.service.IpBgService
import com.dj.ip.proxy.utils.CityUtils
import com.safframework.log.L

/**
 * description:
 * author:kyXiao
 * date:2020/5/22
 */
class IpActionReceiver : BroadcastReceiver() {
    //adb shell am broadcast -a com.dj.handsome.receiver --es psw "123431" --es city_name "汕头市" --ei keep_alive 100 --ei isp 1
    companion object {
        const val LOGIN_PSW = "psw"
        const val CITY_NAME = "city_name"
        const val KEEP_ALIVE = "keep_alive"
        const val IP_ISP = "isp"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.apply {
            intent?.let {
                val psw = it.getStringExtra(LOGIN_PSW)
                val cityName = it.getStringExtra(CITY_NAME)
                val keepAliveTime = it.getIntExtra(KEEP_ALIVE,1080)
                val ipIsp = it.getIntExtra(IP_ISP,0)
                Log.d(
                    this@IpActionReceiver::class.java.simpleName,
                    "psw=$psw\ncityName=$cityName\ntime=$keepAliveTime\nipIsp=$ipIsp"
                )
                if (psw.isNullOrEmpty())
                    return

                CityUtils.getCityCodeByCityName(
                    this,
                    cityName,
                    object : CityUtils.CityCodeListener {
                        override fun onCityCode(cityCode: String) {
                            Log.d(
                                this@IpActionReceiver::class.java.simpleName,
                                "根据城市名查找到的cityCode: $cityCode"
                            )
                            Intent().let { serviceIntent ->
                                serviceIntent.setClass(this@apply, IpBgService::class.java)
                                serviceIntent.putExtras(Bundle().apply {
                                    putString(LOGIN_PSW, psw)
                                    putString(CITY_NAME, cityName)
                                    putInt(KEEP_ALIVE, keepAliveTime)
                                    putInt(IP_ISP, ipIsp)
                                })
                                startService(serviceIntent)
                            }
                        }
                    })
            }
        }
    }
}