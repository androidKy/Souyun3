package com.task.cn.proxy

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.google.gson.Gson
import com.safframework.log.L
import com.task.cn.ProxyConstant
import com.task.cn.jbean.IpInfo2Bean
import com.task.cn.jbean.VerifyIpBean
import com.utils.common.ThreadUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

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

        fun getNetIP(ipListener: IpListener) {

            ThreadUtils.executeByCached(object : ThreadUtils.Task<String>() {
                override fun doInBackground(): String? {
                    val request = Request.Builder().url(ProxyConstant.IPINFO_URL).build()
                    val client = OkHttpClient.Builder().build()

                    val execute = client.newCall(request).execute()

                    return execute.body()?.string()
                }

                override fun onSuccess(response: String?) {
                    L.d("ip result: $response")
                    try {
                        /*val jsonData = response?.split("=")?.get(1)!!.replace(";", "")
                        val verifyIpBean = Gson().fromJson(jsonData, VerifyIpBean::class.java)
                        ipListener.onIpResult(true, verifyIpBean)*/
                        val leftIndex = response?.indexOfLast {
                            it == '('
                        }
                        val rightIndex = response?.indexOfLast { it == ')' }

                        val jsonData = response?.substring(leftIndex!! + 1, rightIndex!!)

                        Gson().fromJson(jsonData!!, IpInfo2Bean::class.java).apply {
                            val verifyIpBean = VerifyIpBean().let {
                                it.cid = this.cityCode
                                it.cip = this.ip
                                it.cname = this.pro + this.city
                                it
                            }
                            ipListener.onIpResult(true, verifyIpBean)
                        }
                        L.d("ip json result: $jsonData")
                    } catch (e: Exception) {
                        L.d("解析IP数据失败：${e.message}")
                        ipListener.onIpResult(false, null)
                    }
                }

                override fun onCancel() {

                }

                override fun onFail(t: Throwable?) {
                    ipListener.onIpResult(false, null)
                }

            })

            /*AndroidNetworking.get(ProxyConstant.PING_URL)
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
                })*/
        }

        fun getLocalIp(ipListener: IpListener) {

        }
    }
}

interface IpListener {
    fun onIpResult(result: Boolean, verifyIpBean: VerifyIpBean?)
}