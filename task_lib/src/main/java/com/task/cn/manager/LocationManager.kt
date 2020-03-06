package com.task.cn.manager

import com.amap.api.location.AMapLocationListener
import com.safframework.log.L
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
                .setLocationListener(AMapLocationListener {
                    it?.apply {
                        if (this.errorCode == 0) {
                            val longitude = this.longitude
                            val latitude = this.latitude

                            mLocationListener?.onLocationResult(
                                latitude.toString(),
                                longitude.toString()
                            )
                        } else {
                            L.d("定位失败: code:${this.errorCode} errorInfo:${this.errorInfo}")
                            mLocationListener?.onLocationResult("0", "0")
                        }
                    }

                })
                .startLocation(ip)
        }
    }


    override fun stopLocation() {
        LocationController.instance.stopLocation()
    }
}