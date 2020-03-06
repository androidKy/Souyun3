package com.task.cn.location

import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import com.utils.common.Utils

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

    private var mLocationListener: AMapLocationListener? = null
    private var mLocationClient: AMapLocationClient? = null

    fun setLocationListener(locationListener: AMapLocationListener): LocationController {
        mLocationListener = locationListener
        return this
    }

    override fun startLocation(ip: String) {
        AMapLocationClient(Utils.getApp()).run {
            
            mLocationClient = this

            setLocationListener(this@LocationController.mLocationListener)
            startLocation()
        }
    }


    override fun stopLocation() {
        mLocationClient?.apply {
            if (isStarted){
                stopLocation()
            }
            onDestroy()
        }
    }
}