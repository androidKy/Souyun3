package com.task.cn.location

import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseListener
import com.google.gson.Gson
import com.safframework.log.L
import com.task.cn.ProxyConstant
import com.task.cn.SPConstant.Companion.KEY_CITY_NAME
import com.task.cn.SPConstant.Companion.SP_IP_INFO
import com.task.cn.database.RealmHelper
import com.task.cn.jbean.IpLocationBean
import com.task.cn.jbean.LocationBean
import com.task.cn.manager.LocationListener
import com.utils.common.SPUtils
import com.utils.common.ThreadUtils
import com.utils.common.Utils
import okhttp3.OkHttpClient
import okhttp3.Request
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
        //val url = ProxyConstant.CITY_LOCATION_URL+SPUtils.getInstance(SP_IP_INFO).getString(KEY_CITY_NAME)
        val url = ProxyConstant.CITY_LOCATION_URL + ip

        ThreadUtils.executeByCached(object : ThreadUtils.Task<String>() {
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
        })

        /*  AndroidNetworking.get(url)
              .build()
              .getAsOkHttpResponse(object : OkHttpResponseListener {
                  override fun onResponse(response: Response?) {
                      try {
                          response?.body()?.string()?.apply {
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

                  override fun onError(anError: ANError?) {
                      mLocationListener?.onLocationResult("0.0", "0.0")
                  }
              })*/
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