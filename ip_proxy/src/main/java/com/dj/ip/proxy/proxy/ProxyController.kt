package com.dj.ip.proxy.proxy

import android.os.Looper
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.dj.ip.proxy.bean.IpBean
import com.safframework.log.L
import java.util.logging.Handler

/**
 * description:
 * author:kyXiao
 * date:2020/3/23
 */
class ProxyController {

    private var mPsw: String = ""
    private var mCityCode: String = ""
    private var mCityName: String = ""
    private var mIspFlag: Int = 0
    private var mCloseTime:Int = 1080

    private var mProxyRequestListener: ProxyRequestListener? = null

    fun setProxyRequestListener(proxyRequestListener: ProxyRequestListener): ProxyController {
        this.mProxyRequestListener = proxyRequestListener
        return this
    }

    fun setData(psw: String, cityName: String="", cityCode: String="0",closeTime:Int=1080, ispFlag: Int=0): ProxyController {
        this.mPsw = psw
        this.mCityName = cityName
        this.mCityCode = cityCode
        this.mIspFlag = ispFlag
        this.mCloseTime = closeTime
        return this
    }


    fun setData(psw: String, cityName: String="", cityCode: String="0", ispFlag: Int=0): ProxyController {
        this.mPsw = psw
        this.mCityName = cityName
        this.mCityCode = cityCode
        this.mIspFlag = ispFlag
        return this
    }

    fun setData(psw: String, cityName: String, cityCode: String): ProxyController {
        this.mPsw = psw
        this.mCityName = cityName
        this.mCityCode = cityCode
        return this
    }

    fun startProxy() {
        val proxyUrl =
            "http://10.8.0.1:8096/open?api=$mPsw&close_time=$mCloseTime&area=$mCityCode&isp=$mIspFlag"
        L.d("proxyUrl:$proxyUrl")
        AndroidNetworking.get(proxyUrl)
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    L.d("请求更换接口: $response")
                    if (!response.isNullOrEmpty() && response == "ok") {
                        android.os.Handler(Looper.getMainLooper())
                            .postDelayed({
                                PingManager.getNetIP(object : IpListener {
                                    override fun onIpResult(result: Boolean, ipBean: IpBean?) {
                                        if (result) {
                                            ipBean?.cname = mCityName
                                            mProxyRequestListener?.onIpResult(true, ipBean)
                                        } else mProxyRequestListener?.onIpResult(false, null)
                                    }
                                })
                            }, 1000)
                    } else {
                        mProxyRequestListener?.onIpResult(false, null)
                    }
                }

                override fun onError(anError: ANError?) {
                    L.d(anError?.response?.message())
                    mProxyRequestListener?.onIpResult(false, null)
                }
            })
    }
}