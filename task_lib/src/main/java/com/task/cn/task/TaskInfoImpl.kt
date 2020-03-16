package com.task.cn.task

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.google.gson.Gson
import com.safframework.log.L
import com.task.cn.*
import com.task.cn.SPConstant.Companion.SP_DEVICE_INFO
import com.task.cn.URL.Companion.GET_DEVICE_INFO_URL
import com.task.cn.device.DeviceInfoController
import com.task.cn.device.DeviceInfoListener
import com.task.cn.jbean.AccountInfoBean
import com.task.cn.jbean.DeviceInfoBean
import com.task.cn.jbean.IpInfoBean
import com.task.cn.jbean.TaskBean
import com.task.cn.manager.LocationListener
import com.task.cn.manager.LocationManager
import com.task.cn.model.DeviceModel
import com.task.cn.proxy.ProxyManager
import com.task.cn.proxy.ProxyRequestListener
import com.task.cn.util.AppUtils
import com.utils.common.SPUtils
import com.utils.common.Utils
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Description:
 * Created by Quinin on 2020-03-03.
 **/
class TaskInfoImpl(private val taskInfoView: TaskInfoView) : ITaskInfo {

    override fun getTaskInfo() {
        Result(StatusCode.FAILED, TaskBean(), StatusMsg.DEFAULT.msg).run {
            try {
                val inputStream = Utils.getApp().assets.open("task_info.json")

                val content = BufferedReader(InputStreamReader(inputStream))
                    .lineSequence()
                    .fold(StringBuilder()) { buff, line -> buff.append(line) }
                    .toString()
                    .replace("\\s+".toRegex(), "")
                L.d(content)

                val taskBean = Gson().fromJson(content, TaskBean::class.java)

                this.code = StatusCode.SUCCEED
                this.r = taskBean
                this.msg = StatusMsg.SUCCEED.msg
            } catch (e: Exception) {
                L.d(e.message)
                this.msg = if (e.message == null) "getTaskInfo json解析失败" else e.message!!
            }

            taskInfoView.onResponTaskInfo(this)
        }
    }

    override fun getTaskInfo(taskBean: TaskBean) {
        Result(StatusCode.SUCCEED, taskBean, StatusMsg.SUCCEED.msg).apply {
            taskInfoView.onResponTaskInfo(this)
        }

    }

    override fun getAccountInfo() {
        Result(StatusCode.FAILED, AccountInfoBean(), StatusMsg.DEFAULT.msg).run {


            taskInfoView.onResponAccountInfo(this)
        }
    }

    override fun getIpInfo(cityCode: String, cityName: String) {
        ProxyManager()
            .setCityCode(cityCode)
            .setCityName(cityName)
            .setProxyRequestListener(object : ProxyRequestListener {
                override fun onProxyResult(result: Result<IpInfoBean>) {
                    taskInfoView.onResponIpInfo(result)
                }
            })
            .startProxy()
    }


    override fun getDeviceInfo(platformList: List<Int>) {
        Result(StatusCode.FAILED, DeviceInfoBean(), StatusMsg.DEFAULT.msg).run {
            val platform: Int = if (platformList.size == 1) platformList[0] else -1

            AndroidNetworking.get("${GET_DEVICE_INFO_URL}$platform")
                .build()
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String?) {
                        L.d("服务器返回的设备信息: $response")
                        try {
                            if (!response.isNullOrEmpty()) {
                                val deviceModel = Gson().fromJson(response, DeviceModel::class.java)
                                if (deviceModel.ret == 200) {
                                    val deviceInfoBean = deviceModel.data.deviceInfoBean
                                    deviceInfoBean.id = deviceModel.data.id

                                    this@run.r = deviceInfoBean
                                    this@run.code = StatusCode.SUCCEED
                                    this@run.msg = StatusMsg.SUCCEED.msg

                                    SPUtils.getInstance(SP_DEVICE_INFO).put(SPConstant.KEY_DEVICE_ID,deviceModel.data.id.toString())
                                } else this@run.msg = deviceModel.msg

                                taskInfoView.onResponDeviceInfo(this@run)
                            } else {
                                this@run.msg = "服务器返回数据为空"

                                taskInfoView.onResponDeviceInfo(this@run)
                            }
                        } catch (e: Exception) {
                            this@run.msg = e.message!!
                            taskInfoView.onResponDeviceInfo(this@run)
                        }
                    }

                    override fun onError(anError: ANError?) {
                        anError?.errorBody?.apply {
                            this@run.msg = this
                        }

                        taskInfoView.onResponDeviceInfo(this@run)
                    }
                })
        }
    }

    override fun getLocationByIP(ip: String) {
        LocationManager()
            .setLocationListener(object : LocationListener {
                override fun onLocationResult(latitude: String, longitude: String) {
                    taskInfoView.onResponIPAddress(latitude, longitude)
                }
            })
            .startLocation(ip)
    }

    override fun changeDeviceInfo(taskBean: TaskBean, platformList: List<Int>?) {
        Result(StatusCode.FAILED, false, StatusMsg.DEFAULT.msg).run {
            val deviceInfoBean = taskBean.device_info
            val pkgNameList = ArrayList<String>()

            platformList?.let {
                for (platform in it) {
                    val pkg = getPlatformPkg(platform)
                    if (pkg.isNotEmpty())
                        pkgNameList.add(pkg)
                }
            }
            if (pkgNameList.size == 0) {
                //添加所有用户程序的包名
                val userApps = AppUtils.getUserApps()
                for (pkg in userApps) {
                    pkgNameList.add(pkg.pkgName)
                }
            }

            DeviceInfoController()
                .setDeviceInfoListener(object : DeviceInfoListener {
                    override fun onChangeResult(result: Boolean) {
                        if (result) {
                            this@run.code = StatusCode.SUCCEED
                            this@run.msg = StatusMsg.SUCCEED.msg
                            this@run.r = true
                        } else this@run.msg = "修改设备信息失败"


                        addSelfForDeviceInfo(object : DeviceInfoListener {
                            override fun onChangeResult(result: Boolean) {
                                taskInfoView.onChangeDeviceInfo(this@run)
                            }
                        }, deviceInfoBean)
                    }
                })
                .addDeviceInfos(pkgNameList, deviceInfoBean)

        }
    }

    /**
     * 把本应用添加到hook列表
     */
    private fun addSelfForDeviceInfo(
        deviceInfoListener: DeviceInfoListener,
        deviceInfoBean: DeviceInfoBean
    ) {
        DeviceInfoController()
            .setDeviceInfoListener(deviceInfoListener)
            .addDeviceInfo(Utils.getApp().packageName, deviceInfoBean)
    }
}