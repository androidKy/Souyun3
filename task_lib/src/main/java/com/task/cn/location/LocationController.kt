package com.task.cn.location

import android.os.Handler
import android.os.Looper
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.google.gson.Gson
import com.safframework.log.L
import com.task.cn.ProxyConstant
import com.task.cn.SPConstant.Companion.KEY_CITY_NAME
import com.task.cn.SPConstant.Companion.SP_IP_INFO
import com.task.cn.jbean.IpLocationBean
import com.task.cn.manager.LocationListener
import com.utils.common.SPUtils

/**
 * Description:
 * Created by Quinin on 2020-03-06.
 **/
class LocationController private constructor() : ILocationController {

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LocationController()
        }
    }

    private var mLocationListener: LocationListener? = null

    private var mFailedCount = 0
    //private var mLocationClient: AMapLocationClient? = null

    fun setLocationListener(locationListener: LocationListener): LocationController {
        mLocationListener = locationListener
        return this
    }

    override fun startLocation(ip: String) {
        val url = ProxyConstant.CITY_LOCATION_URL + SPUtils.getInstance(SP_IP_INFO).getString(
            KEY_CITY_NAME
        )
        //val url = ProxyConstant.CITY_LOCATION_URL + ip
        /* ThreadUtils.executeByCached(object : ThreadUtils.Task<String>() {
             override fun doInBackground(): String? {
                 val request = Request.Builder().url(url).build()
                 val client = OkHttpClient.Builder().build()

                 val execute = client.newCall(request).execute()
                 return execute.body()?.string()
             }

             override fun onSuccess(result: String?) {
                 try {
                     result.apply {
                         val locationBean = Gson().fromJson(this, IpLocationBean::class.java)
                         if (locationBean != null) {
                             mLocationListener?.onLocationResult(
                                 locationBean.result.location.lat.toString(),
                                 locationBean.result.location.lng.toString()
                             )
                         }
                     }
                 } catch (e: Exception) {
                     L.d(e.message)
                     mLocationListener?.onLocationResult("0.0", "0.0")
                 }
             }

             override fun onCancel() {

             }

             override fun onFail(t: Throwable?) {
                 mLocationListener?.onLocationResult("0.0", "0.0")
             }
         })*/

        Handler(Looper.getMainLooper()).postDelayed({
            AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String?) {
                        try {
                            response?.apply {
                                val locationBean = Gson().fromJson(this, IpLocationBean::class.java)
                                if (locationBean != null) {
                                    mFailedCount = 0
                                    mLocationListener?.onLocationResult(
                                        locationBean.result.location.lat.toString(),
                                        locationBean.result.location.lng.toString()
                                    )
                                }
                            }
                        } catch (e: Exception) {
                            L.d(e.message)
                            failed(ip)
                        }
                    }

                    override fun onError(anError: ANError?) {
                        failed(ip)
                    }
                })
        }, 2000)
        /* AMapLocationClient(Utils.getApp()).run {

             mLocationClient = this
             setLocationListener(this@LocationController.mLocationListener)
             startLocation()
         }*/

    }

    private fun failed(ip: String) {
        mFailedCount++
        if (mFailedCount > 3) {
            mLocationListener?.onLocationResult("0.0", "0.0")
        } else {
            mFailedCount = 0
            startLocation(ip)
        }
    }


    override fun stopLocation() {
        /* mLocationClient?.apply {
             if (isStarted){
                 stopLocation()
             }
             onDestroy()
         }*/
    }

}