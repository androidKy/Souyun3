package com.task.cn.device

import android.os.Environment
import com.safframework.log.L
import com.task.cn.DeviceConstant
import com.task.cn.jbean.DeviceInfoBean
import com.utils.common.*
import org.json.JSONObject
import java.io.File
import java.lang.StringBuilder

/**
 * Description:
 * Created by Quinin on 2020-03-06.
 **/
class DeviceInfoController : IDeviceInfo {
    // private var mPkgName: String = ""
    private lateinit var mDeviceInfoBean: DeviceInfoBean
    private var mDeviceInfoListener: DeviceInfoListener? = null

    companion object {
        const val DEVICE_INFO_FILE_PATH = "/data/local/tmp/app.setting.json"
    }

    fun setDeviceInfoListener(deviceInfoListener: DeviceInfoListener): DeviceInfoController {
        this.mDeviceInfoListener = deviceInfoListener
        return this
    }

    /**
     * 添加一条设备信息
     */
    override fun addDeviceInfo(pkgName: String, deviceInfoBean: DeviceInfoBean) {
        //FileUtils.readFile2String()
        mDeviceInfoBean = deviceInfoBean

        if (isSettingFileExist()) //设备配置文件是否存在
        {
            editInfo(pkgName)
        } else {
            createFile(arrayListOf(pkgName))
        }
    }

    /**
     * 给多个应用添加设备信息
     */
    override fun addDeviceInfos(pkgNameList: ArrayList<String>, deviceInfoBean: DeviceInfoBean) {
        mDeviceInfoBean = deviceInfoBean
        if (pkgNameList.size == 1) {
            addDeviceInfo(pkgNameList[0], deviceInfoBean)
        } else {
            //批量添加应用程序相应的设备信息
            //先删除源文件
            val appFile = File(DEVICE_INFO_FILE_PATH)
            if (appFile.exists()) {
                //val deleteResult = appFile.deleteRecursively()
                ThreadUtils.executeByCached(object : ThreadUtils.Task<Boolean>() {
                    override fun doInBackground(): Boolean {
                        return !CMDUtil().execCmd("chmod 777 /data/local/tmp/*;rm -fr /data/local/tmp/app.*;")
                            .contains(
                                "denied"
                            )
                    }

                    override fun onSuccess(result: Boolean) {
                        L.d("删除local/data/tmp/app.setting.json文件: $result")
                        if (result) {
                            if (!isSettingFileExist())
                                createFile(pkgNameList)
                            else {
                                ToastUtils.showToast(
                                    Utils.getApp(),
                                    "删除/data/local/tmp/app.setting.json文件失败"
                                )
                                responResult(false)
                            }
                        } else {
                            ToastUtils.showToast(
                                Utils.getApp(),
                                "删除/data/local/tmp/app.setting.json文件失败"
                            )
                            responResult(false)
                        }
                    }

                    override fun onCancel() {
                    }

                    override fun onFail(t: Throwable?) {
                        responResult(false)
                    }
                })
            } else {
                createFile(pkgNameList)
            }

        }
    }

    private fun isSettingFileExist(): Boolean {
        val file = File(DEVICE_INFO_FILE_PATH)
        return file.exists()
    }

