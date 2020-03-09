package com.task.cn.manager

import com.task.cn.location.ILocationController
import com.task.cn.location.LocationController

/**
 * Description:
 * Created by Quinin on 2020-03-06.
 **/
class LocationManager : ILocationController {

    private var mLocationListener: LocationListener? = null

    fun setLocationListener(locationListener: LocationListener): LocationManager {
        mLocationListener = locationListener
        return this
    }

    override fun startLocation(ip: String) {
        mLocationListener?.apply {
            LocationController.instance
                .setLocationListener(this)
                .startLocation(ip)
        }
    }


    override fun stopLocation() {
        LocationController.instance.stopLocation()
    }
}