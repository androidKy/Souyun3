package com.task.cn.location

import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseListener
import com.google.gson.Gson
import com.safframework.log.L
import com.task.cn.ProxyConstant
import com.task.cn.jbean.LocationBean
import com.task.cn.manager.LocationListener
import com.utils.common.Utils
import okhttp3.Response

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
    //private var mLocationClient: AMapLocationClient? = null

    fun setLocationListener(locationListener: LocationListener): LocationController {
        mLocationListener = locationListener
        return this
    }

    override fun startLocation(ip: String) {

        AndroidNetworking.get(ProxyConstant.LOCATION_URL)
            .build()
            .getAsOkHttpResponse(object : OkHttpResponseListener {
                override fun onResponse(response: Response?) {
                    try {
                        response?.body()?.string()?.apply {
                            val locationBean = Gson().fromJson(this, LocationBean::class.java)
                            if (locationBean != null) {
                                mLocationListener?.onLocationResult(
                                    locationBean.lat.toString(),
                                    locationBean.lon.toString()
                                )
                            }
                        }
                    } catch (e: Exception) {
                        L.d(e.message)
                        mLocationListener?.onLocationResult("0.0", "0.0")
                    }
                }

                override fun onError(anError: ANError?) {
                    mLocationListener?.onLocationResult("0.0", "0.0")
                }
            })
        /* AMapLocationClient(Utils.getApp()).run {

             mLocationClient = this
             setLocationListener(this@LocationController.mLocationListener)
             startLocation()
         }*/

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