package com.task.cn.proxy

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.google.gson.Gson
import com.safframework.log.L
import com.task.cn.ProxyConstant
import com.task.cn.jbean.VerifyIpBean

/**
 * Description:
 * Created by Quinin on 2020-03-05.
 **/
class PingManager {
    companion object {
        fun verifyIP(cityName: String, ipListener: IpListener) {
            AndroidNetworking.get(ProxyConstant.PING_URL)
                    .build()
                    .getAsString(object : StringRequestListener {
                        override fun onResponse(response: String?) {
                            L.d("ip result: $response")
                            try {
                                val jsonData = response?.split("=")?.get(1)!!.replace(";", "")
                                val verifyIpBean = Gson().fromJson(jsonData, VerifyIpBean::class.java)

                                val cname = verifyIpBean.cname
                                if (cname.contains(cityName)) {
                                    ipListener.onIpResult(true, verifyIpBean)
                                } else ipListener.onIpResult(false, null)
                            } catch (e: Exception) {
                                L.d("解析IP数据失败：${e.message}")
                                ipListener.onIpResult(false, null)
                            }
                        }

                        override fun onError(anError: ANError?) {
                            ipListener.onIpResult(false, null)
                        }
                    })
        }

        fun getNetIP(ipListener: IpListener)
        {
            AndroidNetworking.get(ProxyConstant.PING_URL)
                .build()
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String?) {
                        L.d("ip result: $response")
                        try {
                            val jsonData = response?.split("=")?.get(1)!!.replace(";", "")
                            val verifyIpBean = Gson().fromJson(jsonData, VerifyIpBean::class.java)
                            ipListener.onIpResult(true, verifyIpBean)
                        } catch (e: Exception) {
                            L.d("解析IP数据失败：${e.message}")
                            ipListener.onIpResult(false, null)
                        }
                    }

                    override fun onError(anError: ANError?) {
                        ipListener.onIpResult(false, null)
                    }
                })
        }

        fun getLocalIp(ipListener: IpListener){

        }
    }
}

interface IpListener {
    fun onIpResult(result: Boolean, verifyIpBean: VerifyIpBean?)
}