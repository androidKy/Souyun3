package com.task.cn.device

import com.task.cn.jbean.DeviceInfoBean

/**
 * Description:设置
 * Created by Quinin on 2020-03-06.
 **/
interface IDeviceInfo {
    fun addDeviceInfo(pkgName: String, deviceInfoBean: DeviceInfoBean)

    fun addDeviceInfos(pkgNameList: ArrayList<String>, deviceInfoBean: DeviceInfoBean)

}