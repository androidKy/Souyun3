package com.task.cn.device

import android.os.Environment
import com.safframework.log.L
import com.task.cn.DeviceConstant
import com.task.cn.jbean.DeviceInfoBean
import com.utils.common.*
import com.utils.common.cmd.CommandUtil
import org.json.JSONObject
import java.io.File
import java.lang.StringBuilder

/**
 * description:
 * author:kyXiao
 * date:2020/3/27
 */
class MockDeviceController {
    private val DEVICE_INFO_FILE_PATH = "/data/local/tmp/app.setting.json"

    private val mPkgList: ArrayList<String> = arrayListOf()
    private var mOnMockedListener: OnMockedListener? = null
    private var mDeviceInfoBean: DeviceInfoBean? = null

    fun setPkgList(pkgList: ArrayList<String>): MockDeviceController {
        this.mPkgList.clear()
        this.mPkgList.addAll(pkgList)
        return this
    }

    fun setDeviceInfoBean(deviceInfoBean: DeviceInfoBean): MockDeviceController {
        this.mDeviceInfoBean = deviceInfoBean
        return this
    }

    fun setOnMockedListener(onMockedListener: OnMockedListener?): MockDeviceController {
        this.mOnMockedListener = onMockedListener
        return this
    }

    fun mockDevice() {
        if (mPkgList.size == 0) {
            responFailed("包名不能为空")
            return
        }
        /*if (mOnMockedListener == null) {
            responFailed("OnMockedListener不能为空")
            return
        }*/
        if (mDeviceInfoBean == null) {
            responFailed("设备信息不能为空")
            return
        }
        val deviceInfoFile = File(DEVICE_INFO_FILE_PATH)
        if (deviceInfoFile.exists()) {
            L.d("${DEVICE_INFO_FILE_PATH}存在,更新设备信息文件")
            updateDeviceInfoFile()
        } else {
            L.d("${DEVICE_INFO_FILE_PATH}不存在,现在sdcard创建文件然后复制过去")
            createDeviceInfoFile()
        }
    }

    private fun createDeviceInfoFile() {
        ThreadUtils.executeByCached(object : ThreadUtils.Task<Boolean>() {
            override fun doInBackground(): Boolean {
                var result = false
                try {
                    val filesDir = Environment.getExternalStorageDirectory().path
                    L.d("filesDir: $filesDir")
                    val filePath = "$filesDir${File.separator}app.setting.json"
                    val sdcardFile = File(filePath)
                    val createResult =
                        if (sdcardFile.exists()) {
                            sdcardFile.delete()
                            sdcardFile.createNewFile()
                        } else sdcardFile.createNewFile()

                    if (createResult) {
                        val command =
                            "cp -ar $filePath /data/local/tmp/;chown root:root $DEVICE_INFO_FILE_PATH;chmod 777 $DEVICE_INFO_FILE_PATH;"

                        CommandUtil.sendCommand(
                            command
                        ) { result = true }
                        FileIOUtils.writeFileFromString(
                            DeviceInfoController.DEVICE_INFO_FILE_PATH,
                            "{}"
                        )
                    }
                } catch (e: Exception) {
                    L.d(e.message)
                }
                return result
            }

            override fun onSuccess(result: Boolean) {
                if (result) {
                    updateDeviceInfoFile()
                } else {
                    responFailed("创建设备信息文件失败")
                }
            }

            override fun onCancel() {
            }

            override fun onFail(t: Throwable?) {
                responFailed("创建设备信息文件失败:${t?.message}")
            }

        })
    }

    private fun updateDeviceInfoFile() {
        ThreadUtils.executeByCached(object : ThreadUtils.Task<Boolean>() {
            override fun doInBackground(): Boolean {
                try {
                    val deviceInfo = FileIOUtils.readFile2String(DEVICE_INFO_FILE_PATH)
                    L.d("修改设备信息前：${deviceInfo}")
                    JSONObject(deviceInfo).also { jsonObj ->
                        //val keys = jsonObj.keys()
                        val clearCmd = StringBuilder()
                        for (pkg in mPkgList) {
                            jsonObj.put(pkg, setDeviceInfo(JSONObject()))
                            /* keys.apply {
                                 if (this.hasNext()) {
                                     this.forEach {
                                         if (it == pkg) {

                                         }
                                     }
                                 } else {
                                     jsonObj.put(pkg, setDeviceInfo(JSONObject()))
                                 }
                             }*/
                            if (pkg != Utils.getApp().packageName)
                                clearCmd.append("am force-stop $pkg;pm clear $pkg;")
                        }
                        FileIOUtils.writeFileFromString(DEVICE_INFO_FILE_PATH, jsonObj.toString())
                        L.d("修改设备信息后：${FileIOUtils.readFile2String(DEVICE_INFO_FILE_PATH)}")
                        val cmd = clearCmd.toString()
                        L.d("清楚APP数据：$cmd")
                        CommandUtil.sendCommand(cmd)
                    }
                } catch (e: Exception) {
                    L.d(e.message)
                    return false
                }
                return true
            }

            override fun onSuccess(result: Boolean) {
                if (result)
                    responSucceed("设备信息文件修改成功")
                else responFailed("设备信息文件修改失败")
            }

            override fun onCancel() {
            }

            override fun onFail(t: Throwable?) {
                responFailed("设备信息文件修改失败:${t?.message}")
            }
        })
    }