    /**
     * 直接修改文件信息
     */
    private fun editInfo(pkgName: String) {
        //先读取文件转成字符串
        ThreadUtils.executeByCached(object : ThreadUtils.Task<Boolean>() {
            override fun doInBackground(): Boolean {
                var resultBoolean = false
                val cmdResult = CMDUtil().execCmd("chmod 777 $DEVICE_INFO_FILE_PATH;")
                val result = FileIOUtils.readFile2String(DEVICE_INFO_FILE_PATH)
                L.d("配置前文件信息: $result")
                try {
                    JSONObject(result).run {
                        var pkgExist = false
                        for (key in this.keys()) {
                            if (key == pkgName) {
                                pkgExist = true
                                break
                            }
                        }
                        if (pkgExist) {
                            setDeviceInfo(getJSONObject(pkgName))
                        } else {
                            put(pkgName, setDeviceInfo(JSONObject()))
                        }
                        val str = this.toString()
                        FileIOUtils.writeFileFromString(DEVICE_INFO_FILE_PATH, str)

                        if (!cmdResult.contains("denied"))
                            resultBoolean = true

                        if (resultBoolean) {
                            if (pkgName != Utils.getApp().packageName)
                                CMDUtil().execCmd("pm clear $pkgName")
                        }
                    }
                } catch (e: Exception) {
                    L.d(e.message)
                }

                return resultBoolean
            }

            override fun onSuccess(result: Boolean) {
                responResult(result)
            }

            override fun onCancel() {
                responResult(false)
            }

            override fun onFail(t: Throwable?) {
                responResult(false)
            }
        })

    }

    /**
     * 创建文件并且添加文件信息
     */
    private fun createFile(pkgList: ArrayList<String>) {
        try {
            val filesDir = Environment.getExternalStorageDirectory().path

            L.d("filesDir: $filesDir")
            val filePath = "$filesDir${File.separator}app.setting.json"
            File(filePath).apply {
                if (exists()) {
                    val deleteResult = delete()
                    if (!deleteResult) {
                        ToastUtils.showToast(Utils.getApp(), "删除sdcard/app.setting.json文件失败")
                        responResult(false)
                        return
                    }
                }
            }
            val createResult = File(filePath).createNewFile()
            if (createResult) {
                ThreadUtils.executeByCached(object : ThreadUtils.Task<Boolean>() {
                    override fun doInBackground(): Boolean {
                        var result = false
                        try {
                            JSONObject().apply {
                                for (pkgName in pkgList) {
                                    put(pkgName, setDeviceInfo(JSONObject()))
                                }
                                //put(pkgList[0], setDeviceInfo(JSONObject()))

                                val str = this.toString()
                                FileIOUtils.writeFileFromString(filePath, str)
                            }

                            val execCmd =
                                CMDUtil().execCmd("cp -ar $filePath data/local/tmp/;")
                            if (!execCmd.contains("denied")) {
                                result = true
                            }

                            if (result) {
                                val clearSB = StringBuilder()
                                pkgList.forEach {
                                    if (it != Utils.getApp().packageName) {
                                        //CMDUtil().execCmd("pm clear ${pkgList[0]};")
                                        clearSB.append("pm clear $it;")
                                    }
                                }
                                L.d("clearCmd: ${clearSB.toString()}")
                                CMDUtil().execCmd(clearSB.toString())
                            }
                        } catch (e: Exception) {
                            L.d(e.message)
                        }

                        return result
                    }

                    override fun onSuccess(result: Boolean) {
                        responResult(result)
                    }

                    override fun onCancel() {
                        responResult(false)
                    }

                    override fun onFail(t: Throwable?) {
                        responResult(false)
                    }

                })
            } else {
                L.d("在目录 $filePath 下创建文件失败 ")
                ToastUtils.showToast(Utils.getApp(), "在目录${filePath}下创建文件失败 ")
                responResult(false)
            }
        } catch (e: Exception) {
            L.d(e.message)
            responResult(false)
        }
    }

