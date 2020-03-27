package com.task.cn.device

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.interfaces.StringRequestListener
import com.task.cn.URL
import com.task.cn.jbean.DeviceInfoBean

/**
 * description:
 * author:kyXiao
 * date:2020/3/27
 */
class DeviceManager {
    private var mStringRequestListener: StringRequestListener? = null
    private var mOnMockedListener: MockDeviceController.OnMockedListener? = null

    fun setRequestListener(stringRequestListener: StringRequestListener): DeviceManager {
        this.mStringRequestListener = stringRequestListener
        return this
    }

    fun setOnMockedListener(onMockedListener: MockDeviceController.OnMockedListener): DeviceManager {
        this.mOnMockedListener = onMockedListener
        return this
    }

    /**
     * 获取设备信息
     */
    fun getDeviceInfo(platform: Int) {
        mStringRequestListener?.also {
            val respon = AndroidNetworking.get("${URL.GET_DEVICE_INFO_URL}$platform")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .executeForString()

            //todo
            //.getAsString(it)
        }
    }

    /**
     * 修改设备信息文件
     */
    fun mockDevice(pkgList: ArrayList<String>, deviceInfoBean: DeviceInfoBean) {
        MockDeviceController()
            .setPkgList(pkgList)
            .setDeviceInfoBean(deviceInfoBean)
            .setOnMockedListener(mOnMockedListener)
            .mockDevice()
    }
}