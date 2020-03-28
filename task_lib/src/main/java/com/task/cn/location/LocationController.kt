package com.task.cn.location

import android.os.Looper
import com.google.gson.Gson
import com.safframework.log.L
import com.task.cn.ProxyConstant
import com.task.cn.SPConstant.Companion.KEY_CITY_NAME
import com.task.cn.SPConstant.Companion.SP_IP_INFO
import com.task.cn.jbean.IpLocationBean
import com.task.cn.manager.LocationListener
import com.utils.common.SPUtils
import com.utils.common.ThreadUtils
import com.utils.common.ToastUtils
import com.utils.common.Utils
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * Description:
 * Created by Quinin on 2020-03-06.
 **/
class LocationController private constructor() : ILocationController {

    private var mConnectTimes: Int = 0

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LocationController()
        }
    }

    init {
        mConnectTimes = 0
    }

    private var mLocationListener: LocationListener? = null
    //private var mLocationClient: AMapLocationClient? = null

    val mHandler:android.os.Handler = android.os.Handler(Looper.getMainLooper()){
        if(it.what == 400)
        {

        }
        false
    }

    fun setLocationListener(locationListener: LocationListener): LocationController {
        mLocationListener = locationListener
        return this
    }

    private fun getLocationInfo(url: String): String? {
        var execute: Response? = null
        try {
            val request = Request.Builder().url(url).build()
            val client = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()

            execute = client.newCall(request).execute()
            val response = execute.body()?.string()
            /*if (response.isNullOrEmpty()) {
                if (mConnectTimes <= 3) {
                    return getLocationInfo(url)
                }
            }*/
            return response
        } catch (e: Exception) {
        } finally {
            execute?.close()
        }

        return ""
    }

    override fun startLocation(ip: String) {
        val url = ProxyConstant.CITY_LOCATION_URL + SPUtils.getInstance(SP_IP_INFO).getString(
            KEY_CITY_NAME
        )

        ThreadUtils.executeByCached(object : ThreadUtils.Task<String>() {
            override fun doInBackground(): String? {
                return getLocationInfo(url)
            }

            override fun onSuccess(result: String?) {
                try {
                    ToastUtils.showToast(Utils.getApp(), "获取经纬度: $result")
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