    private fun setDeviceInfo(jsonObj: JSONObject): JSONObject {
        jsonObj.apply {
            put(DeviceConstant.BLUETOOTH_MACADDRESS_KEY, mDeviceInfoBean.macAddress)
            put(DeviceConstant.BLUETOOTH_NAME_KEY, mDeviceInfoBean.bluetoothName)
            put(DeviceConstant.DISPLAY_DPI, mDeviceInfoBean.displayDpi)
            put(DeviceConstant.LANGUAGE, mDeviceInfoBean.language)
            put(DeviceConstant.LATITUDE_KEY, mDeviceInfoBean.latitude)
            put(DeviceConstant.LONGITUDE_KEY, mDeviceInfoBean.longitude)
            put(DeviceConstant.NETWORK_TYPE_KEY, mDeviceInfoBean.networkType)
            put(DeviceConstant.NETIP_ADDR_KEY, mDeviceInfoBean.wifiIpAddr)
            put(DeviceConstant.BSSID_KEY, mDeviceInfoBean.wifiBSSID)
            put(DeviceConstant.WIFI_MACADDRESS_KEY, mDeviceInfoBean.wifiMacAddress)
            put(DeviceConstant.WIFI_SSID_KEY, mDeviceInfoBean.wifiSSID)
            put(DeviceConstant.BUILD_ID_KEY, mDeviceInfoBean.buildID)
            put(DeviceConstant.VERSION_RELEASE_KEY, mDeviceInfoBean.versionRelease)
            put(DeviceConstant.VERSION_SDK_KEY, mDeviceInfoBean.versionSDK)
            put(DeviceConstant.DESCRIPTION_KEY, mDeviceInfoBean.description)
            put(DeviceConstant.FINGERPRINT_KEY, mDeviceInfoBean.fingerprlong)
            put(DeviceConstant.MANUFACTURER_KEY, mDeviceInfoBean.manufacturer)
            put(DeviceConstant.MODEL_KEY, mDeviceInfoBean.model)
            put(DeviceConstant.SERIALNO_KEY, mDeviceInfoBean.serialno)
            put(DeviceConstant.ANDROID_ID_KEY, mDeviceInfoBean.android_id)
            put(DeviceConstant.DEVICEID_KEY, mDeviceInfoBean.deviceId)
            put(DeviceConstant.LINE1NUMBER_KEY, mDeviceInfoBean.line1Number)
            put(DeviceConstant.SIMCOUNTRYISO_KEY, mDeviceInfoBean.simCountryIso)
            put(DeviceConstant.SIMOPERATOR_KEY, mDeviceInfoBean.simOperator)
            put(DeviceConstant.SIMOPERATORNAME_KEY, mDeviceInfoBean.simOperatorName)
            put(DeviceConstant.SIMSERIALNUMBER_KEY, mDeviceInfoBean.simSerialNumber)
            put(DeviceConstant.SIMSTATE_KEY, mDeviceInfoBean.simState)
            put(DeviceConstant.SUBSCRIBERID_KEY, mDeviceInfoBean.subscriberId)
            put(DeviceConstant.USERAGENT_KEY, mDeviceInfoBean.userAgent)
            //新增的设备参数
            put(DeviceConstant.BASEBAND_KEY, mDeviceInfoBean.baseBand)
            put(DeviceConstant.VERSIONHOST_KEY, mDeviceInfoBean.versionHost)
            put(DeviceConstant.CODENAME_KEY, mDeviceInfoBean.versionCodeName)
            put(DeviceConstant.INCREMENTAL_KEY, mDeviceInfoBean.versionIncremental)
            put(DeviceConstant.BOOTLOADER_KEY, mDeviceInfoBean.bootloader)
            put(DeviceConstant.DISPLAYID_KEY, mDeviceInfoBean.displayId)
            put(DeviceConstant.HARDWARE_KEY, mDeviceInfoBean.hardware)
            put(DeviceConstant.BRAND_KEY, mDeviceInfoBean.brand)
            put(DeviceConstant.DEVICE_KEY, mDeviceInfoBean.device)
            put(DeviceConstant.NAME_KEY, mDeviceInfoBean.name)
            put(DeviceConstant.UTCDATE_KEY, mDeviceInfoBean.utdDate)
            put(DeviceConstant.CPUFILE_KEY, mDeviceInfoBean.cpuFile)
            put(DeviceConstant.ISROOTCLOCK_KEY, mDeviceInfoBean.isRootClock)
        }

        return jsonObj
    }


    private fun responResult(result: Boolean) {
        mDeviceInfoListener?.onChangeResult(result)
    }

}