    private fun responSucceed(msg: String) {
        mOnMockedListener?.onMocked(true, msg)
    }

    private fun responFailed(msg: String) {
        mOnMockedListener?.onMocked(false, msg)
    }

    interface OnMockedListener {
        fun onMocked(status: Boolean, msg: String)
    }

    private fun setDeviceInfo(jsonObj: JSONObject): JSONObject {
        jsonObj.apply {
            put(DeviceConstant.BLUETOOTH_MACADDRESS_KEY, mDeviceInfoBean?.macAddress)
            put(DeviceConstant.BLUETOOTH_NAME_KEY, mDeviceInfoBean?.bluetoothName)
            put(DeviceConstant.DISPLAY_DPI, mDeviceInfoBean?.displayDpi)
            put(DeviceConstant.LANGUAGE, mDeviceInfoBean?.language)
            put(DeviceConstant.LATITUDE_KEY, mDeviceInfoBean?.latitude)
            put(DeviceConstant.LONGITUDE_KEY, mDeviceInfoBean?.longitude)
            put(DeviceConstant.NETWORK_TYPE_KEY, mDeviceInfoBean?.networkType)
            put(DeviceConstant.NETIP_ADDR_KEY, mDeviceInfoBean?.wifiIpAddr)
            put(DeviceConstant.BSSID_KEY, mDeviceInfoBean?.wifiBSSID)
            put(DeviceConstant.WIFI_MACADDRESS_KEY, mDeviceInfoBean?.wifiMacAddress)
            put(DeviceConstant.WIFI_SSID_KEY, mDeviceInfoBean?.wifiSSID)
            put(DeviceConstant.BUILD_ID_KEY, mDeviceInfoBean?.buildID)
            put(DeviceConstant.VERSION_RELEASE_KEY, mDeviceInfoBean?.versionRelease)
            put(DeviceConstant.VERSION_SDK_KEY, mDeviceInfoBean?.versionSDK)
            put(DeviceConstant.DESCRIPTION_KEY, mDeviceInfoBean?.description)
            put(DeviceConstant.FINGERPRINT_KEY, mDeviceInfoBean?.fingerprint)
            put(DeviceConstant.MANUFACTURER_KEY, mDeviceInfoBean?.manufacturer)
            put(DeviceConstant.MODEL_KEY, mDeviceInfoBean?.model)
            put(DeviceConstant.SERIALNO_KEY, mDeviceInfoBean?.serialno)
            put(DeviceConstant.ANDROID_ID_KEY, mDeviceInfoBean?.android_id)
            put(DeviceConstant.DEVICEID_KEY, mDeviceInfoBean?.deviceId)
            put(DeviceConstant.LINE1NUMBER_KEY, mDeviceInfoBean?.line1Number)
            put(DeviceConstant.SIMCOUNTRYISO_KEY, mDeviceInfoBean?.simCountryIso)
            put(DeviceConstant.SIMOPERATOR_KEY, mDeviceInfoBean?.simOperator)
            put(DeviceConstant.SIMOPERATORNAME_KEY, mDeviceInfoBean?.simOperatorName)
            put(DeviceConstant.SIMSERIALNUMBER_KEY, mDeviceInfoBean?.simSerialNumber)
            put(DeviceConstant.SIMSTATE_KEY, mDeviceInfoBean?.simState)
            put(DeviceConstant.SUBSCRIBERID_KEY, mDeviceInfoBean?.subscriberId)
            put(DeviceConstant.USERAGENT_KEY, mDeviceInfoBean?.userAgent)
            //新增的设备参数
            put(DeviceConstant.BASEBAND_KEY, mDeviceInfoBean?.baseBand)
            put(DeviceConstant.VERSIONHOST_KEY, mDeviceInfoBean?.versionHost)
            put(DeviceConstant.CODENAME_KEY, mDeviceInfoBean?.versionCodeName)
            put(DeviceConstant.INCREMENTAL_KEY, mDeviceInfoBean?.versionIncremental)
            put(DeviceConstant.BOOTLOADER_KEY, mDeviceInfoBean?.bootloader)
            put(DeviceConstant.DISPLAYID_KEY, mDeviceInfoBean?.displayId)
            put(DeviceConstant.HARDWARE_KEY, mDeviceInfoBean?.hardware)
            put(DeviceConstant.BRAND_KEY, mDeviceInfoBean?.brand)
            put(DeviceConstant.DEVICE_KEY, mDeviceInfoBean?.device)
            put(DeviceConstant.NAME_KEY, mDeviceInfoBean?.name)
            put(DeviceConstant.UTCDATE_KEY, mDeviceInfoBean?.utdDate)
            put(DeviceConstant.CPUFILE_KEY, mDeviceInfoBean?.cpuFile)
            put(DeviceConstant.ISROOTCLOCK_KEY, mDeviceInfoBean?.isRootClock)
            put(DeviceConstant.BOARD_KEY, mDeviceInfoBean?.board)
        }

        return jsonObj
    }
}